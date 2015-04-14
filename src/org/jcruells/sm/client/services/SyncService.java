package org.jcruells.sm.client.services;

import java.util.ArrayList;
import java.util.Iterator;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.CheckInSvc;
import org.jcruells.sm.client.CheckInSvcApi;
import org.jcruells.sm.client.DoctorSvc;
import org.jcruells.sm.client.DoctorSvcApi;
import org.jcruells.sm.client.MedicationSvc;
import org.jcruells.sm.client.MedicationSvcApi;
import org.jcruells.sm.client.PatientSvc;
import org.jcruells.sm.client.PatientSvcApi;
import org.jcruells.sm.client.data.CheckIn;
import org.jcruells.sm.client.data.Doctor;
import org.jcruells.sm.client.data.LocalDBSvcApi;
import org.jcruells.sm.client.data.Patient;
import org.jcruells.sm.client.data.PatientMedication;
import org.jcruells.sm.client.data.SymptomDataContract;

import retrofit.RetrofitError;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class SyncService extends IntentService {

	//private ContentResolver cr;
	private App app;
	private int recordId;
	private CheckInSvcApi svcCheckIn;
	private MedicationSvcApi svcMedication;
	private PatientSvcApi svcPatient;
	private DoctorSvcApi svcDoctor;
	private LocalDBSvcApi localDBSvc;
	
	
	
	public SyncService() {
		super("SyncService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		app = (App) getApplicationContext();
		//cr = getContentResolver();
		app = (App) getApplicationContext();
		recordId = app.getUser().getRecordNumber();
		localDBSvc = new LocalDBSvcApi(this);
		
		Log.d(App.DEBUG_TAG, "on handle intent for record id: " + recordId);
	 	
		if (app.isPatientRole()) {
			syncPatient();
		} else {
			if (app.isDoctorRole()) {
				syncDoctor();
			}
		}  
	}

	private void syncPatient() {		
		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In syncPatient method");
		
		patientPullFromServer();
		patientPushToServer();
		
	}
	
	
	private void syncDoctor() {
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In syncDoctor method");
		
		doctorPullFromServer();
		doctorPushToServer();
		
	}

	/**
	 * This method allows a patient to pull the new medications assigned by
	 * her doctor ant to insert them in the local SQLite DB 
	 * @return a boolean indicating if the operation was succesful
	 */
	private boolean patientPullFromServer() {
		
		//showToast("patientPullFromServer inici");
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In patientPullFromServer method");
		boolean ok = true;
		
		ArrayList<PatientMedication> newMedications = null;
		Patient patient = localDBSvc.getPatientFromLocalDB(recordId);
		String lastSyncTimestamp;
		
		if (patient == null) {
			Log.d(App.DEBUG_TAG, "No patient found, exiting");
			return false;
		}
		
		lastSyncTimestamp = patient.getClientLastSyncTimestamp();
		
		if (svcMedication == null) svcMedication = MedicationSvc.init(app);
		
		
		// Get list of patient medications from last sync
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "Try to get new medications from timestamp: " + patient.getClientLastSyncTimestamp());
		
		try {
			newMedications = (ArrayList<PatientMedication>) svcMedication.getNewMedications(lastSyncTimestamp);
		} catch(RetrofitError e) {
			Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
			Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
			Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
			
			// If we are unauthorized, try to renew the token
			if (e.getResponse().getStatus() == 401) {
				
				Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
				app.setUserLoggedIn(false); // Force acquiring a new token
				svcCheckIn = CheckInSvc.init(app);
				try {
					newMedications = (ArrayList<PatientMedication>) svcMedication.getNewMedications(lastSyncTimestamp);
				} catch(RetrofitError e2) {
					Log.d(App.DEBUG_TAG, "Trying again... ");
					Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
					Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
					Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
					ok = false;
				}
			} else {
				ok = false;
			}
		}
	
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "got new medications form server " + newMedications.size());
		
		
		// If there are new medications, insert them in the local DB 
		if (ok && newMedications.size() > 0) {
			
			String message = localDBSvc.loadNewMedications(newMedications, recordId);
			showToast(message);
		} else {
			showToast("No new medications received from the server");
		}
		
		
		// Finally if we arrived here without errors, we get the last_sync_timestamp 
		// from the server and update it to the local database for being used in future syncs 
		// For the previous steps I ideally should be using transactions but for this 
		// non commercial project I would be avoiding  this extra complexity
		
		if (ok) {
			Log.d(App.DEBUG_TAG, "-------------------------------------------------");
			Log.d(App.DEBUG_TAG, "Update last sync timestamp in local database ");
			updatePatientLastSyncTimestampFromServer();
		} 
		
		Log.d(App.DEBUG_TAG, " return form patientPushToServer");
		
		
		return ok;
	}
	
	/**
	 * This method allows a patient to push to the server his latest checkins
	 * This method allows a patient to send its updated data to the remote server
	 * @return a boolean indicating if the operation was succesful
	 */
	private boolean patientPushToServer() {
		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In patientPushToServer method");
		
		boolean ok = true;
		App app = ((App)getApplicationContext());
		
		//Get the checkins to be synced from the local database		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "Get the checkins to be synced from the local database");
		
		ArrayList<CheckIn> checkins = localDBSvc.getCheckinsToSyncFromLocalDB(recordId);
		
		if (checkins.size() == 0) { //Nothing to push to the server, exiting ok
			Log.d(App.DEBUG_TAG, "NO check-ins to push to server, exiting");
		} 
		
		Log.d(App.DEBUG_TAG, " create CheckInSvc");
		Log.d(App.DEBUG_TAG, "user " + app.getUser().getUsername());
		Log.d(App.DEBUG_TAG, "password " + app.getUser().getPassword());
		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "Add the new checkins to the remote server");	
		
		if (checkins.size() > 0) {
			if (svcCheckIn == null) svcCheckIn = CheckInSvc.init(app);
			try {
				svcCheckIn.addNewCheckIns(checkins);
			} catch(RetrofitError e) {
				Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
				Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
				Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
				
				// If we are unathorized, try to renew the token
				if (e.getResponse().getStatus() == 401) {
					
					Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
					app.setUserLoggedIn(false);
					svcCheckIn = CheckInSvc.init(app);
					try {
						svcCheckIn.addNewCheckIns(checkins);
					} catch(RetrofitError e2) {
						Log.d(App.DEBUG_TAG, "Trying again... ");
						Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
						Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
						Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
						ok = false;
					}
				} else {
					ok = false;
				}
			}
			
			//If checkins have been sent ok to the server, we must update its local state
			//to synced in the local database
			//Ideally we should be blocking any attempt to update this checkins in the local 
			//database during the lapse of time they are sent to the server
			if (ok) {
				localDBSvc.updateCheckinsToSynchedInLocalDB(checkins);
			}
		}
		
		
		
		//Send the state of the patient to the server
		Log.d(App.DEBUG_TAG, "Send the state of the patient to the server");
		Patient patient = localDBSvc.getPatientFromLocalDB(recordId);
		
		if (patient.getSynced() == SymptomDataContract.STATE_NOT_SYNCED) {
			if (svcPatient == null) svcPatient = PatientSvc.init(app);
			try {
				svcPatient.updatePatient(patient);
			} catch(RetrofitError e) {
				Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
				Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
				Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
				
				// If we are unathorized, try to renew the token
				if (e.getResponse().getStatus() == 401) {
					
					Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
					app.setUserLoggedIn(false);
					svcCheckIn = CheckInSvc.init(app);
					try {
						svcPatient.updatePatient(patient);
					} catch(RetrofitError e2) {
						Log.d(App.DEBUG_TAG, "Trying again... ");
						Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
						Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
						Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
						ok = false;
					}
				} else {
					ok = false;
				}
			}
			if(ok) {
				Log.d(App.DEBUG_TAG, "patient state sent to server, now update synced in local db");
				localDBSvc.updatePatientToSynchedInLocalDB(recordId);
			}
		}		
		
		Log.d(App.DEBUG_TAG, " return form patientPushToServer");
		
		final int size = checkins.size();
		if (ok) {
			if (size > 0) {
				showToast((new StringBuilder()).append(size).append(pluralize(" check-in", size)).append(" sent to the server").toString());
			} else {
				showToast("No new checkins to sent to the server");
			}
		}
		
		//showToast("patientPushToServer fi");
		return ok;
	}

	/**
	 * This method allows a doctor to pull new checkins of their patients from 
	 * the server. This records are then stored in the local database.  
	 * This method also gets fresh data from their patients and updates this  
	 * data in the local SQLIte DB.
	 * @return a boolean indicating if the operation was succesful
	 */
	private boolean doctorPullFromServer() {
		
		
		//showToast("doctorPullFromServer inici");
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In doctorPullFromServer method");
		boolean ok = true;
		ArrayList<CheckIn> newCheckIns = new ArrayList<CheckIn>();
		ArrayList<Patient> updatedPatients = new ArrayList<Patient>();
		
		
		Doctor doctor;
		//Doctor doctor = getDoctorFromLocalDB(recordId);
		
		if (svcCheckIn == null) svcCheckIn = CheckInSvc.init(app);
		
		// Get the doctor last sync timestamp
		
		doctor = localDBSvc.getDoctorFromLocalDB(recordId);
		
		if (doctor == null) {
			Log.d(App.DEBUG_TAG, "No point at all! Doctor should exist!");
			return false;
		}
		String lastSyncTimestamp = doctor.getClientLastSyncTimestamp();
		
		// Get the new check-ins
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "Get the new check-ins from the server");
		
		try {
			newCheckIns = (ArrayList<CheckIn>) svcCheckIn.getNewCheckIns(lastSyncTimestamp);			
		} catch(RetrofitError e) {
			Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
			Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
			Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
			
			// If we are unathorized, try to renew the token
			if (e.getResponse().getStatus() == 401) {
				
				Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
				app.setUserLoggedIn(false); //forcing to renew to token
				svcCheckIn = CheckInSvc.init(app);
				try {
					newCheckIns = (ArrayList<CheckIn>) svcCheckIn.getNewCheckIns(lastSyncTimestamp);	
				} catch(RetrofitError e2) {
					Log.d(App.DEBUG_TAG, "Trying again... ");
					Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
					Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
					Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
					ok = false;
				}
			} else {
				ok = false;
			}
		}
		
		// Get the updated data from his patients
		if (ok) {
			if (svcPatient == null) svcPatient = PatientSvc.init(app);
			
			// Get the new check-ins
			Log.d(App.DEBUG_TAG, "-------------------------------------------------");
			Log.d(App.DEBUG_TAG, "Get the new patients data from the server");
			
			try {
				updatedPatients = (ArrayList<Patient>) svcPatient.getUpdatedPatients(lastSyncTimestamp);			
			} catch(RetrofitError e) {
				Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
				Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
				Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
				
				// If we are unathorized, try to renew the token
				if (e.getResponse().getStatus() == 401) {
					
					Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
					app.setUserLoggedIn(false); //forcing to renew to token
					svcCheckIn = CheckInSvc.init(app);
					try {
						updatedPatients = (ArrayList<Patient>) svcPatient.getUpdatedPatients(lastSyncTimestamp);	
					} catch(RetrofitError e2) {
						Log.d(App.DEBUG_TAG, "Trying again... ");
						Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
						Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
						Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
						ok = false;
					}
				} else {
					ok = false;
				}
			}
			
			Log.d(App.DEBUG_TAG, "Gotten " + updatedPatients.size() + " updated patients from server");
			
		}
		
		// Insert the new Checkins in the local SQLite DB
		if (newCheckIns.size() > 0) {
			final String checkinsMsg = localDBSvc.insertNewCheckinsPulledFromServer(newCheckIns, recordId);
			showToast(checkinsMsg);
		}
		if (updatedPatients.size() > 0) {
			final String updatedPatientsMsg = localDBSvc.updatePatientsPulledFormServer(updatedPatients);
			showToast(updatedPatientsMsg);
		}
		
		if (newCheckIns.size() == 0 && updatedPatients.size() == 0) {
			showToast("No new data pulled from the server");
		}
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "Update last sync timestamp in local database ");
		updateDoctorLastSyncTimestampFromServer();
		
		//showToast("doctorPullFromServer fi");
		
		return ok;
	}

	/**
	 * This method allows a doctor to push new medications of their patients to 
	 * the server. 
	 * @return a boolean indicating if the operation was succesful
	 */
	private boolean doctorPushToServer() {
		
		//showToast("doctorPushToServer inici");
		
		Log.d(App.DEBUG_TAG, "In doctorPushToServer method");
		boolean ok = true;
		
		//Get all new and not synced medications form local database 
		ArrayList<PatientMedication> medications = localDBSvc.getMedicationsToSyncFromLocalDB(recordId);
		
		Log.d(App.DEBUG_TAG,"Gott " + medications.size());
		
		if (svcMedication == null) svcMedication = MedicationSvc.init(app);
		
		if (medications.size() > 0) {
			
			try {
				svcMedication.updatePatientMedications(medications);			
			} catch(RetrofitError e) {
				Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
				Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
				Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
				
				// If we are unathorized, try to renew the token
				if (e.getResponse().getStatus() == 401) {
					
					Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
					app.setUserLoggedIn(false); //forcing to renew to token
					svcCheckIn = CheckInSvc.init(app);
					try {
						svcMedication.updatePatientMedications(medications);	
					} catch(RetrofitError e2) {
						Log.d(App.DEBUG_TAG, "Trying again... ");
						Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
						Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
						Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
						ok = false;
					}
				} else {
					ok = false;
				}
			}
		} 
		
		if (ok) {
			if (medications.size() > 0) {
				
				//Update the medications to state synced in the local database
				localDBSvc.updateMedicationsToSynchedInLocalDB(medications, recordId);
				
				showToast("New medications have been sent to the server");
			} else {
				showToast("No new medications to send to the server");
			}
		}
		
		//showToast("doctorPushToServer fi");
		
		return ok;
	}
	
	
	private boolean updatePatientLastSyncTimestampFromServer() {
		
		Log.d(App.DEBUG_TAG, "in updateLastSyncTimestampFromServer");
		
		boolean ok = true;
		
		if (svcPatient == null) { svcPatient = PatientSvc.init(app); }
		
		Patient p = null;
		
		try {
			p = svcPatient.getPatient(recordId);			
		} catch(RetrofitError e) {
			Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
			Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
			Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
			
			// If we are unathorized, try to renew the token
			if (e.getResponse().getStatus() == 401) {
				
				Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
				app.setUserLoggedIn(false); //forcing to renew to token
				svcCheckIn = CheckInSvc.init(app);
				try {
					p = svcPatient.getPatient(recordId);	
				} catch(RetrofitError e2) {
					Log.d(App.DEBUG_TAG, "Trying again... ");
					Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
					Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
					Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
					ok = false;
				}
			} else {
				ok = false;
			}
			
		}
		
		if (ok) {
			String clientLastSyncTimestamp = p.getClientLastSyncTimestamp();
			ok = localDBSvc.updatePatientLastSyncTimestamp(clientLastSyncTimestamp, recordId);
		}
		
		if(ok) {
			Log.d(App.DEBUG_TAG, "updated patient");} 
		else
			{ Log.d(App.DEBUG_TAG, "not updated patient");}
		
		return ok;
		
	}
	
	
	private boolean updateDoctorLastSyncTimestampFromServer() {
		
		Log.d(App.DEBUG_TAG, "in updateLastSyncTimestampFromServer");
		
		boolean ok = true;
		
		if (svcDoctor == null) { svcDoctor = DoctorSvc.init(app); }
		
		Doctor d = null;
		
		try {
			d = svcDoctor.getDoctor(recordId);			
		} catch(RetrofitError e) {
			Log.d(App.DEBUG_TAG, "message: " + e.getMessage());
			Log.d(App.DEBUG_TAG, "cause: " +  e.getCause());
			Log.d(App.DEBUG_TAG, "staus code: " + e.getResponse().getStatus());
			
			// If we are unathorized, try to renew the token
			if (e.getResponse().getStatus() == 401) {
				
				Log.d(App.DEBUG_TAG, "Got 401 unathorized ... ");
				app.setUserLoggedIn(false); //forcing to renew to token
				svcCheckIn = CheckInSvc.init(app);
				try {
					d = svcDoctor.getDoctor(recordId);			
				} catch(RetrofitError e2) {
					Log.d(App.DEBUG_TAG, "Trying again... ");
					Log.d(App.DEBUG_TAG, "message: " + e2.getMessage());
					Log.d(App.DEBUG_TAG, "cause: " +  e2.getCause());
					Log.d(App.DEBUG_TAG, "staus code: " + e2.getResponse().getStatus());
					ok = false;
				}
			} else {
				ok = false;
			}
			
		}
		
		if (ok) {
			String clientLastSyncTimestamp = d.getClientLastSyncTimestamp();
			Log.d(App.DEBUG_TAG, "update client last synch to " + clientLastSyncTimestamp);
			
			ok = localDBSvc.updateDoctorLastSyncTimestamp(clientLastSyncTimestamp, recordId);
		
		}
		
		if(ok) {
			Log.d(App.DEBUG_TAG, "updated doctor");} 
		else
			{ Log.d(App.DEBUG_TAG, "not updated doctor");}
		
		return ok;
		
	}
	
	
	
	private void showToast(final String sText)
	  { 
		
		 final Context MyContext = this;
	     new Handler(Looper.getMainLooper()).post(new Runnable()
	     {  @Override public void run()
	        {  Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_SHORT);
	           toast1.show(); 
	        }
	     });
	     
	     
	  };
	  
	  private String pluralize(String s, int n) {
		  return (new StringBuilder(s)).append(n>1 ? "s" : "").toString();
	  }
}

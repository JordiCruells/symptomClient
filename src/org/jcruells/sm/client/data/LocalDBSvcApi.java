package org.jcruells.sm.client.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.doctor.PatientAlertReceiver;
import org.jcruells.sm.client.patient.ChangedMedicationReceiver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class LocalDBSvcApi {
	
	private Context ctx;
	private ContentResolver cr;
	
	public LocalDBSvcApi(Context ctx) {
		this.ctx = ctx;
		this.cr = ctx.getContentResolver();
	}
	
	public Patient getPatientFromLocalDB(int recordId) {
		
		String selection = SymptomDataContract.PATIENT_RECORD_ID + " = ?";
		String[] selectionArgs = {String.valueOf(recordId)};
		
		Log.d(App.DEBUG_TAG, "create cursor to get the patient");
		
		
		// Get the current patient data
		Cursor c = cr.query(SymptomDataContract.PATIENTS_URI, SymptomDataContract.PATIENT_ALL_COLUMNS, 
				selection, selectionArgs, null);
		Patient patient = null;
		
		if (c.moveToFirst()) {
			patient = new Patient(
					c.getLong(c.getColumnIndex(SymptomDataContract.ID)),
					c.getInt(c.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)),
					c.getLong(c.getColumnIndex(SymptomDataContract.DOCTOR_ID)),
					c.getString(c.getColumnIndex(SymptomDataContract.PATIENT_NAME)),
					c.getString(c.getColumnIndex(SymptomDataContract.PATIENT_LAST_NAME)),
					c.getString(c.getColumnIndex(SymptomDataContract.PATIENT_BIRTHDAY)),
					c.getInt(c.getColumnIndex(SymptomDataContract.SEVERE_PAIN_MINUTES)),
					c.getInt(c.getColumnIndex(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES)),
					c.getInt(c.getColumnIndex(SymptomDataContract.NO_EAT_MINUTES)),
					c.getInt(c.getColumnIndex(SymptomDataContract.PAIN_LEVEL)),
					c.getInt(c.getColumnIndex(SymptomDataContract.STOPPED_EATING_LEVEL)),
					c.getString(c.getColumnIndex(SymptomDataContract.LAST_CHECKIN_DATETIME)),
					c.getString(c.getColumnIndex(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP)),
					c.getString(c.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
					c.getInt(c.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
					c.getInt(c.getColumnIndex(SymptomDataContract.SYNCED)));
			Log.d(App.DEBUG_TAG, "patient created " + patient.getPatientName());
		}
		c.close();
		
		return patient;
	}
	
	
	public ArrayList<CheckIn> getCheckinsToSyncFromLocalDB(int recordId) { 
	
		ArrayList<CheckIn> checkins = new ArrayList<CheckIn>();
		CheckIn checkin;
		
		String checkinsToPushServerSelection = SymptomDataContract.PATIENT_RECORD_ID + "= ? AND " +
				SymptomDataContract.SYNCED + " = ? ";
		String[] checkinsToPushServerArgs = new String[]{String.valueOf(recordId), 
				                                         String.valueOf(SymptomDataContract.STATE_NOT_SYNCED)};
		Cursor cursor = cr.query(SymptomDataContract.CHECKINS_URI, 
				                 SymptomDataContract.CHECKINS_ALL_COLUMNS, 
				                 checkinsToPushServerSelection, 
				                 checkinsToPushServerArgs , 
				                 SymptomDataContract.ID);
		
		Log.d(App.DEBUG_TAG, "selection:" + checkinsToPushServerSelection);
		Log.d(App.DEBUG_TAG, "criteria:" + checkinsToPushServerArgs);
		
		// Turn cursor into an arraylist of Checkins
		Log.d(App.DEBUG_TAG, cursor.getCount() + "records form the cursor");
		
		if (cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					
					checkin = new CheckIn(
							cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)), 
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.CHECK_IN_DATETIME)), 
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PAIN_LEVEL)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.MEDICATION_TAKEN)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_DATETIME)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_ANSWERS)), 
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.STOPPED_EATING_LEVEL)),
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNCED))
							);
					
					checkins.add(checkin);
					Log.d(App.DEBUG_TAG, "new checkin added to collection: " + checkin);
					
				} while (cursor.moveToNext());
				
			}
		}
		cursor.close();
		
		return checkins;
	}
	
	
	public ArrayList<PatientMedication> getMedicationsToSyncFromLocalDB(int recordId) {
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In getMedicationsToSyncFromLocalDB method");
		
		ArrayList<PatientMedication> patientMedications = new  ArrayList<PatientMedication>();
		Uri patientMedicationsDoctor = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_DOCTOR_URI,String.valueOf(recordId));
		
		Log.d(App.DEBUG_TAG, "SymptomDataContract.PATIENT_MEDICATIONS_DOCTOR_URI :" + SymptomDataContract.PATIENT_MEDICATIONS_DOCTOR_URI.toString());
		Log.d(App.DEBUG_TAG, "patientMedicationsDoctor: " + patientMedicationsDoctor.toString());
		
		Cursor cursor = cr.query(patientMedicationsDoctor, null, null, null, null);
		PatientMedication patientMedication;
		
		if (cursor.moveToFirst()) {
			do {
				patientMedication = new PatientMedication(
						cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_ID)),
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_FROM)),
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_TO)),
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNCED))
						);
				patientMedications.add(patientMedication);
				
			} while(cursor.moveToNext());
		}
		cursor.close();
		
		return patientMedications;
	}
	
	
	public void updateCheckinsToSynchedInLocalDB(ArrayList<CheckIn> checkins) {
	
		Log.d(App.DEBUG_TAG, "updateCheckinsToSynchedInLocalDB, rows to update = " + checkins.size());
		Iterator<CheckIn> i = checkins.iterator();
		final String where = SymptomDataContract.ID + " = ?";
		final String[] selectionArgs = new String[1];
		final ContentValues values = new ContentValues();
		values.put(SymptomDataContract.SYNCED, String.valueOf(SymptomDataContract.STATE_SYNCED));
		
		while(i.hasNext()) {
			selectionArgs[0] = String.valueOf(i.next().getId());
			cr.update(SymptomDataContract.CHECKINS_URI, values, where, selectionArgs);
			Log.d(App.DEBUG_TAG, "checkin updated to synced");
		}
	}
	
	public void updatePatientToSynchedInLocalDB(int recordId) {
		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In updatePatientToSynchedInLocalDB method");
		
		final ContentValues values = new ContentValues();
		values.put(SymptomDataContract.SYNCED, String.valueOf(SymptomDataContract.STATE_SYNCED));
		final String where = SymptomDataContract.PATIENT_RECORD_ID + " = ?";
		final String[] selectionArgs = {String.valueOf(recordId)};
		int updatedRows = 0;
		if ((updatedRows = cr.update(SymptomDataContract.PATIENTS_URI, values, where, selectionArgs)) != 1) {
			Log.d(App.DEBUG_TAG, updatedRows +  " updated");
		} else {
			Log.d(App.DEBUG_TAG, "succesfully updated");
		}
		
	};
	
	
	public void updateMedicationsToSynchedInLocalDB(ArrayList<PatientMedication> medications, int recordId) {
		
		Log.d(App.DEBUG_TAG, "updateMedicationsToSynchedInLocalDB, rows to update = " + medications.size());
		Iterator<PatientMedication> i = medications.iterator();
		final String where = SymptomDataContract.PATIENT_RECORD_ID + " = ? AND " +
							 SymptomDataContract.PATIENT_MEDICATION_ID + " = ?";
		final String[] selectionArgs = new String[2];
		final ContentValues values = new ContentValues();
		PatientMedication medication;
		values.put(SymptomDataContract.SYNCED, String.valueOf(SymptomDataContract.STATE_SYNCED));
		Uri patientMedicationsUri = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, String.valueOf(recordId));
		while(i.hasNext()) {
			medication = i.next();
			selectionArgs[0] = String.valueOf(medication.getPatientRecordId());
			selectionArgs[1] = String.valueOf(medication.getPatientMedicationId());
			cr.update(patientMedicationsUri, values, where, selectionArgs);
			Log.d(App.DEBUG_TAG, "medication updated to synced");
		}
		
	}
	
	public Doctor getDoctorFromLocalDB(long doctorId) {
		
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In getDoctorFromLocalDB method");
		
		Doctor doctor = null;
		
		String selection = SymptomDataContract.USER_ID + " = ?";
		String[] selectionArgs = {String.valueOf(doctorId)};
		
		Log.d(App.DEBUG_TAG, "create cursor to get the doctor");
		
		
		// Get the current doctor data
		Cursor c = cr.query(SymptomDataContract.DOCTORS_URI, SymptomDataContract.DOCTOR_ALL_COLUMNS, 
				selection, selectionArgs, null);
		
		if (c.moveToFirst()) {
			doctor = new Doctor(
					c.getLong(c.getColumnIndex(SymptomDataContract.ID)),
					c.getLong(c.getColumnIndex(SymptomDataContract.USER_ID)),
					c.getString(c.getColumnIndex(SymptomDataContract.DOCTOR_NAME)),
					c.getString(c.getColumnIndex(SymptomDataContract.DOCTOR_LAST_NAME)),
					c.getString(c.getColumnIndex(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP)),
					c.getString(c.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
					c.getInt(c.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
					c.getInt(c.getColumnIndex(SymptomDataContract.SYNCED)));
			Log.d(App.DEBUG_TAG, "Doctor created " + doctor.getDoctorName());
		}
		
		c.close();
		return doctor;
	}
	
	
	public String insertNewCheckinsPulledFromServer(ArrayList<CheckIn> newCheckIns, int recordId) {

		int numNewCheckins = 0;
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In insertNewCheckinsPulledFromServer method");
		CheckIn newCheckIn;
		ContentValues values = new ContentValues();
		Iterator<CheckIn> iNewCheckins = newCheckIns.iterator();
		Uri doctorGetCheckInFromServerUri = Uri.withAppendedPath(SymptomDataContract.CHECKINS_URI, String.valueOf(recordId));
		while(iNewCheckins.hasNext()) {
			newCheckIn =  iNewCheckins.next();
			values = newCheckIn.toContentValues();
			values.remove(SymptomDataContract.ID);
			values.put(SymptomDataContract.SYNCED, String.valueOf(SymptomDataContract.STATE_SYNCED));
			if (cr.insert(doctorGetCheckInFromServerUri, values) == null) {
				Log.d(App.DEBUG_TAG, "Couldn't insert checkin: " + newCheckIn);
			} else {
				Log.d(App.DEBUG_TAG, "New check-in inserted");
			}
			values.clear();
		}
		
		numNewCheckins = newCheckIns.size();
			
		StringBuilder message = new StringBuilder();
		if (numNewCheckins > 0) {
			message.append(numNewCheckins)
				   .append(pluralize(" new check-in", numNewCheckins))
				   .append(" received. ");
		}
		return message.toString(); 
		
	}
	
	
	
	public String updatePatientsPulledFormServer(ArrayList<Patient> updatedPatients) {
		Log.d(App.DEBUG_TAG, "-------------------------------------------------");
		Log.d(App.DEBUG_TAG, "In updatePatientsPulledFormServer method");
		Patient updatedPatient;
		ContentValues values = new ContentValues();
		String where = SymptomDataContract.PATIENT_RECORD_ID + " = ? "; 
		String[] selectionArgs = new String[1];
		int numberPatientsCanNotEat = 0;
		int numberOfPatientsWithSeverePain = 0;
		int numberOfPatientsWithModerateToSeverePain = 0;
		int numUpdatedPatients = updatedPatients.size();
		
		Iterator<Patient> iUpdatedPatients = updatedPatients.iterator();
		while(iUpdatedPatients.hasNext()) {
			updatedPatient = iUpdatedPatients.next();
			
			selectionArgs[0] = String.valueOf(updatedPatient.getRecordId());
			Log.d(App.DEBUG_TAG, "Gonna update for the patient id: " + selectionArgs[0]);
			
			Log.d(App.DEBUG_TAG, "severepainminutes" + updatedPatient.getSeverePainMinutes());
			Log.d(App.DEBUG_TAG, "moderatetoseverepainminutes" + updatedPatient.getModerateToSeverePainMinutes());
			Log.d(App.DEBUG_TAG, "stoppedeating level" + updatedPatient.getStoppedEatingLevel());
			
			//Check if the patient is in any of the critical states that must generate an alarm
			// and update the appropiate counter
			if (updatedPatient.getSeverePainMinutes() > 1) {
				numberOfPatientsWithSeverePain ++;
			} else {
				if (updatedPatient.getModerateToSeverePainMinutes() > 1) {
					numberOfPatientsWithModerateToSeverePain ++;
				}
			}
			if (updatedPatient.getStoppedEatingLevel() > 1) {
				numberPatientsCanNotEat ++;
			}
					
			// update the values
			values.put(SymptomDataContract. MODERATE_TO_SEVERE_PAIN_MINUTES, updatedPatient.getSeverePainMinutes());
			values.put(SymptomDataContract. MODERATE_TO_SEVERE_PAIN_MINUTES, updatedPatient.getModerateToSeverePainMinutes());
			values.put(SymptomDataContract.NO_EAT_MINUTES, updatedPatient.getNoEatMinutes());
			values.put(SymptomDataContract.PAIN_LEVEL, updatedPatient.getPainLevel());
			values.put(SymptomDataContract.STOPPED_EATING_LEVEL, updatedPatient.getStoppedEatingLevel());
			values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, updatedPatient.getLastCheckinDatetime());
			values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, updatedPatient.getClientLastSyncTimestamp());
			values.put(SymptomDataContract.SERVER_TIMESTAMP, updatedPatient.getServerTimestamp());
			values.put(SymptomDataContract.SYNC_ACTION, updatedPatient.getSyncAction());
			values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED);
			
			
			cr.update(SymptomDataContract.PATIENTS_URI, values, where, selectionArgs);
			
			values.clear();
		}
		
		Log.d(App.DEBUG_TAG, "severepainminutes total" + numberPatientsCanNotEat);
		Log.d(App.DEBUG_TAG, "moderatetoseverepainminutes total " + numberOfPatientsWithSeverePain);
		Log.d(App.DEBUG_TAG, "stoppedeating level total " + numberOfPatientsWithModerateToSeverePain);
		
		StringBuilder message = new StringBuilder();
		
		if (numUpdatedPatients > 0) {
			message.append(numUpdatedPatients)
			       .append(pluralize(" patient", numUpdatedPatients)) 
				   .append(" with updated information. ");
		
		
			if (numberOfPatientsWithSeverePain > 0) {
				message.append(numberOfPatientsWithSeverePain)
				       .append(pluralize(" patient",numberOfPatientsWithSeverePain))
				       .append(" with severe pain. ");
			}
			if (numberOfPatientsWithModerateToSeverePain > 0) {
				message.append(numberOfPatientsWithModerateToSeverePain)
					   .append(pluralize(" patient",numberOfPatientsWithModerateToSeverePain))
				       .append(" with moderate to severe pain. ");
			}
			if (numberPatientsCanNotEat > 0) {
				message.append(numberPatientsCanNotEat)
				        .append(pluralize(" patient", numberPatientsCanNotEat))
				        .append(" can not eat. ");
			}
		} else {
			message.append("No new patient\'s data to sync from the server. ");
		}
		
		
		// Generate a notification
		if (numberOfPatientsWithSeverePain > 0 || 
			numberOfPatientsWithModerateToSeverePain > 0 ||	
			numberPatientsCanNotEat	> 0) {
			
			Intent intent = new Intent(PatientAlertReceiver.INCOMING_PATIENT_ALERT);
			Log.d(App.DEBUG_TAG, "sending broadcast INCOMING_PATIENT_ALERT_INTENT");
			ctx.sendBroadcast(intent);
			
		}
		
		
		Log.d(App.DEBUG_TAG, "Return from updatePatientsPulledFormServer");
		
		
		return message.toString();
	}; 		

	
	
	
	public String loadNewMedications(ArrayList<PatientMedication>newMedications, int recordId) {
	
		String message;
		Iterator<PatientMedication> i = newMedications.iterator();
		int medicationsNew = 0;
		int medicationsOld = 0;
		
		while(i.hasNext()) {
			Log.d(App.DEBUG_TAG, "insert new patient medication");
			PatientMedication medication = i.next();
			String appendedUri = String.valueOf(recordId);
			Uri uriPatientMedications = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, appendedUri);
			
			int rowsUpdated;
			
			if (medication.getSyncAction() == SymptomDataContract.SYNC_UPDATE) {
				medicationsOld ++;
				ContentValues values = new ContentValues();
				values.put(SymptomDataContract.PATIENT_MEDICATION_TO, medication.getPatientMedicationTo());
				values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED);
				final String where = SymptomDataContract.PATIENT_RECORD_ID + " = ? AND " + SymptomDataContract.PATIENT_MEDICATION_ID + " = ?";
				final String[] selectionArgs = {String.valueOf(recordId), String.valueOf(medication.getPatientMedicationId())};
				Log.d(App.DEBUG_TAG, "update where : " + where + " " + selectionArgs[0] + "-" + selectionArgs[1]);
				
				if ((rowsUpdated = cr.update(uriPatientMedications, values, where, selectionArgs)) != 1) {
					Log.d(App.DEBUG_TAG, "Uppss, " + rowsUpdated + " rows updated !?");
				}
				
			} else {
				medicationsNew ++;
				ContentValues values = medication.toContentValues();
				values.remove(SymptomDataContract.ID);
				values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED);
				Log.d(App.DEBUG_TAG, "values:" + values);
				cr.insert(SymptomDataContract.PATIENT_MEDICATIONS_URI, values);
				if (cr.insert(uriPatientMedications, values) == null) {
					Log.d(App.DEBUG_TAG, "Could not insert new patient medication into the local database");
					return "Could not insert new patient medication into the local database";
				} else {
					Log.d(App.DEBUG_TAG, "Patient medication inserted");					
				}
			}
		}
		

		final int size = newMedications.size();
		if(size > 0) {		
			StringBuilder sb = new StringBuilder();
			if (medicationsNew > 0) {
				sb.append(medicationsNew).append(pluralize(" medication",size)).append(" new. ");
			}
			if (medicationsOld > 0) {
				sb.append(medicationsOld).append(pluralize(" medication", medicationsOld)).append(" removed");
			}
			Intent intent = new Intent(ChangedMedicationReceiver.CHANGED_MEDICATION_ALERT);
			intent.putExtra(ChangedMedicationReceiver.RECORD_ID, recordId);
			ctx.sendBroadcast(intent);
			return sb.toString();
				
		} else {
			return "No new medications got from the server";
		}		
		
	}
	
	
	
	

	public boolean updatePatientLastSyncTimestamp(String clientLastSyncTimestamp, int recordId) {
	
		boolean ok = true;
		Log.d(App.DEBUG_TAG, "update client last synch to " + clientLastSyncTimestamp);
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, clientLastSyncTimestamp);
		final String where = SymptomDataContract.PATIENT_RECORD_ID + " = ?";
		final String[] selectionArgs = {String.valueOf(recordId)};
		
		Log.d(App.DEBUG_TAG, "where: " + where);
		Log.d(App.DEBUG_TAG, "selectionArgs: " + selectionArgs[0]);
		
		if (!(cr.update(SymptomDataContract.PATIENTS_URI, values, where, selectionArgs) > 0)) {
			Log.e(App.DEBUG_TAG, "1 row should be updated in patient");
			ok = false;
		};
		return ok;
	}

	public boolean updateDoctorLastSyncTimestamp(String clientLastSyncTimestamp, int recordId) {
	
		boolean ok = true;
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, clientLastSyncTimestamp);
		final String where = SymptomDataContract.USER_ID + " = ?";
		final String[] selectionArgs = {String.valueOf(recordId)};
		
		Log.d(App.DEBUG_TAG, "where: " + where);
		Log.d(App.DEBUG_TAG, "selectionArgs: " + selectionArgs[0]);
		
		if (!(cr.update(SymptomDataContract.DOCTORS_URI, values, where, selectionArgs) > 0)) {
			Log.e(App.DEBUG_TAG, "1 row should be updated in doctors");
			ok = false;
		};
		return ok;
	}
	
    private String pluralize(String s, int n) {
		  return (new StringBuilder(s)).append(n>1 ? "s" : "").toString();
	}
}

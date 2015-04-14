package org.jcruells.sm.client.doctor;


import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.Doctor;
import org.jcruells.sm.client.data.LocalDBSvcApi;
import org.jcruells.sm.client.patient.CheckinsListActivity;
import org.jcruells.sm.client.services.SyncService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DoctorActivity extends FragmentActivity {
	
	@InjectView(R.id.doctorName)
	protected TextView doctorName;
	
	@InjectView(R.id.lastSync)
	protected TextView lastSync;
	
	@InjectView(R.id.btnSynchronize)
	protected Button btnSynchronize;
	
	@InjectView(R.id.btnAlarms)
	protected Button btnAlarms;
	
	@InjectView(R.id.btnPatients)
	protected Button btnPatients;
	
	@InjectView(R.id.btnCheckins)
	protected Button btnCheckins;
		
	private App app;
	private int doctorId;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.d(App.DEBUG_TAG, "DoctorActivity onCreate");
		setContentView(R.layout.doctor_activity);
		app = (App) getApplicationContext();
		ButterKnife.inject(this);
		doctorId = app.getUser().getRecordNumber();
		Doctor d = (new LocalDBSvcApi(app)).getDoctorFromLocalDB(doctorId);
		doctorName.setText(d.getDoctorName() + " " + d.getDoctorLastName());
		lastSync.setText(d.getClientLastSyncTimestamp());
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Log.d(App.DEBUG_TAG, "DoctorActivity onResume");
		doctorId = app.getUser().getRecordNumber();
		Doctor d = (new LocalDBSvcApi(app)).getDoctorFromLocalDB(doctorId);
		lastSync.setText(d.getClientLastSyncTimestamp());
	}

	
	@OnClick(R.id.btnSynchronize)
	public void synchronize() {
		Log.d(App.DEBUG_TAG, "onclick synchronize");
		Intent intent = new Intent(this, SyncService.class);
		Log.d(App.DEBUG_TAG, "start service");
		startService(intent);
	}
	
	@OnClick(R.id.btnAlarms)
	public void openPatientsAlarms() {
		Log.d(App.DEBUG_TAG, "onclick open patients alarms");
		Intent intent = new Intent(this, PatientsAlarmsListActivity.class);
		intent.putExtra(PatientsAlarmsListActivity.FILTER, PatientsAlarmsListActivity.FILTER_PATIENT_ALARMS);
		Log.d(App.DEBUG_TAG, "start service");
		startActivity(intent);
	}
	
	@OnClick(R.id.btnPatients)
	public void openPatients() {
		DialogFragment searchFragment = new SearchPatientDialogFragment();
	    searchFragment.show(getSupportFragmentManager(), "missiles");

	    /*		
 		Log.d(App.DEBUG_TAG, "onclick open patients");
		Intent intent = new Intent(this, PatientsAlarmsListActivity.class);
		intent.putExtra(PatientsAlarmsListActivity.FILTER, PatientsAlarmsListActivity.FILTER_PATIENTS_DOCTOR);
		Log.d(App.DEBUG_TAG, "start service");
		startActivity(intent);
		*/
	}
	
	@OnClick(R.id.btnCheckins)
	public void openLastCheckins() {
		Log.d(App.DEBUG_TAG, "onclick open last checkins");
		Intent intent = new Intent(this, CheckinsListActivity.class);
		intent.putExtra(CheckinsListActivity.FILTER, CheckinsListActivity.FILTER_ALL_CHECKINS_PATIENTS_DOCTOR);
		Log.d(App.DEBUG_TAG, "start service");
		startActivity(intent);
	}
	
	private class SearchPatientDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        builder.setView(inflater.inflate(R.layout.search_patient_dialog, null));
	        builder.setMessage(R.string.search_patient)
	               .setPositiveButton(R.string.action_search, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // PERFORM THE SEARCH
	                	   Log.d(App.DEBUG_TAG, "Perfom the search");
	                	   
	                	   EditText patientName = (EditText) ((AlertDialog) dialog).findViewById(R.id.patientName);
	                	   EditText patientLastName = (EditText) ((AlertDialog) dialog).findViewById(R.id.patientLastName);
	                	   
	               		   Intent intent = new Intent(DoctorActivity.this, PatientsAlarmsListActivity.class);
	               		   intent.putExtra(PatientsAlarmsListActivity.FILTER, PatientsAlarmsListActivity.FILTER_PATIENTS_DOCTOR);
	               		   intent.putExtra(PatientsAlarmsListActivity.FILTER_NAME, patientName.getText().toString());
	               		   intent.putExtra(PatientsAlarmsListActivity.FILTER_LAST_NAME, patientLastName.getText().toString());
	               		   Log.d(App.DEBUG_TAG, "start service");
	               		   startActivity(intent);
	                   }
	               })
	               .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                	   SearchPatientDialogFragment.this.getDialog().cancel();

	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	
}

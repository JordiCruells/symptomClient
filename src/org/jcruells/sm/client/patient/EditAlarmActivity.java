package org.jcruells.sm.client.patient;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.Alarm;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EditAlarmActivity extends FragmentActivity  {

	
	private int action;
	
	public static final String ALARM = "alarm";
	public static final String ACTION = "action";
	public static final int ACTION_INSERT = 1;
	public static final int  ACTION_UPDATE = 2;
	
	
	@InjectView(R.id.alarmActivate)
	protected CheckBox alarmActivate;
	
	@InjectView(R.id.alarmTime)
	protected TextView alarmTime;
	
	@InjectView(R.id.alarmVibrate)
	protected CheckBox alarmVibrate;
	
	
	@InjectView(R.id.btnSave)
	protected Button btnSave;
	
	@InjectView(R.id.btnCancel)
	protected Button btnCancel;
	
	@InjectView(R.id.btnDelete)
	protected Button btnDelete;
	

	private Alarm alarm;
	private int recordId;
	private App app;
	private String initialTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.d(App.DEBUG_TAG, "on create checin activity");
		
		super.onCreate(savedInstanceState);		
	
		app = ((App)getApplicationContext());
		
		recordId = app.getUser().getRecordNumber();
		
		setContentView(R.layout.edit_alarm_activity);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		Log.d(App.DEBUG_TAG, "get extras");
		
		Bundle extras = getIntent().getExtras();
			    
		action = getIntent().getExtras().getInt(ACTION);
			
		if (action == ACTION_UPDATE) {
			alarm = (Alarm) extras.getSerializable(ALARM);
			initialTime = alarm.getAlarmTime();
		} else {
			btnDelete.setEnabled(false);
			alarm = new Alarm(0, recordId, "", Alarm.ALARM_ACTIVATED, Alarm.ALARM_VIBRATE);
			selectTime();
		}
			
		alarmActivate.setChecked(alarm.getActivated() > 0);
		alarmVibrate.setChecked(alarm.getVibrate() > 0);
		alarmTime.setText(alarm.getAlarmTime());
		
		Log.d(App.DEBUG_TAG, "exit oncreate");		
		
	}	
	
	@OnClick(R.id.btnSave)
	public void saveAlarm() {
		
		Log.d(App.DEBUG_TAG,"SAVE alarm");
		
		// Insert the new check-in
		ContentValues values = new ContentValues();
		
		String time = alarmTime.getText().toString();
		int vibrate = alarmVibrate.isChecked() ? Alarm.ALARM_VIBRATE : Alarm.ALARM_NO_VIBRATE;
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID , recordId );
		values.put(SymptomDataContract.ALARM_TIME , time);
		values.put(SymptomDataContract.ALARM_ACTIVATED , alarmActivate.isChecked() ? Alarm.ALARM_ACTIVATED : Alarm.ALARM_DEACTIVATED);
		values.put(SymptomDataContract.ALARM_VIBRATE, vibrate);
		
		Log.d(App.DEBUG_TAG, "VALUES: " + values);
		
		Uri alarmUri = Uri.withAppendedPath(SymptomDataContract.ALARMS_URI, String.valueOf(recordId));
		
		Log.d(App.DEBUG_TAG,"ALARM URI IS: " + alarmUri.toString());
		
		switch (action) {
			case ACTION_INSERT:
				Log.d(App.DEBUG_TAG,"EXECUTE INSERT ALARM");
				getContentResolver().insert(alarmUri, values);
				Log.d(App.DEBUG_TAG, "add alarm " + time + " - " + vibrate);
				AlarmsHelper.addAlarm(time, vibrate);
				break;
			case ACTION_UPDATE:
				Log.d(App.DEBUG_TAG,"EXECUTE UPDATE ALARM");
				String selection = SymptomDataContract.ID + " = ?";
				String[] selectionArgs = {String.valueOf(alarm.getId())};
				
				Log.d(App.DEBUG_TAG, "selection: " + selection);
				Log.d(App.DEBUG_TAG, "selectionArgs: " + selectionArgs[0]);
				getContentResolver().update(alarmUri, values, selection, selectionArgs);
				
				Log.d(App.DEBUG_TAG, "update alarm " + initialTime + " - " + time + " - " + vibrate);
				AlarmsHelper.updateAlarm(initialTime, time, vibrate);
				break;
			default:
				break;
		}
		
		finish();
	}
	
	
	@OnClick(R.id.btnDelete)
	public void deleteAlarm() {
		
		Log.d(App.DEBUG_TAG,"delete alarm");
		String selection = SymptomDataContract.ID + " = ?";
		String[] selectionArgs = {String.valueOf(alarm.getId())};
		Uri alarmUri = Uri.withAppendedPath(SymptomDataContract.ALARMS_URI, String.valueOf(recordId));
		getContentResolver().delete(alarmUri, selection, selectionArgs);
		Log.d(App.DEBUG_TAG, "delete alarm " + initialTime);
		AlarmsHelper.deleteAlarm(initialTime);
		finish();
		
	}
	
	@OnClick(R.id.btnCancel)
	public void changeListMedicationAnswers() {
		finish();
	}
	
	
	@OnClick(R.id.alarmTime)
	public void selectTime() {
		Log.d(App.DEBUG_TAG, " select time");
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	
	public void setTime(String time) {
		Log.d(App.DEBUG_TAG, " set time to " + time);
		alarm.setAlarmTime(time);
		alarmTime.setText(time);
	}

	
	/*private void deleteMedication() {

		CallableTask.invoke(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
								
				String selection = SymptomDataContract.PATIENT_MEDICATION_ID + " = ?";
				String[] arguments = {itemId.toString()};
				
				Log.d(App.DEBUG_TAG, "delete medication: " + itemId);
				
				String appendedUri = String.valueOf(recordId);
				Uri uriPatientMedications = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, appendedUri);
				
				return getContentResolver().delete(uriPatientMedications, selection, arguments);
				
			}
		}, 
		new TaskCallback<Integer>() {
			@Override
			public void success(Integer result) {
				// Fetch the cursor and for
				Log.d(App.DEBUG_TAG, "delete successful ");		
				
			}
			
			@Override
			public void error(Exception e) {
				Log.d(App.DEBUG_TAG, "Error while getting list of medications");
			}
		});
	}
	*/
	
	/*
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d(App.DEBUG_TAG, "on activity result in");
		Log.d(App.DEBUG_TAG, "resultCode: " + resultCode);
		
		
		if (resultCode == RESULT_OK) {
			
			Bundle extras = data.getExtras();
			switch(requestCode) {
				case MEDICATION_ACTIVITY:	
					medicationTaken = extras.getInt("medicationTaken", medicationTaken);
					alarmTime = extras.getString("time", alarmTime);
					date = extras.getString("date", date);
					formatedDateTime = extras.getString("formatedDateTime", formatedDateTime);
					Log.d(App.DEBUG_TAG, "on activity result " + medicationTaken + "-" + alarmTime + "-" + date);
					
					setTextPainMedication(medicationTaken, formatedDateTime);
					
					break;
				
				default:
			
			}
		}
		
	}*/
	
	

}

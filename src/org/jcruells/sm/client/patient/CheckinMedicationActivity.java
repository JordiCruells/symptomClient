package org.jcruells.sm.client.patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;

import android.content.Intent;
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

public class CheckinMedicationActivity extends FragmentActivity  {
	
	
	@InjectView(R.id.checkMedication)
	protected CheckBox checkMedication;
	
	@InjectView(R.id.medicationTime)
	protected TextView medicationTime;
	
	@InjectView(R.id.selectDate)
	protected Button selectDate;
	
	@InjectView(R.id.selectTime)
	protected Button selectTime;
	
	@InjectView(R.id.btnAcceptMedicationInfo)
	protected Button btnAcceptMedicationInfo;
	
	@InjectView(R.id.btnCancelMedicationInfo)
	protected Button btnCancelMedicationInfo;
	
	private String date;
	private String time;
	private int medicationTaken = 1;
	private String formatedDateTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		
		Log.d(App.DEBUG_TAG, "CheckInActivity onCreate");
		
		setContentView(R.layout.checkin_medication_activity);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
		    date = extras.getString("date");
		    time = extras.getString("time");
		    medicationTaken = extras.getInt("medicationTaken");
		}
		
		final Date d = new Date();
		final String stringTime = new SimpleDateFormat(" HH:mm").format(d);
		time = stringTime + ":00";
		date = new SimpleDateFormat("yyyy-MM-dd").format(d);
		final DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		final String stringDate =  df.format(d); 
		
		Log.d(App.DEBUG_TAG, "stringTime " + stringTime + " stringDate " + stringDate);
		formatedDateTime = new StringBuilder(stringDate).append(stringTime).toString();
		medicationTime.setText(formatedDateTime);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	@OnClick(R.id.selectTime)
	public void selectTime(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	@OnClick(R.id.selectDate)
	public void selectDate(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	@OnClick(R.id.btnAcceptMedicationInfo)
	public void acceptMedicationInfo(View v) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("date",date);
		returnIntent.putExtra("time",time);
		returnIntent.putExtra("medicationTaken",medicationTaken);
		returnIntent.putExtra("formatedDateTime",formatedDateTime);
		setResult(RESULT_OK,returnIntent);
		finish();	
	}
	
	@OnClick(R.id.btnCancelMedicationInfo)
	public void cancelMedicationInfo(View v) {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	
	}
	
	@OnClick(R.id.checkMedication)
	public void checkMedicationInfo(View v) {
		if (checkMedication.isChecked())
			medicationTaken = 1;
		else
			medicationTaken = 0;
		
		Log.d(App.DEBUG_TAG, "check medication set to " + medicationTaken);
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setFormatedDateTime(String formatedDateTime) {
		this.formatedDateTime = formatedDateTime;
		medicationTime.setText(formatedDateTime);
	}
	
	public String getFormatedDateTime() {
		return formatedDateTime;
	}
	

}

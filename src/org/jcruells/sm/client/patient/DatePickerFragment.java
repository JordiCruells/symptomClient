package org.jcruells.sm.client.patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jcruells.sm.client.App;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	private String date;
	private static String SPACE = " ";
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
		
		CheckinMedicationActivity act = (CheckinMedicationActivity) getActivity();
		
		final Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		Date d = c.getTime();
		
		String originalDateTimeString = act.getFormatedDateTime();
		String originalTimeString =  originalDateTimeString.substring(originalDateTimeString.lastIndexOf(SPACE));
		Log.d(App.DEBUG_TAG, "original time is " + originalTimeString);
		
		final String finalDay = df.format(d); 
		String newTime = new StringBuilder(finalDay).append(SPACE).append(originalTimeString).toString();
		
		date = new SimpleDateFormat("yyyy-MM-dd").format(d);
		
		act.setDate(date);
		act.setFormatedDateTime(newTime);
		
		Log.d(App.DEBUG_TAG, "date is set to " + date);
	}
	
	
	
	
	
}
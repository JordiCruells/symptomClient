package org.jcruells.sm.client.patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jcruells.sm.client.App;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {

	private String time;
	private static String SPACE = " ";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
		//DateFormat.is24HourFormat(getActivity())
		true
		);
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		Log.d(App.DEBUG_TAG, "hour is : " + hourOfDay + " minute is: " + minute);
		
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		
		Date d = c.getTime();
		
		Activity caller = getActivity();
		
		if (caller instanceof CheckinMedicationActivity) {
		
			CheckinMedicationActivity act = (CheckinMedicationActivity) caller;
			
			time = new SimpleDateFormat("HH:mm:ss").format(d);
			
			String originalDateTimeString =act.getFormatedDateTime();
			String originalDateString =  originalDateTimeString.substring(0, originalDateTimeString.lastIndexOf(SPACE));
			
			
			act.setTime(time);
			act.setFormatedDateTime(originalDateString + SPACE + time.substring(0, 5));
			
			Log.d(App.DEBUG_TAG, "time is set to " + time);
		}
		
		if (caller instanceof EditAlarmActivity) {
			
			EditAlarmActivity act = (EditAlarmActivity) caller;
			time = new SimpleDateFormat("HH:mm").format(d);
			act.setTime(time);
			Log.d(App.DEBUG_TAG, "time is set to " + time);
		}
		
		
	}

	

}
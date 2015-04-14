package org.jcruells.sm.client.doctor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.Patient;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PatientsAlarmsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;
	private static Resources resources;
	private static String LAST_CHECKIN;
	private static String SEVERE_PAIN_TEXT;
	private static String MODERATE_TO_SEVERE_PAIN_TEXT;
	private static String NO_EATING_TEXT;

	public PatientsAlarmsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of PatientsAlarmsCursorAdapter");
		inflater = LayoutInflater.from(context);
		if (resources == null) { 
			resources = context.getResources();
			LAST_CHECKIN = resources.getString(R.string.last_checkin_at);
			SEVERE_PAIN_TEXT = resources.getString(R.string.severe_pain_text);
			MODERATE_TO_SEVERE_PAIN_TEXT = resources.getString(R.string.moderate_to_severe_pain_text);
			NO_EATING_TEXT = resources.getString(R.string.no_eating_text);
		}
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in PatientsAlarmsCursorAdapter");
		View v = inflater.inflate(R.layout.patient_alarm_list_item, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		Log.d(App.DEBUG_TAG, "bindView in PatientsAlarmsCursorAdapter");
		// TODO Auto-generated method stub
		final String strPatientName = cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_NAME));
		final String strPatientLastName = cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_LAST_NAME));
		final String strLastCheckinTime = cursor.getString(cursor.getColumnIndex(SymptomDataContract.LAST_CHECKIN_DATETIME));
		final int severePainMinutes = cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SEVERE_PAIN_MINUTES));
		final int moderateToSeverePainMinutes = cursor.getInt(cursor.getColumnIndex(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES));
		final int noEatMinutes = cursor.getInt(cursor.getColumnIndex(SymptomDataContract.NO_EAT_MINUTES));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		
		String textLastCheckinDate = "";
		try { 
			textLastCheckinDate = df.format(sdf.parse(strLastCheckinTime));
		} catch(ParseException e) 
		{
			Log.d(App.DEBUG_TAG, "Problem when parsing strLastCheckinTime " + strLastCheckinTime);
		}
		
		String textPatient = strPatientName + " " + strPatientLastName + " - " + LAST_CHECKIN + textLastCheckinDate;
		Log.d(App.DEBUG_TAG, "text patient: " + textPatient);
		
		String textAlarm = "";
		
		if (severePainMinutes > App.MINUTES_ALARM_SEVERE_PAIN) {
			textAlarm += String.format(SEVERE_PAIN_TEXT, severePainMinutes/60 );
		} else {
			if (moderateToSeverePainMinutes > App.MINUTES_ALARM_MODERATE_TO_SEVERE_PAIN) {
				textAlarm += String.format(SEVERE_PAIN_TEXT, moderateToSeverePainMinutes/60 );
			}
		}
		if (noEatMinutes > App.MINUTES_ALARM_NO_EAT) {
			textAlarm += String.format(NO_EATING_TEXT, noEatMinutes/60 );
		}
		
		Log.d(App.DEBUG_TAG, "text patient: " + textAlarm);
		
		((TextView) view.findViewById(R.id.textPatient)).setText(textPatient);
		((TextView) view.findViewById(R.id.textAlarm)).setText(textAlarm);
		
		
		
	}
	
	public Patient get(int position) {
	    
		Log.d(App.DEBUG_TAG, "Patient GET : " + position);
		Cursor cursor = getCursor();
		Patient p = null;
		if (cursor.moveToPosition(position)) {
			p = new Patient(cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)),
						 	cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)), 
						 	cursor.getLong(cursor.getColumnIndex(SymptomDataContract.DOCTOR_ID)),
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_NAME)),
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_LAST_NAME)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_BIRTHDAY)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SEVERE_PAIN_MINUTES)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.NO_EAT_MINUTES)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PAIN_LEVEL)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.STOPPED_EATING_LEVEL)),
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.LAST_CHECKIN_DATETIME)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP)), 
							cursor.getString(cursor.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
							cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNCED))); 
		}

		Log.d(App.DEBUG_TAG, "exit Patient GET ");
		return p;
	}

	

}


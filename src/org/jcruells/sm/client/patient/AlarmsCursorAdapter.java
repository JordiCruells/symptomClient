package org.jcruells.sm.client.patient;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.Alarm;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AlarmsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;

	public AlarmsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of MedicationQuestionsCursorAdapter");
		inflater = LayoutInflater.from(context);
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in AlarmsCursorAdapter");
		View v = inflater.inflate(R.layout.alarm_list_item, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(App.DEBUG_TAG, "bindView in CheckinsCursorAdapter");
		// TODO Auto-generated method stub
		
		String time = cursor.getString(cursor.getColumnIndex(SymptomDataContract.ALARM_TIME));
		boolean activated = cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ALARM_ACTIVATED)) > 0;
		
		((TextView) view.findViewById(R.id.alarmTime)).setText(time);
		((CheckBox) view.findViewById(R.id.alarmActivated)).setChecked(activated);
		
	}
	
	@Override
	public long getItemId(int position) {
	    Cursor cursor = getCursor();
	    cursor.moveToPosition(position);
	    Log.d(App.DEBUG_TAG, "getItemID");
	    Log.d(App.DEBUG_TAG, "position: " + position);
	    Log.d(App.DEBUG_TAG, "return " + cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)));
	    return cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID));
	}
	
	
	public Alarm getAlarmItem(int position) {
		
		 Log.d(App.DEBUG_TAG, "alar mget item");
		 Cursor cursor = getCursor();
		 cursor.moveToPosition(position);
		 return new Alarm(
				(int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)),
				(int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)),
				cursor.getString(cursor.getColumnIndex(SymptomDataContract.ALARM_TIME)),
				(int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ALARM_ACTIVATED)),
				(int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ALARM_VIBRATE))
				 );
				
	}
	

}

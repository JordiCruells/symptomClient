package org.jcruells.sm.client.patient;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.CheckIn;
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

public class CheckinsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;
	
	private String[] painLevels;
	private String pain;

	public CheckinsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of CheckinsCursorAdapter");
		inflater = LayoutInflater.from(context);
		Resources r = context.getResources();
		painLevels = r.getStringArray(R.array.answers_pain_level);
		pain = r.getString(R.string.pain_level) + ": ";
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in CheckinsCursorAdapter");
		View v = inflater.inflate(R.layout.checkin_list_item, parent, false);
		//String strDate = cursor.getString(cursor.getPosition());
		//((TextView) v.findViewById(R.id.date)).setText(strDate);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(App.DEBUG_TAG, "bindView in CheckinsCursorAdapter");
		// TODO Auto-generated method stub
		String strDate = cursor.getString(cursor.getColumnIndex(SymptomDataContract.CHECK_IN_DATETIME));
		int painLevel = cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PAIN_LEVEL));
		Log.d(App.DEBUG_TAG, "bindView set text: " + strDate);
		
		((TextView) view.findViewById(R.id.date)).setText(strDate);
		((TextView) view.findViewById(R.id.painLevel)).setText(pain + painLevels[painLevel]);
		
	}
	
	public CheckIn get(int position) {
	    
		Log.d(App.DEBUG_TAG, "CheckIn GET : " + position);
		Cursor cursor = getCursor();
		CheckIn c = null;
		if (cursor.moveToPosition(position)) {
			c = new CheckIn(					
					(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)),					
				 	(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)),				 	
					cursor.getString(cursor.getColumnIndex(SymptomDataContract.CHECK_IN_DATETIME)),
					(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.PAIN_LEVEL)),		
					(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.MEDICATION_TAKEN)),
					cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_DATETIME)),
					cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_ANSWERS)),
					(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.STOPPED_EATING_LEVEL)),
					cursor.getString(cursor.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
					(int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
					(int)cursor.getLong(cursor.getColumnIndex(SymptomDataContract.SYNCED)));
		}
	
        		Log.d(App.DEBUG_TAG, "exit Patient GET ");
		return c;
	}

}

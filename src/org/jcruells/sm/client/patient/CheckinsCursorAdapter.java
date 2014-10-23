package org.jcruells.sm.client.patient;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.DoctorDatabaseOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CheckinsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;

	public CheckinsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of CheckinsCursorAdapter");
		inflater = LayoutInflater.from(context);
		
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
		String strDate = cursor.getString(cursor.getColumnIndex(DoctorDatabaseOpenHelper.CHECK_IN_DATETIME));
		((TextView) view.findViewById(R.id.date)).setText(strDate);
		
	}

	

}

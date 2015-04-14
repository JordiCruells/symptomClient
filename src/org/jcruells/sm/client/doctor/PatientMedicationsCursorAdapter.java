package org.jcruells.sm.client.doctor;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
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

public class PatientMedicationsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;
	private String fromDate;

	public PatientMedicationsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of MedicationQuestionsCursorAdapter");
		inflater = LayoutInflater.from(context);
		fromDate = context.getResources().getString(R.string.from_date);
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in MedicationQuestionsCursorAdapter");
		View v = inflater.inflate(R.layout.patient_medication_list_item, parent, false);
		//String strDate = cursor.getString(cursor.getPosition());
		//((TextView) v.findViewById(R.id.date)).setText(strDate);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(App.DEBUG_TAG, "bindView in CheckinsCursorAdapter");
		// TODO Auto-generated method stub
		String medication = cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME));
		String medicationFrom = cursor.getString(cursor.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_FROM));
		
		Log.d(App.DEBUG_TAG, "bindView set MEDICATION NAME: " + medication);
		((TextView) view.findViewById(R.id.medicationName)).setText(medication);
		((TextView) view.findViewById(R.id.medicationFrom)).setText(String.format(fromDate, medicationFrom));
		
	}
	
	@Override
	public long getItemId(int position) {
	    Cursor cursor = getCursor();
	    cursor.moveToPosition(position);
	    Log.d(App.DEBUG_TAG, "getItemID");
	    Log.d(App.DEBUG_TAG, "position: " + position);
	    Log.d(App.DEBUG_TAG, "return " + cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)));
	    return cursor.getLong(cursor.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_ID));
	}
	
	public String getMedicationName(int position) {
	    Cursor cursor = getCursor();
	    cursor.moveToPosition(position);
	    Log.d(App.DEBUG_TAG, "return " + cursor.getLong(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME)));
	    return cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME));
	}

	

}

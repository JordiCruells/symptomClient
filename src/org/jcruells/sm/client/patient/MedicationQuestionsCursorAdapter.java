package org.jcruells.sm.client.patient;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MedicationQuestionsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;
	private String questionTemplate;

	public MedicationQuestionsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of MedicationQuestionsCursorAdapter");
		inflater = LayoutInflater.from(context);
		questionTemplate = context.getResources().getString(R.string.question_template_medication);
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in MedicationQuestionsCursorAdapter");
		View v = inflater.inflate(R.layout.medication_question_list_item, parent, false);
		//String strDate = cursor.getString(cursor.getPosition());
		//((TextView) v.findViewById(R.id.date)).setText(strDate);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(App.DEBUG_TAG, "bindView in CheckinsCursorAdapter");
		// TODO Auto-generated method stub
		String medication = cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME));
		
		Log.d(App.DEBUG_TAG, "bindView set MEDICATION NAME: " + medication);
		((TextView) view.findViewById(R.id.medicationQuestion)).setText(String.format(questionTemplate, medication));
		
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

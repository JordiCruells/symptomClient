package org.jcruells.sm.client.doctor;



import java.util.HashSet;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
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

public class MedicationsCursorAdapter extends CursorAdapter {
	
	private  LayoutInflater inflater;
	private String questionTemplate;
	private HashSet<Integer> checkedMedications = new HashSet<Integer>();

	
	public void setCheckedMedications(HashSet<Integer> checkedMedications) {
		this.checkedMedications = checkedMedications;	
		
		Log.d(App.DEBUG_TAG, "****** checked medications: " + checkedMedications);
	}
	
	public MedicationsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		Log.d(App.DEBUG_TAG, "constructor of MedicationQuestionsCursorAdapter");
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "newView in MedicationQuestionsCursorAdapter");
		View v = inflater.inflate(R.layout.change_medications_list_item, parent, false);
		//String strDate = cursor.getString(cursor.getPosition());
		//((TextView) v.findViewById(R.id.date)).setText(strDate);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(App.DEBUG_TAG, "bindView in CheckinsCursorAdapter");
		// TODO Auto-generated method stub
		String medication = cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME));
		Integer id = (int) cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID));
		Log.d(App.DEBUG_TAG, "bindView set MEDICATION NAME: " + medication);
		((TextView) view.findViewById(R.id.medication)).setText(medication);
		
		Log.d(App.DEBUG_TAG, "******* id: " + id);
		
		
		
		if (checkedMedications.contains(id)) {
			((CheckBox) view.findViewById(R.id.medicationSelected)).setChecked(true);
			Log.d(App.DEBUG_TAG, "******* contains id: " + id);
		} else {
			((CheckBox) view.findViewById(R.id.medicationSelected)).setChecked(false);
			Log.d(App.DEBUG_TAG, "******* does not contains id: " + id);
		}
		
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
	
	public String getMedicationName(int position) {
	    Cursor cursor = getCursor();
	    cursor.moveToPosition(position);
	    Log.d(App.DEBUG_TAG, "return " + cursor.getLong(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME)));
	    return cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_NAME));
	}



}


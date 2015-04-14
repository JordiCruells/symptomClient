package org.jcruells.sm.client.patient;


import java.util.ArrayList;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;



public class CheckinMedicationListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	@InjectView(R.id.listMedicationQuestions)
	protected ListView listMedicationQuestions;
	
	private MedicationQuestionsCursorAdapter mAdapter;
	
	private SparseArray<String> medicationAnswers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		
		Log.d(App.DEBUG_TAG, "CheckinMedicationListActivity onCreate");
		
		setContentView(R.layout.checkin_medications_list_activity);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		medicationAnswers = new SparseArray<String>();
		
		mAdapter = new MedicationQuestionsCursorAdapter(this, null, 0);
		
		listMedicationQuestions.setAdapter(mAdapter);
		listMedicationQuestions.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				    
				   Log.d(App.DEBUG_TAG, "on item click");
				   
				   final CheckBox cb = (CheckBox) view.findViewById(R.id.medicationAnswer);
				   final int itemId = (int)(mAdapter.getItemId(position));
				   
				   if (medicationAnswers.get(itemId) == null) { 
					   	medicationAnswers.put(itemId, mAdapter.getMedicationName(position));
					   	Log.d(App.DEBUG_TAG, "put " + itemId + " - " + mAdapter.getMedicationName(position));
				   	   	cb.setChecked(true);
				   } else {
					   	medicationAnswers.remove(itemId);
					   	Log.d(App.DEBUG_TAG, "remove " + itemId);
				   		cb.setChecked(false);
				   }
				   
				   Log.d(App.DEBUG_TAG, "medicationAnswers.size " + medicationAnswers.size());
			   } 
		});
		
		
		
		
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);
	}
	
	@OnClick(R.id.acceptListMedication)
	public void accept() {
		
		Log.d(App.DEBUG_TAG, "in acceptListMedication");
		
		final Resources r = getResources();
		final String listDelimiter = r.getString(R.string.list_delimiter);
		final String endListDelimiter = r.getString(R.string.end_list_delimiter);
		
		int size = medicationAnswers.size();
		
		Log.d(App.DEBUG_TAG, "size " + size);
		
		ArrayList<Integer> listAnswers = new ArrayList<Integer>();
		StringBuilder textAnswers = new StringBuilder();
		for(int i = 0; i < size; i++) {
			Log.d(App.DEBUG_TAG, "i: " + i);
			Log.d(App.DEBUG_TAG, "medicationAnswers.keyAt(i): " + medicationAnswers.keyAt(i));
			listAnswers.add(medicationAnswers.keyAt(i));
			if (i > 0) {
				if (i == size - 1) {
					textAnswers.append(endListDelimiter);
				} else {
					textAnswers.append(listDelimiter);
				}
					
			}
			Log.d(App.DEBUG_TAG, "append text: " + medicationAnswers.valueAt(i));
			textAnswers.append(medicationAnswers.valueAt(i));
		}
		
		Intent returnIntent = new Intent();		
		returnIntent.putExtra("medicationAnswers",listAnswers);
		returnIntent.putExtra("textMedicationAnswers",textAnswers.toString());
		Log.d(App.DEBUG_TAG, "acceptListMedication");
		Log.d(App.DEBUG_TAG, "textMedicationAnswers " + textAnswers.toString());
		Log.d(App.DEBUG_TAG, "listAnswers " + listAnswers);
		setResult(RESULT_OK,returnIntent);
		finish();
	}
	
	@OnClick(R.id.cancelListMedication)
	public void cancel() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		Log.d(App.DEBUG_TAG, "checkin medication on create loader<cursor>");
		
		App app = (App) getApplicationContext();
		String appendedUri = app.getUser().getRecordNumber().toString();
		
		Uri uriPatientMedications = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, appendedUri);
		
		Log.d(App.DEBUG_TAG, "Uri is :" + uriPatientMedications.toString());
	
		return new CursorLoader(this, uriPatientMedications , 
				SymptomDataContract.PATIENT_MEDICATION_WITH_NAME_ALL_COLUMNS,
				null, null,SymptomDataContract.MEDICATION_NAME + " ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "data is " + data);
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}

}

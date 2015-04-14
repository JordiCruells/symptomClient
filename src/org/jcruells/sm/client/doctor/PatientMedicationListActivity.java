package org.jcruells.sm.client.doctor;


import java.util.ArrayList;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;



public class PatientMedicationListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	public static final String RECORD_ID = "recordId";
	public static final String NAME = "name";
	public static final String ACTION = "action";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_VIEW = "view";
	
	@InjectView(R.id.listMedications)
	protected ListView listMedications;
	
	//@InjectView(R.id.btnAddRemove)
	protected Button btnAddRemove;
	
	@InjectView(R.id.btnClose)
	protected Button btnClose;
	
	private PatientMedicationsCursorAdapter mAdapter;
	
	//private SparseArray<String> medicationAnswers;
	
	private int recordId;
	private String name;
	private boolean editionEnabled;
	private ArrayList<Integer> medications = new ArrayList<Integer>();
	
	
	private int CHANGE_MEDICATION_ACTIVITY = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		
		Log.d(App.DEBUG_TAG, "Chanmge MedicationListActivity onCreate");
		
		Intent i = getIntent();
		
		final ContentValues savedValues = (ContentValues) getLastNonConfigurationInstance();
	    if (savedValues == null) {
	    	if (i.hasExtra(RECORD_ID)) {			
				recordId = i.getIntExtra(RECORD_ID, 0);
				Log.d(App.DEBUG_TAG, "getting record id form extra " + recordId);
			} else {
				recordId = ((App) getApplicationContext()).getUser().getRecordNumber();
			}
			if (i.hasExtra(NAME)) {
				
				name = i.getStringExtra(NAME);
				Log.d(App.DEBUG_TAG, "getting NAME form extra " + name);
			}
			if (i.hasExtra(ACTION)) {
				Log.d(App.DEBUG_TAG, "has extra action ");
				Log.d(App.DEBUG_TAG, "i.getStringExtra(ACTION " + i.getStringExtra(ACTION));
				editionEnabled =(i.getStringExtra(ACTION)).equals(ACTION_EDIT);				
			}
	    } else {
	    	Log.d(App.DEBUG_TAG, "recreate values");
	        recordId = savedValues.getAsInteger(RECORD_ID);
	        name = savedValues.getAsString(NAME);
	        editionEnabled = savedValues.getAsBoolean("editionenabled");
	    }
	 	
	    LinearLayout layout = (LinearLayout) View.inflate(this,R.layout.patient_medications_list_activity, null);
	    if (editionEnabled) {
			Log.d(App.DEBUG_TAG, "Edition is enabled");
			//btnAddRemove.setEnabled(true);
			
			LinearLayout buttons = (LinearLayout) layout.findViewById(R.id.listMedicationsButtons);
			Log.d(App.DEBUG_TAG, "buttons" + buttons);
			
			btnAddRemove = new Button(this);
			
			Log.d(App.DEBUG_TAG, "btnAddRemove" + btnAddRemove);
			
			btnAddRemove.setText(getResources().getString(R.string.action_add_remove));
			btnAddRemove.setLayoutParams(new LinearLayout.LayoutParams(
		            						 	ViewGroup.LayoutParams.MATCH_PARENT,
		            						 	ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
			
			Log.d(App.DEBUG_TAG, "add view ");
			buttons.addView(btnAddRemove, 0); 
			
			Log.d(App.DEBUG_TAG, "add view after");
			
			btnAddRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(App.DEBUG_TAG, "in addRemove Medication");
					
					Intent i = new Intent(PatientMedicationListActivity.this, ChangeMedicationsActivity.class);		
					i.putExtra(ChangeMedicationsActivity.RECORD_ID,recordId);
					i.putExtra(ChangeMedicationsActivity.NAME,name);	
					i.putIntegerArrayListExtra(ChangeMedicationsActivity.MEDICATIONS, medications);
					startActivityForResult(i, CHANGE_MEDICATION_ACTIVITY);
				}
				
			});
			
			Log.d(App.DEBUG_TAG, "add view after set clicl listener");
		}
	    

		//setContentView(R.layout.patient_medications_list_activity);
	    setContentView(layout);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		
	    /*else {
			Log.d(App.DEBUG_TAG, "Edition is not enabled");
			btnAddRemove.setEnabled(false);
		}*/
	    
		if (name != null && name.length() > 0) {
			String title = getResources().getString(R.string.title_activity_patient_medication);
			setTitle(String.format(title, name));
		}
		
		
		//medicationAnswers = new SparseArray<String>();
		
		mAdapter = new PatientMedicationsCursorAdapter(this, null, 0);
		
		listMedications.setAdapter(mAdapter);
		
	
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    //this is called by the framework when needed
	    //Just return what you want to save here.
		ContentValues values = new ContentValues();
		values.put("editionEnabled", editionEnabled);
		values.put(RECORD_ID, recordId);
		values.put(NAME, name);
	    return new ContentValues();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onResume");
		refreshCursor();
		/*if (editionEnabled) {
			Log.d(App.DEBUG_TAG, "Edition is enabled on resume");
			btnAddRemove.setEnabled(true);
		} else {
			Log.d(App.DEBUG_TAG, "Edition is not enabled on resume");
			btnAddRemove.setEnabled(false);
		}*/
	}
	
	private void refreshCursor() {
		Log.d(App.DEBUG_TAG, "refresh cursor");	
		getLoaderManager().restartLoader(0, null, this);		
	}
	
	
	/*@OnClick(R.id.btnAddRemove)
	public void addRemove() {
		
		Log.d(App.DEBUG_TAG, "in addRemove Medication");
		
		Intent i = new Intent(PatientMedicationListActivity.this, ChangeMedicationsActivity.class);		
		i.putExtra(ChangeMedicationsActivity.RECORD_ID,recordId);
		i.putExtra(ChangeMedicationsActivity.NAME,name);	
		i.putIntegerArrayListExtra(ChangeMedicationsActivity.MEDICATIONS, medications);
		startActivityForResult(i, CHANGE_MEDICATION_ACTIVITY);
		
	}*/
	
	@OnClick(R.id.btnClose)
	public void close() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		Log.d(App.DEBUG_TAG, "patient medication on create loader<cursor>");
		
		String appendedUri = String.valueOf(recordId);
		
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
				
		medications.clear();
		
		//Populate medications set
		if (data.moveToFirst()) {
			do {
				medications.add(data.getInt(data.getColumnIndex(SymptomDataContract.PATIENT_MEDICATION_ID)));
			} while (data.moveToNext());
		}
		
		//btnAddRemove.setEnabled(true);
		
		Log.d(App.DEBUG_TAG, "added " + medications.size() + " to set of medications");
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}

}

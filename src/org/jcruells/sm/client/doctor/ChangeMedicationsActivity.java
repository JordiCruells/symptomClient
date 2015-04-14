package org.jcruells.sm.client.doctor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Callable;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.CallableTask;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.TaskCallback;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChangeMedicationsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	
	public static final String RECORD_ID = "record_id";
	public static final String NAME = "name";
	public static final String MEDICATIONS = "medications";
	
	private final String MAX_DATE = "9999-12-31";

	@InjectView(R.id.listAllMedications)
	protected ListView listAllMedications;
	
	@InjectView(R.id.btnAccept)
	protected Button btnAccept;
	
	private HashSet<Integer> medications;
	private String name;
	private int recordId;
	private boolean changesPerformed = false;
	private Integer itemId;
	private String date;
	
	
	private MedicationsCursorAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		
		Log.d(App.DEBUG_TAG, "Cheange MedicationListActivity onCreate");
		
		date = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		
		setContentView(R.layout.change_medications_activity);
		
		Intent i = getIntent();
		
		if (i.hasExtra(RECORD_ID)) {
			Log.d(App.DEBUG_TAG, "getting record id form extra " + ((App) getApplicationContext()).getUser().getRecordNumber());
			recordId = i.getIntExtra(RECORD_ID, 0);
		} else {
			recordId = ((App) getApplicationContext()).getUser().getRecordNumber();
		}
		if (i.hasExtra(NAME)) {
			Log.d(App.DEBUG_TAG, "getting NAME form extra " );
			name = i.getStringExtra(NAME);
		}

		if (i.hasExtra(MEDICATIONS)) {
			Log.d(App.DEBUG_TAG, "getting medications form extra ");
			medications = new HashSet<Integer>(i.getIntegerArrayListExtra(MEDICATIONS));
		}

		
		String title = getResources().getString(R.string.title_activity_patient_medication);
		setTitle(String.format(title, name));
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		mAdapter = new MedicationsCursorAdapter(this, null, 0);
		mAdapter.setCheckedMedications(medications);
		
		listAllMedications.setAdapter(mAdapter);
		
		listAllMedications.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				    
				   Log.d(App.DEBUG_TAG, "on item click");
				   
				   final CheckBox cb = (CheckBox) view.findViewById(R.id.medicationSelected);
				   itemId = (int)(mAdapter.getItemId(position));
				   
				   if (!medications.contains(itemId)) { 
					   	medications.add(itemId);
					   	Log.d(App.DEBUG_TAG, "put " + itemId);
					   	
					   	//Add new medication in the database for this patient
					   	addMedication();
				   	   	cb.setChecked(true);
				   	   	changesPerformed = true;
				   	   	
				   } else {
					   	medications.remove(itemId);
					   	Log.d(App.DEBUG_TAG, "remove " + itemId);
					   	
					   	// Remove medication in the database for this patient
					   	deleteMedication();
				   		cb.setChecked(false);
				   		changesPerformed = true;
				   }
				   
				   btnAccept.setEnabled(true);
				   
				   Log.d(App.DEBUG_TAG, "medicationAnswers.size " + medications.size());
			   } 
		});
		
		
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);
	}
	
	@OnClick(R.id.btnAccept)
	public void accept() {
		
		Log.d(App.DEBUG_TAG, "in acceptListMedication");
		
		Intent returnIntent = new Intent();
		if (changesPerformed)
			setResult(RESULT_OK, returnIntent);
		else 
			setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		Log.d(App.DEBUG_TAG, "medications on create loader<cursor>");
		
		return new CursorLoader(this, SymptomDataContract.MEDICATIONS_URI , 
				SymptomDataContract.MEDICATION_ALL_COLUMNS,
				null, null,SymptomDataContract.MEDICATION_NAME + " ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "load finished data is " + data);
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		//mAdapter.swapCursor(null);
	}

	
	private void check(int pos) {
		Log.d(App.DEBUG_TAG, "check the checkbox at " + pos);
		final int firstListItemPosition = listAllMedications.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listAllMedications.getChildCount() - 1;
		final View view;
		
		Log.d(App.DEBUG_TAG, "firstListItemPosition " + firstListItemPosition);
		Log.d(App.DEBUG_TAG, "lastListItemPosition " + lastListItemPosition);
		
		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			Log.d(App.DEBUG_TAG, "get view");
		    view =  mAdapter.getView(pos, null, listAllMedications);
		} else {
		    final int childIndex = pos - firstListItemPosition;
		    Log.d(App.DEBUG_TAG, "view getChildAt");
		    view = listAllMedications.getChildAt(childIndex);
		}
		
		Log.d(App.DEBUG_TAG, "view " + view.toString());
		CheckBox cb =  (CheckBox) (view.findViewById(R.id.medicationSelected));
		Log.d(App.DEBUG_TAG, "checkbox " + cb.toString());
		
		cb.setChecked(true);
	}
	
	
	private void addMedication() {

		CallableTask.invoke(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
								
				ContentValues values = new ContentValues();
				values.put(SymptomDataContract.PATIENT_RECORD_ID, recordId);
				values.put(SymptomDataContract.PATIENT_MEDICATION_ID, itemId);
				values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, date);
				values.put(SymptomDataContract.PATIENT_MEDICATION_TO, MAX_DATE);
				values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
				values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);
				
				Log.d(App.DEBUG_TAG, "insert values: " + values);
				
				String appendedUri = String.valueOf(recordId);
				Uri uriPatientMedications = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, appendedUri);
				
				if (getContentResolver().insert(uriPatientMedications, values) != null) {
					return 1;
				} else return 0;
				
			}
		}, 
		new TaskCallback<Integer>() {
			@Override
			public void success(Integer result) {
				// Fetch the cursor and for
				Log.d(App.DEBUG_TAG, "insert medications success");		
				
			}
			
			@Override
			public void error(Exception e) {
				Log.d(App.DEBUG_TAG, "Error while getting list of medications");
			}
		});
	}
	
	private void deleteMedication() {

		CallableTask.invoke(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
								
				String selection = SymptomDataContract.PATIENT_MEDICATION_ID + " = ?";
				String[] arguments = {itemId.toString()};
				
				Log.d(App.DEBUG_TAG, "delete medication: " + itemId);
				
				String appendedUri = String.valueOf(recordId);
				Uri uriPatientMedications = Uri.withAppendedPath(SymptomDataContract.PATIENT_MEDICATIONS_URI, appendedUri);
				

				ContentValues values = new ContentValues();
				values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
				values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);
				values.put(SymptomDataContract.PATIENT_MEDICATION_TO, date);
				
				return getContentResolver().update(uriPatientMedications, values, selection, arguments);
				
			}
		}, 
		new TaskCallback<Integer>() {
			@Override
			public void success(Integer result) {
				// Fetch the cursor and for
				Log.d(App.DEBUG_TAG, "delete successful ");		
				
			}
			
			@Override
			public void error(Exception e) {
				Log.d(App.DEBUG_TAG, "Error while getting list of medications");
			}
		});
	}
	
	
}

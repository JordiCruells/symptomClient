package org.jcruells.sm.client.patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.CallableTask;
import org.jcruells.sm.client.LoginScreenActivity;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.TaskCallback;
import org.jcruells.sm.client.data.CheckIn;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckinsListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public static final String FILTER ="filter";
	public static final int FILTER_CHECKINS_SINGLE_PATIENT = 1;
	public static final int FILTER_ALL_CHECKINS_PATIENTS_DOCTOR = 2;
	
	public static final String RECORD_ID = "recordId";
	
	private CheckinsCursorAdapter mAdapter;
	private static Integer recordId;

	private App app;
	private int filter = FILTER_CHECKINS_SINGLE_PATIENT;
	
	//@InjectView(R.id.btnAddCheckIn)
	protected Button btnAddCheckIn;
	
	@InjectView(R.id.listCheckIns)
	protected ListView listCheckIns;
	
	protected TextView noCheckins;
	
	/*@InjectView(R.id.btnSetupAlarms)
	protected Button btnSetupAlarms;*/
	
	/*@InjectView(R.id.btnSynchronize)
	protected Button btnSynchronize;*/
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onCreate");
		
		
		
		app = (App) getApplicationContext();
		
		Log.d(App.DEBUG_TAG, "get intent");
		
		Intent i = getIntent();
		if (i.hasExtra(FILTER)) {
			Log.d(App.DEBUG_TAG, "1");
			filter = i.getIntExtra(FILTER, FILTER_CHECKINS_SINGLE_PATIENT);
			Log.d(App.DEBUG_TAG, "filter is set to " + filter);
		}
		Log.d(App.DEBUG_TAG, "filter is" + filter);
		
		if (i.hasExtra(RECORD_ID)) {
			Log.d(App.DEBUG_TAG, "3");
			Log.d(App.DEBUG_TAG, "getting record id form extra " + app.getUser().getRecordNumber());
			recordId = i.getIntExtra(RECORD_ID, 0);
		} else {
			Log.d(App.DEBUG_TAG, "4");
			recordId = app.getUser().getRecordNumber();
		}
		
		Log.d(App.DEBUG_TAG, "get linear layout");
		LinearLayout layout = (LinearLayout) View.inflate(this,R.layout.checkins_list_activity, null);
		
		//If we are accesing as a patient show an add checkin button
		if (app.isPatientRole()) {
			//btnaddCheckIn.setEnabled(false);
			
			btnAddCheckIn = new Button(this);
			
			Log.d(App.DEBUG_TAG, "btnAddCheckIn" + btnAddCheckIn);
			
			btnAddCheckIn.setText(getResources().getString(R.string.new_check_in));
			btnAddCheckIn.setLayoutParams(new LinearLayout.LayoutParams(
		            						 	ViewGroup.LayoutParams.MATCH_PARENT,
		            						 	ViewGroup.LayoutParams.WRAP_CONTENT));
			
			Log.d(App.DEBUG_TAG, "add views ");
			
			layout.addView(btnAddCheckIn, 0);
			
			Log.d(App.DEBUG_TAG, "add views after");
			
			btnAddCheckIn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CheckinsListActivity.this, CheckInActivity.class);
					intent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_NEW);
					startActivity(intent);
				}
			});
			
			Log.d(App.DEBUG_TAG, "after set clicks listeners");
			
		}
		Log.d(App.DEBUG_TAG, "set content vierw");
		
		setContentView(layout);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		Log.d(App.DEBUG_TAG, "new checkins cursor adapter");
		mAdapter = new CheckinsCursorAdapter(this, null, 0);
				
		Log.d(App.DEBUG_TAG, "get reference list");
		
		//listCheckIns = (ListView) findViewById(R.id.listCheckIns);
		
		Log.d(App.DEBUG_TAG, "set list adapter");
		
		listCheckIns.setAdapter(mAdapter);
		
		listCheckIns.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				   
				   CheckIn checkin = mAdapter.get(position);
				   Intent intent = new Intent(CheckinsListActivity.this, CheckInActivity.class);				   
				   intent.putExtra(CheckInActivity.CHECKIN, checkin);
				   
				   
				   Log.d(App.DEBUG_TAG, "recordId: " + recordId);
				   Log.d(App.DEBUG_TAG, "checkin.getPatientRecordId(): " + checkin.getPatientRecordId());
				   
				   Log.d(App.DEBUG_TAG, "checkin.getSynced(): " + checkin.getSynced());
				   Log.d(App.DEBUG_TAG, " SymptomDataContract.STATE_NOT_SYNCED: " +  SymptomDataContract.STATE_NOT_SYNCED);
				   
				   if (checkin.getPatientRecordId() == recordId && checkin.getSynced() == SymptomDataContract.STATE_NOT_SYNCED ) {
					   Log.d(App.DEBUG_TAG, "yy edit");
					   intent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_EDIT);
				   } else {
					   Log.d(App.DEBUG_TAG, "yy view");
					   intent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_VIEW);
				   }
				   
				   Log.d(App.DEBUG_TAG, "start activity CHECKIN medication datetime" + checkin.getMedicationDatetime());
					
				   startActivity(intent);
			   } 
		});
		
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);		
		
	}
	
	
	
	
	
	
	
	
	

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onResume");	
		
		getLoaderManager().restartLoader(0, null, this);
	}
	
	
	// Close database
	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		Log.d(App.DEBUG_TAG, "on create loader<cursor> filter is " + filter);
		Loader<Cursor> loader = null;
		
		switch(filter) {
			case FILTER_CHECKINS_SINGLE_PATIENT:
				String selection = SymptomDataContract.PATIENT_RECORD_ID + "= ? ";
				String[] conditions = {String.valueOf(recordId)};
				String order = SymptomDataContract.CHECK_IN_DATETIME + " DESC";
				loader = new CursorLoader(this, SymptomDataContract.CHECKINS_URI, 
						SymptomDataContract.CHECKINS_ALL_COLUMNS,
						selection, conditions, order);
				break;
				
			case FILTER_ALL_CHECKINS_PATIENTS_DOCTOR:
				Uri checkinsDoctorUri = Uri.withAppendedPath(SymptomDataContract.CHECKINS_URI, String.valueOf(recordId)); 
				loader = new CursorLoader(this, checkinsDoctorUri, 
						SymptomDataContract.CHECKINS_ALL_COLUMNS,null, null, null);
				break;
		}
		
		return loader;
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "data is " + data);
		
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.checkinsLayout);
		
		if (data.getCount() > 0) {
			//LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.checkins_list_activity, null);
			/*if (noCheckins == null) {
				noCheckins = new TextView(this);
				noCheckins.setText(getResources().getString(R.string.no_checkins));
				noCheckins.setId(id);
			}*/
			layout.removeView(layout.findViewById(R.id.noCheckins));
			//setContentView(layout);	
			
		} 
		
		mAdapter.swapCursor(data);
		
		
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@OnClick(R.id.btnAddCheckIn)
	public void openNewCheckInActivity() {
		
		Intent intent = new Intent(this, CheckInActivity.class);
		intent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_NEW);
		startActivity(intent);
	}
		
	
	@OnClick(R.id.btnSetupAlarms)
	public void openSetupAlarms() {
		Log.d(App.DEBUG_TAG, "OPen setup alarms");
		Intent intent = new Intent(this, AlarmsSetupActivity.class);
		startActivity(intent);
	}*/
	
	
	/*@OnClick(R.id.btnSynchronize)
	public void synchronize() {
		
		Log.d(App.DEBUG_TAG, "onclick synchronize");
		
		Intent intent = new Intent(this, SyncService.class);
		
		Log.d(App.DEBUG_TAG, "start service");
		startService(intent);
		
	}*/

}

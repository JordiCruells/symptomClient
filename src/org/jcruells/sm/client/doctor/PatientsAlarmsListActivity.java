package org.jcruells.sm.client.doctor;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.SymptomDataContract;
import org.jcruells.sm.client.services.SyncService;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class PatientsAlarmsListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public static final String FILTER ="filter";
	public static final int FILTER_PATIENT_ALARMS = 1;
	public static final int FILTER_PATIENTS_DOCTOR = 2;
	public static final String FILTER_NAME = "name";
	public static final String FILTER_LAST_NAME = "last_name";
	
	private PatientsAlarmsCursorAdapter mAdapter;
	private BroadcastReceiver mReceiver;
	
	@InjectView(R.id.listAlarms)
	protected ListView listAlarms;
	
	/*@InjectView(R.id.btnSynchronize)
	protected Button btnSynchronize;*/
	
	private long doctorId;
	private int filter = FILTER_PATIENT_ALARMS;
	private String filterName = "";
	private String filterLastName = "";
	private LinearLayout layout;
	private TextView noAlarms;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		
		doctorId = ((App) getApplicationContext()).getUser().getRecordNumber();
		
		
		Intent i = getIntent();
		if (i.hasExtra(FILTER)) {
			filter = i.getIntExtra(FILTER, FILTER_PATIENT_ALARMS);
			if (filter == FILTER_PATIENTS_DOCTOR) {
				setTitle(getResources().getString(R.string.title_activity_patients_list));
			}
			if (i.hasExtra(FILTER_NAME)) {
				filterName = i.getStringExtra(FILTER_NAME);
			}
			if (i.hasExtra(FILTER_LAST_NAME)) {
				filterLastName = i.getStringExtra(FILTER_LAST_NAME);
			}
		}
		
		Log.d(App.DEBUG_TAG, "PatientsAlarmsListActivity onCreate " + filterName + " " + filterLastName);
		
		LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.patients_alarms_list_activity, null);
		
		setContentView(layout);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		Log.d(App.DEBUG_TAG, "call to read alarms");
			
		mAdapter = new PatientsAlarmsCursorAdapter(this, null, 0);
				
		
		Log.d(App.DEBUG_TAG, "set list adapter");
		
		listAlarms.setAdapter(mAdapter);
		
		listAlarms.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				   
				    Log.d(App.DEBUG_TAG, "on item click list patients");
				   
					Intent intent = new Intent(PatientsAlarmsListActivity.this, PatientCardActivity.class);
					intent.putExtra(PatientCardActivity.PATIENT, mAdapter.get(position));
					Log.d(App.DEBUG_TAG, "start activity patient record");
					
					startActivity(intent);
			   } 
		});
		
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);		
		
		/*getContentResolver().registerContentObserver(SymptomDataContract.ALARMS_URI,true,new AlarmsObserver());*/
		
	}
	
	
	/*@OnClick(R.id.btnSynchronize)
	public void synchronize() {
		
		Log.d(App.DEBUG_TAG, "onclick synchronize");
		
		Intent intent = new Intent(this, SyncService.class);
		
		Log.d(App.DEBUG_TAG, "start service");
		startService(intent);
		
	}*/
	
		

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "PatientsAlarmsListActivity onResume");
		refreshLoader();
		
		IntentFilter intentFilter = new IntentFilter(PatientAlertReceiver.INCOMING_PATIENT_ALERT);
 
		mReceiver = new BroadcastReceiver() {
 
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(App.DEBUG_TAG, "inside onReceive in patient alarms activity");
				refreshLoader();
			}
		};
		
		//registering receiver to receive incoming patient alerts
		Log.d(App.DEBUG_TAG, "Registering the receiver");
		this.registerReceiver(mReceiver, intentFilter);
		
	}
	
	private void refreshLoader() {
		Log.d(App.DEBUG_TAG, "PatientsAlarmsListActivity refresh loader");	
		getLoaderManager().restartLoader(0, null, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(this.mReceiver);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		Log.d(App.DEBUG_TAG, "on create loader<cursor> for doctorID " + doctorId);
		
		String alarmSelection=null;
		String[] patientAlarmsCondition = {String.valueOf(doctorId),
				String.valueOf(App.MINUTES_ALARM_MODERATE_TO_SEVERE_PAIN), 
				String.valueOf(App.MINUTES_ALARM_SEVERE_PAIN), 
				String.valueOf(App.MINUTES_ALARM_NO_EAT)};
		String alarmOrder=null;
		String[] alarmConditions=null;
		
		switch(filter) {
			case FILTER_PATIENT_ALARMS:
				alarmSelection = 
				   SymptomDataContract.DOCTOR_ID + " = ? AND (" +
		           SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES + " > ? OR " +
				   SymptomDataContract.SEVERE_PAIN_MINUTES + " > ? OR " +
				   SymptomDataContract.NO_EAT_MINUTES + " > ? )";
				alarmConditions = patientAlarmsCondition;
				alarmOrder = SymptomDataContract.LAST_CHECKIN_DATETIME + " DESC";
				break;
			case FILTER_PATIENTS_DOCTOR:
				alarmSelection = 
				   SymptomDataContract.DOCTOR_ID + " = " + String.valueOf(doctorId) + " AND " +
				"( " + SymptomDataContract.PATIENT_NAME      + " LIKE '%"  + filterName     + "%' OR '" + filterName     + "' = '') AND " +
				"( " + SymptomDataContract.PATIENT_LAST_NAME + " LIKE '%"  + filterLastName + "%' OR '" + filterLastName + "' = '')";
				alarmConditions = null;
				alarmOrder = SymptomDataContract.LAST_CHECKIN_DATETIME + " DESC";
				Log.d(App.DEBUG_TAG, "filter patients " + alarmSelection + "-" + filterName + filterLastName );
				break;
		}
		
		return new CursorLoader(this, SymptomDataContract.PATIENTS_URI, 
				SymptomDataContract.PATIENT_ALL_COLUMNS,
				alarmSelection, alarmConditions, alarmOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(App.DEBUG_TAG, "data is " + data);
		mAdapter.swapCursor(data);
		
		if (data.getCount() == 0) {
			LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.patients_alarms_list_activity, null);
			if (noAlarms == null) {
				noAlarms = new TextView(this);
				if (filter == FILTER_PATIENTS_DOCTOR) {
					noAlarms.setText(getResources().getString(R.string.no_patients));
				} else {
					noAlarms.setText(getResources().getString(R.string.no_alarms));
				}
			}
			layout.addView(noAlarms, 0);
			setContentView(layout);	
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}
	
	/*@SuppressLint("NewApi")
	class AlarmsObserver extends ContentObserver {     
		public AlarmsObserver(Handler handler) {
			super(handler);        
		}
		 
		@Override
		public void onChange(boolean selfChange) {
			this.onChange(selfChange, null);
		}    
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			Log.d(App.DEBUG_TAG, "on change content observer");	
			PatientsAlarmsListActivity outerActivity = PatientsAlarmsListActivity.this;
			Log.d(App.DEBUG_TAG, "call to refresh loader");	
			outerActivity.refreshLoader();
		}    
	}*/

}

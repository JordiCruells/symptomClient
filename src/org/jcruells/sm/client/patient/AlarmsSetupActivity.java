package org.jcruells.sm.client.patient;


import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.Alarm;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.app.LoaderManager;
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
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;



public class AlarmsSetupActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	@InjectView(R.id.listAlarms)
	protected ListView listAlarms;
	
	@InjectView(R.id.btnAddAlarm)
	protected Button btnAddAlarm;
	
	private AlarmsCursorAdapter mAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		
		Log.d(App.DEBUG_TAG, "CAlarms setup activity onCreate");
		
		setContentView(R.layout.alarms_list_activity);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
		
		
		mAdapter = new AlarmsCursorAdapter(this, null, 0);
		
		listAlarms.setAdapter(mAdapter);
		listAlarms.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				    
				   Log.d(App.DEBUG_TAG, "on item click");
				   openEditAlarm(mAdapter.getAlarmItem(position));
			   } 
		});
		
		
		Log.d(App.DEBUG_TAG, "init loader");
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "Alarm setup Activity onResume");	
		
		getLoaderManager().restartLoader(0, null, this);
	}
	
	
	@OnClick(R.id.btnAddAlarm)
	public void addAlarm() {
		Log.d(App.DEBUG_TAG, "add alarm");
		openInsertAlarm();
	}
	
	private void openInsertAlarm() {
		Log.d(App.DEBUG_TAG, "open edit alarm");
		
		Intent i = new Intent(AlarmsSetupActivity.this, EditAlarmActivity.class);
		i.putExtra(EditAlarmActivity.ACTION, EditAlarmActivity.ACTION_INSERT);
		startActivity(i);
	}
	
	private void openEditAlarm(Alarm alarm) {
		Log.d(App.DEBUG_TAG, "open edit alarm");
		
		Intent i = new Intent(AlarmsSetupActivity.this, EditAlarmActivity.class);
		i.putExtra(EditAlarmActivity.ALARM, alarm);
		i.putExtra(EditAlarmActivity.ACTION, EditAlarmActivity.ACTION_UPDATE);
		startActivity(i);
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		Log.d(App.DEBUG_TAG, "checkin medication on create loader<cursor>");
		
		App app = (App) getApplicationContext();
		String appendedUri = app.getUser().getRecordNumber().toString();
		
		Uri uriAlarms = Uri.withAppendedPath(SymptomDataContract.ALARMS_URI, appendedUri);
		
		Log.d(App.DEBUG_TAG, "Uri is :" + uriAlarms.toString());
	
		return new CursorLoader(this, uriAlarms , 
				SymptomDataContract.ALARM_ALL_COLUMNS,
				null, null,SymptomDataContract.ALARM_TIME + " ASC");
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

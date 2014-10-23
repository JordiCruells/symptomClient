package org.jcruells.sm.client.patient;



import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.CheckInDao;
import org.jcruells.sm.client.data.PatientDatabaseOpenHelper;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CheckinsListActivity extends ListActivity {
	
	//private SimpleCursorAdapter mAdapter;	
	private CheckinsCursorAdapter mAdapter;
	private CheckInDao dao; 
	private App app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		app = (App) getApplicationContext();
		
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onCreate");
		
		dao = new CheckInDao(((App) getApplicationContext()).getDB());
		setContentView(R.layout.checkins_list_activity);
		
		Log.d(App.DEBUG_TAG, "call to readCheckIns");
		// Create a cursor that obtains all the check-ins 
		Cursor c = dao.readCheckIns();
		
		Log.d(App.DEBUG_TAG, "create adapter");
		
		/*mAdapter = new SimpleCursorAdapter(this, R.layout.checkin_list_item, c,
				PatientDatabaseOpenHelper.checkins_list_columns, new int[] { R.id._id, R.id.date },
				0);
		*/
		
		mAdapter = new CheckinsCursorAdapter(this, c, 0);
		
				
		Log.d(App.DEBUG_TAG, "set list adapter");
		setListAdapter(mAdapter);

		
		
		
		//ButterKnife.inject(this);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(app, "position: " + position + ", id: " + id + ", itemId: " + l.getItemIdAtPosition(position) , Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onResume");
		
	}
	
	
	// Close database
	@Override
	protected void onDestroy() {

		SQLiteDatabase db = ((App) getApplicationContext()).getDB();
		db.close();
		
		
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		Log.d(App.DEBUG_TAG, "REMOVE THIS DELETION OF DATABASE WHEN NOT IN DEVELOPMENT !!!!");
		
		
		((PatientDatabaseOpenHelper) ((App) getApplicationContext()).getDbHelper()).deleteDatabase();
		
		super.onDestroy();

	}
	

}

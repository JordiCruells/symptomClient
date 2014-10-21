package org.jcruells.sm.client;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckinsListActivity extends Activity {
	
	@InjectView(R.id.patientName)
	protected TextView patientName_;
	
	App context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onCreate");
		
		context = (App) getApplicationContext();
		setContentView(R.layout.activity_checkins_list);
		ButterKnife.inject(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "CheckInsListActivity onResume");
		User user = context.getUser();
		patientName_.setText("Name: " + user.getName() + " Last name:" + user.getLastName() + " Medical Record: " + user.getRecordNumber() + " Birth Date: " + user.getBirthDate() );;
	}
	
	

}

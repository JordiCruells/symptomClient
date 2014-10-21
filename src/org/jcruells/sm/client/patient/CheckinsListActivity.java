package org.jcruells.sm.client.patient;



import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.User;
import org.jcruells.sm.client.R.id;
import org.jcruells.sm.client.R.layout;

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

package org.jcruells.sm.client.patient;



import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class PatientActivity extends Activity {
	
	@InjectView(R.id.patientName)
	protected TextView patientName_;
	
	private App context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Log.d(App.DEBUG_TAG, "PatientActivity onCreate");
		
		context = (App) getApplicationContext();
		setContentView(R.layout.patient_activity);
		ButterKnife.inject(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "PatientActivity onResume");
		User user = context.getUser();
		patientName_.setText("Name: " + user.getName() + " Last name:" + user.getLastName() + " Medical Record: " + user.getRecordNumber() + " Birth Date: " + user.getBirthDate() );;
	}
	
	

}

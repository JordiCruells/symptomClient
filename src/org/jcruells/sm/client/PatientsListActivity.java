package org.jcruells.sm.client;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class PatientsListActivity extends Activity {
	
	@InjectView(R.id.doctorName)
	protected TextView doctorName_;
	
	App context;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.d(App.DEBUG_TAG, "PatientsListActivity onCreate");
		setContentView(R.layout.activity_patients_list);
		context = (App) getApplicationContext();
		ButterKnife.inject(this);
	}
	
	
	@Override
	protected void onResume() {
		
		
		super.onResume();
		Log.d(App.DEBUG_TAG, "PatientsListActivity onResume");
		doctorName_.setText(context.getUser().getName());
	}

}

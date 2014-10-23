package org.jcruells.sm.client.doctor;


import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class DoctorActivity extends Activity {
	
	@InjectView(R.id.doctorName)
	protected TextView doctorName_;
	
	private App context;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.d(App.DEBUG_TAG, "DoctorActivity onCreate");
		setContentView(R.layout.doctor_activity);
		context = (App) getApplicationContext();
		ButterKnife.inject(this);
	}
	
	
	@Override
	protected void onResume() {
		
		
		super.onResume();
		Log.d(App.DEBUG_TAG, "DoctorActivity onResume");
		doctorName_.setText(context.getUser().getName());
	}

}

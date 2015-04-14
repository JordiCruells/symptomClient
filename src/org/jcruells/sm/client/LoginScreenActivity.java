package org.jcruells.sm.client;

import java.util.concurrent.Callable;

import org.jcruells.sm.client.data.LocalDBSvcApi;
import org.jcruells.sm.client.data.User;
import org.jcruells.sm.client.doctor.DoctorActivity;
import org.jcruells.sm.client.doctor.PatientCardActivity;
import org.jcruells.sm.client.patient.AlarmsHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;


public class LoginScreenActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;
	
	@InjectView(R.id.spinnerUsers)
	protected Spinner spinnerUsers;
	
	
	/* To enable introduce the login credentials via spinner, just for testing purposes !! */
	final String users[] = {"doctor1","doctor2","patient1","patient2","patient3","patient4","patient5"};
	final String passwords[] = {"pwd_d1","pwd_d2","pwd_p1","pwd_p2","pwd_p3","pwd_p4","pwd_p5"};

	
	App app;
	
	// THIS IS TO FORCE TO CREATE THE DATABASE EVERY TIME THE APPP IS STARTED
	@Override
	protected void onDestroy() {
		
		CallableTask.invoke(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				getApplicationContext().deleteDatabase("sm_patient_db");
				return null;
			}
		}, new TaskCallback<Void>() {
			@Override
			public void success(Void result) {
				Log.d(App.DEBUG_TAG, "database deleted");
			}

			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error while deleting database.", e);
			}
		});
		super.onDestroy();
	}
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen_activity);

		app = (App) getApplicationContext();
		ButterKnife.inject(this);
		
		// Load users in the spinner (this is useful for testing purposes)
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerUsers.setAdapter(spinnerArrayAdapter);
		
	}
	
	@OnItemSelected(R.id.spinnerUsers)
	public void selectUser(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		userName_.setText(users[position]);
		password_.setText(passwords[position]);
	}	


	@OnClick(R.id.loginButton)
	public void login() {
		Log.d(App.DEBUG_TAG, "login");
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		
		final App app = ((App)getApplicationContext());
		
		Log.d(App.DEBUG_TAG, "is logged: " + app.isUserLoggedIn());
		
		
		
		// Force the aplication to log in
		app.setUserLoggedIn(false);

		final UserSvcApi svc = UserSvc.init(user, pass, app);

		CallableTask.invoke(new Callable<User>() {

			@Override
			public User call() throws Exception {
				return svc.getUserData();
			}
		}, new TaskCallback<User>() {

			@Override
			public void success(User result) {
				 
				
				Log.d(App.DEBUG_TAG, "login success");
				
				String role = app.getUser().getRole();
				//String name = app.getUser().getName();
				
				Log.d(App.DEBUG_TAG, "role:" + role);
				//Log.d(App.DEBUG_TAG, "name:" + name);
				
				
				// Open the apropiate activity according to the role of the user 
				if (app.isDoctorRole()) {
				
					Log.d(App.DEBUG_TAG, "is doctor role, userId: " + app.getUser().getId());
					startActivity(new Intent(
							LoginScreenActivity.this,
							DoctorActivity.class));
					
				} else {
					
					// Setup alarms
					Log.d(App.DEBUG_TAG, "call to alarmsHelper");
					AlarmsHelper alarmsHelper = new AlarmsHelper(app);
					alarmsHelper.loadAlarms();
					app.setAlarmsHelper(alarmsHelper);
					
					// And load initial activity
					
					final LocalDBSvcApi localDBSvc = new LocalDBSvcApi(app);
					Intent i = new Intent(LoginScreenActivity.this, PatientCardActivity.class);
					i.putExtra(PatientCardActivity.PATIENT, localDBSvc.getPatientFromLocalDB(app.getUser().getRecordNumber()));
					Log.d(App.DEBUG_TAG, "satart patient card activity with recordid =  " + app.getUser().getRecordNumber());
					startActivity(i);
					
				}
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
				
				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
				
				app.setUserLoggedIn(false);
			}
		});
	}

}

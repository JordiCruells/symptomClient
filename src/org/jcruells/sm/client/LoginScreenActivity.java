package org.jcruells.sm.client;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.jcruells.sm.client.doctor.PatientsListActivity;
import org.jcruells.sm.client.patient.CheckinsListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;
	
	App context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen_activity);

		context = (App) getApplicationContext();
		ButterKnife.inject(this);
	}

	@OnClick(R.id.loginButton)
	public void login() {
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();
		
		final App app = ((App)getApplicationContext());

		final VideoSvcApi svc = VideoSvc.init(server, user, pass, app);

		CallableTask.invoke(new Callable<Collection<Video>>() {

			@Override
			public Collection<Video> call() throws Exception {
				return svc.getVideoList();
			}
		}, new TaskCallback<Collection<Video>>() {

			@Override
			public void success(Collection<Video> result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing
				
				Log.d(App.DEBUG_TAG, "login success");
				
				String role = app.getUser().getRole();
				String name = app.getUser().getName();
				
				
				Log.d(App.DEBUG_TAG, "role:" + role);
				Log.d(App.DEBUG_TAG, "name:" + name);
				
				
				// Open the apropiate activity according to the role of the user 
				if (context.isDoctorRole()) {
				
					startActivity(new Intent(
							LoginScreenActivity.this,
							PatientsListActivity.class));
					
				} else {
					
					startActivity(new Intent(
							LoginScreenActivity.this,
							CheckinsListActivity.class));
				}
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
				
				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}

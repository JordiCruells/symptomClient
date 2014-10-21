package org.jcruells.sm.client;

import android.app.Application;


/* This class is used to store a global
 * state of the application
 */

public class App extends Application {
	
	public static final String DEBUG_TAG = "SM_DEBUG";
	public static final String ROLE_DOCTOR = "DOCTOR";
	public static final String ROLE_PATIENT = "PATIENT";
	
	private User user;
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isDoctor() {
		return this.user.getRole().equals(ROLE_DOCTOR);
	}
	public boolean isPatient() {
		return this.user.getRole().equals(ROLE_PATIENT);
	}
}

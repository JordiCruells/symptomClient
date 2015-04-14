package org.jcruells.sm.client;

import java.util.Date;

import org.jcruells.sm.client.data.User;
import org.jcruells.sm.client.patient.AlarmsHelper;

import android.app.Application;



/* This class is used to store a global
 * state of the application
 */

public class App extends Application {
	
	public static final String SERVER = "https://10.0.2.2:8443";
	public static final String DEBUG_TAG = "SM_DEBUG";
	public static final String ROLE_DOCTOR = "DOCTOR";
	public static final String ROLE_PATIENT = "PATIENT";
	
	public static final int MINUTES_ALARM_SEVERE_PAIN = 12 * 60;
	public static final int MINUTES_ALARM_MODERATE_TO_SEVERE_PAIN = 18 * 60;
	public static final int MINUTES_ALARM_NO_EAT = 12 * 60;			
	
	private User user;
	private Date lastSynch;
	private AlarmsHelper alarmsHelper;
	private String token = null;
	private boolean userLoggedIn = false;
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean loggedIn) {
		this.userLoggedIn = loggedIn;
	}

	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isDoctorRole() {
		return this.user.getRole().equals(ROLE_DOCTOR);
	}
	public boolean isPatientRole() {
		return this.user.getRole().equals(ROLE_PATIENT);
	}
		
	public Date getLastSynch() {
		return lastSynch;
	}

	public void setLastSynch(Date lastSynch) {
		this.lastSynch = lastSynch;
	}

	public AlarmsHelper getAlarmsHelper() {
		return alarmsHelper;
	}

	public void setAlarmsHelper(AlarmsHelper alarmsHelper) {
		this.alarmsHelper = alarmsHelper;
	}
}

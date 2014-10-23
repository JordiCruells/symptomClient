package org.jcruells.sm.client;

import java.util.Date;

import org.jcruells.sm.client.data.DoctorDatabaseOpenHelper;
import org.jcruells.sm.client.data.PatientDatabaseOpenHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/* This class is used to store a global
 * state of the application
 */

public class App extends Application {
	
	public static final String DEBUG_TAG = "SM_DEBUG";
	public static final String ROLE_DOCTOR = "DOCTOR";
	public static final String ROLE_PATIENT = "PATIENT";
	
	private User user;
	private SQLiteOpenHelper dbHelper;
	private SQLiteDatabase db;
	private Date lastSynch;

	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	public SQLiteDatabase getDB() {
		return db;
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
	
	public void startDB() {
		
		Log.d(DEBUG_TAG, "inside satartDB");
		
		if (isPatientRole()) {
			
			Log.d(DEBUG_TAG, "is patient");
			dbHelper = new PatientDatabaseOpenHelper(this);
			db = dbHelper.getWritableDatabase();
			return;
		}
		
		if (isDoctorRole()) {
			Log.d(DEBUG_TAG, "is doctor");
			dbHelper = new DoctorDatabaseOpenHelper(this);
			db =dbHelper.getWritableDatabase();
			return;
		}		
	}

	public Date getLastSynch() {
		return lastSynch;
	}


	public void setLastSynch(Date lastSynch) {
		this.lastSynch = lastSynch;
	}
}

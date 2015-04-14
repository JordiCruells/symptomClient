package org.jcruells.sm.client.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jcruells.sm.client.App;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.google.common.base.Joiner;

public class SymptomProvider extends ContentProvider {

	private static SymptomDatabaseOpenHelper dbHelper;
	private static SQLiteDatabase db = null;
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final int CHECK_INS = 1;
	private static final int MEDICATIONS_PATIENT = 2;
	private static final int PATIENTS = 3;
	private static final int MEDICATIONS = 4;
	private static final int ALARMS_PATIENT = 5;
	private static final int DOCTORS = 6;
	private static final int CHECKINS_DOCTOR = 7;
	private static final int MEDICATIONS_DOCTOR = 8;

	
	private static final String queryMedicationsPatient = 
			"SELECT A." +
			Joiner.on(", A.").join(SymptomDataContract.PATIENT_MEDICATION_ALL_COLUMNS) + 
			", B." + SymptomDataContract.MEDICATION_NAME +
			" FROM " + SymptomDataContract.PATIENT_MEDICATIONS_TABLE + " A INNER JOIN " +
			SymptomDataContract.MEDICATIONS_TABLE + " B ON A." + SymptomDataContract.PATIENT_MEDICATION_ID +
			" = B." + SymptomDataContract.ID +
			" WHERE A." + SymptomDataContract.PATIENT_RECORD_ID + " = ?  " +
			" AND A." + SymptomDataContract.PATIENT_MEDICATION_FROM + " <= ? " +
			" AND A." + SymptomDataContract.PATIENT_MEDICATION_TO + " > ? " + 
			" ORDER BY B." + SymptomDataContract.MEDICATION_NAME;
	
	
	private static final String queryMedicationsDoctor = 
			"SELECT A." +
			Joiner.on(", A.").join(SymptomDataContract.PATIENT_MEDICATION_ALL_COLUMNS) + 
			" FROM " + SymptomDataContract.PATIENT_MEDICATIONS_TABLE + " A INNER JOIN " +
			SymptomDataContract.PATIENTS_TABLE + " B ON A." + SymptomDataContract.PATIENT_RECORD_ID +
			" = B." + SymptomDataContract.PATIENT_RECORD_ID +
			" WHERE B." + SymptomDataContract.DOCTOR_ID + " = ?  " +
			" AND A." + SymptomDataContract.SYNCED + " = " + String.valueOf(SymptomDataContract.STATE_NOT_SYNCED) +  
			" ORDER BY A." + SymptomDataContract.PATIENT_RECORD_ID + ", A." + SymptomDataContract.PATIENT_MEDICATION_FROM;
	
	
	private static final String  queryCheckinsPatientsDoctor = 
			"SELECT A." +
			Joiner.on(", A.").join(SymptomDataContract.CHECKINS_ALL_COLUMNS) + 
			" FROM " + SymptomDataContract.CHECKINS_TABLE + " A INNER JOIN " +
			SymptomDataContract.PATIENTS_TABLE + " B ON A." + SymptomDataContract.PATIENT_RECORD_ID +
			" = B." + SymptomDataContract.PATIENT_RECORD_ID +
			" WHERE B." + SymptomDataContract.DOCTOR_ID + " = ?  ";
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
			
		Log.d(App.DEBUG_TAG, "inside onCreate PatientProvider");
			
		dbHelper = new SymptomDatabaseOpenHelper(getContext());
		
		Log.d(App.DEBUG_TAG, "got dbhelper");
		db = dbHelper.getWritableDatabase();
		
		Log.d(App.DEBUG_TAG, "got db");
		
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.CHECKINS_TABLE, CHECK_INS);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.PATIENT_MEDICATIONS_TABLE + "/#", MEDICATIONS_PATIENT);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.PATIENTS_TABLE, PATIENTS);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.MEDICATIONS_TABLE, MEDICATIONS);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.ALARMS_TABLE + "/#", ALARMS_PATIENT);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.DOCTORS_TABLE, DOCTORS);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.CHECKINS_TABLE + "/#", CHECKINS_DOCTOR);
		sUriMatcher.addURI(SymptomDataContract.BASE_URI_STRING, SymptomDataContract.PATIENT_MEDICATIONS_TABLE + "/doctor/#", MEDICATIONS_DOCTOR);
		Log.d(App.DEBUG_TAG, "about to return");
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		Log.d(App.DEBUG_TAG, "in query of PatientProvider");
		// TODO Auto-generated method stub
		Cursor c = null;
		String[] _selectionArgs = {};
		String groupBy = null;
		String having = null;
		if (selectionArgs == null) selectionArgs = _selectionArgs;
		
		Log.d(App.DEBUG_TAG, "uri -> " + uri + "Match to " + sUriMatcher.match(uri));
		
		switch(sUriMatcher.match(uri)) {
			case CHECK_INS:
				Log.d(App.DEBUG_TAG, "in one");
				
				c = db.query(SymptomDataContract.CHECKINS_TABLE,
						   SymptomDataContract.CHECKINS_ALL_COLUMNS, selection, selectionArgs, groupBy, having, sortOrder);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case MEDICATIONS_PATIENT:
				Log.d(App.DEBUG_TAG, "in 2");
				String currentDay = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
				Log.d(App.DEBUG_TAG, "gettin medications for day " + currentDay);
				String[] params_medications_patient = {uri.getLastPathSegment(), currentDay, currentDay};
				Log.d(App.DEBUG_TAG, "getLastPathSegment is " + uri.getLastPathSegment());
				c = db.rawQuery(queryMedicationsPatient, params_medications_patient);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case PATIENTS:
				Log.d(App.DEBUG_TAG, "in 3");
				c = db.query(SymptomDataContract.PATIENTS_TABLE,
						   SymptomDataContract.PATIENT_ALL_COLUMNS, selection, selectionArgs, groupBy, having, sortOrder);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case MEDICATIONS:
				Log.d(App.DEBUG_TAG, "in 4");
				c = db.query(SymptomDataContract.MEDICATIONS_TABLE,
						   SymptomDataContract.MEDICATION_ALL_COLUMNS, selection, selectionArgs, groupBy, having, sortOrder);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case ALARMS_PATIENT:
				Log.d(App.DEBUG_TAG, "in 5");
				c = db.query(SymptomDataContract.ALARMS_TABLE,
						   SymptomDataContract.ALARM_ALL_COLUMNS, selection, selectionArgs, groupBy, having, sortOrder);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case DOCTORS:
				Log.d(App.DEBUG_TAG, "in 6");
				c = db.query(SymptomDataContract.DOCTORS_TABLE,
						   SymptomDataContract.DOCTOR_ALL_COLUMNS, selection, selectionArgs, groupBy, having, sortOrder);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			case MEDICATIONS_DOCTOR:
				Log.d(App.DEBUG_TAG, "in MEDICATIONS_DOCTOR");
				String[] params_medications_doctor = {uri.getLastPathSegment()};
				Log.d(App.DEBUG_TAG, "getLastPathSegment is " + uri.getLastPathSegment());
				c = db.rawQuery(queryMedicationsDoctor, params_medications_doctor);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
				
			case CHECKINS_DOCTOR:
				Log.d(App.DEBUG_TAG, "in CHECKINS_DOCTOR");
				String[] param_doctor_id = {uri.getLastPathSegment()};
				Log.d(App.DEBUG_TAG, "getLastPathSegment is " + uri.getLastPathSegment());
				c = db.rawQuery(queryCheckinsPatientsDoctor, param_doctor_id);
				Log.d(App.DEBUG_TAG, "breaking");
				break;
			default:
		
		}
		
		Log.d(App.DEBUG_TAG, "returning form query, result is " + c);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		String type = null;
		switch(sUriMatcher.match(uri)) {
			case 1:
				//type = TYPE_MULTIPLE_ROWS + "." + URI_PATH + "." + PatientDatabaseOpenHelper.CHECKINS_TABLE;
				type = SymptomDataContract.CONTENT_DIR_TYPE_CHECKINS;
				break;
			default:
				break;
		}
		
		return type;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri mNewUri = null;
		long insertedId;
		switch(sUriMatcher.match(uri)) {
		    case MEDICATIONS_PATIENT:
		    	Log.d(App.DEBUG_TAG, "about to insert new MEDICATION PATIENT");
		    	insertedId = db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
				mNewUri = Uri.withAppendedPath(SymptomDataContract.CHECKINS_URI, String.valueOf(insertedId));
				break;
		    case CHECKINS_DOCTOR:
		    	Log.d(App.DEBUG_TAG, "about to insert new checkin for doctor pulled from the server");
		    	insertedId = db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
				mNewUri = Uri.withAppendedPath(SymptomDataContract.CHECKINS_URI, String.valueOf(insertedId));
				break;
			case CHECK_INS:
				
				Log.d(App.DEBUG_TAG, "about to insert new checkin");
				Log.d(App.DEBUG_TAG, "values are: " + values);
			
				insertedId = db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
				Log.d(App.DEBUG_TAG, "inserted id is " + insertedId);
				
				// Check-in inserted, now update timers on patient 
				if (insertedId > 0) {
					final int recordNumber = values.getAsInteger(SymptomDataContract.PATIENT_RECORD_ID);					
					final int painLevel = values.getAsInteger(SymptomDataContract.PAIN_LEVEL);
					final int stoppedEatingLevel = values.getAsInteger(SymptomDataContract.STOPPED_EATING_LEVEL);
					final String strCurrentCheckInDate = values.getAsString(SymptomDataContract.CHECK_IN_DATETIME);
					final String[] columns = {SymptomDataContract.SEVERE_PAIN_MINUTES,SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES,
							SymptomDataContract.NO_EAT_MINUTES, SymptomDataContract.LAST_CHECKIN_DATETIME};
					final String selection = SymptomDataContract.PATIENT_RECORD_ID + "=?";
					final String[] selectionArgs = {String.valueOf(recordNumber)};
					int moderateToSeverePainMinutes = 0, severePainMinutes = 0, stoppedEatingMinutes = 0;
					
					
					
					// If levels are high we must update acumulated times
					if (painLevel >= SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE || stoppedEatingLevel >= SymptomDataContract.ANSWER_STOPPED_EATING_YES) {
					
						Log.d(App.DEBUG_TAG, "pain level " + painLevel + " - " + "eatLevel " + stoppedEatingLevel);
						long minutesFromLastCheckIn;
						
						Log.d(App.DEBUG_TAG, "before select");
						// Get last values for this patient 
						Cursor c = db.query(SymptomDataContract.PATIENTS_TABLE, columns, selection, selectionArgs, null, null, null, null);
						
						Log.d(App.DEBUG_TAG, "after select");
						
						if (c.moveToFirst()) {
						
							Log.d(App.DEBUG_TAG," c.movetofirst()");
							
							moderateToSeverePainMinutes = c.getInt(c.getColumnIndex(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES));
							severePainMinutes = c.getInt(c.getColumnIndex(SymptomDataContract.SEVERE_PAIN_MINUTES));
							stoppedEatingMinutes = c.getInt(c.getColumnIndex(SymptomDataContract.NO_EAT_MINUTES));
							String strPreviousCheckInDate =  c.getString(c.getColumnIndex(SymptomDataContract.LAST_CHECKIN_DATETIME));
					
							Log.d(App.DEBUG_TAG, "moderateToSeverePainMinutes : " + moderateToSeverePainMinutes);
							Log.d(App.DEBUG_TAG, "severePainMinutes : " + severePainMinutes);
							Log.d(App.DEBUG_TAG, "stoppedEatingMinutes : " + stoppedEatingMinutes);
							Log.d(App.DEBUG_TAG, "strPreviousCheckInDate : " + strPreviousCheckInDate);
							Log.d(App.DEBUG_TAG, "strCurrentCheckInDate : " + strCurrentCheckInDate);
							
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date previousCheckInDate = null, currentCheckInDate = null;
							
							try {
								previousCheckInDate = formatter.parse(strPreviousCheckInDate);
								currentCheckInDate = formatter.parse(strCurrentCheckInDate);
							} 
							catch(ParseException e) {
								Log.d(App.DEBUG_TAG, "something went worng when parsing the checkin date");	
							}
							
							minutesFromLastCheckIn = (currentCheckInDate.getTime() - previousCheckInDate.getTime()) / 60000l; 
							
							Log.d(App.DEBUG_TAG, "minutes from last checkin " + minutesFromLastCheckIn);
									
							if (painLevel >= SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE) {
								moderateToSeverePainMinutes += minutesFromLastCheckIn; 
							} else {
								moderateToSeverePainMinutes = 0;
							}
							
							if (painLevel >= SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE) {
								severePainMinutes += minutesFromLastCheckIn;								
							} else {
								severePainMinutes = 0;
							}
							
							if (stoppedEatingLevel == SymptomDataContract.ANSWER_STOPPED_EATING_YES) {
								stoppedEatingMinutes += minutesFromLastCheckIn;	
							} else {
								stoppedEatingMinutes = 0;
							}
						
							Log.d(App.DEBUG_TAG, "moderateToSeverePainMinutes : " + moderateToSeverePainMinutes);
							Log.d(App.DEBUG_TAG, "severePainMinutes : " + severePainMinutes);
							Log.d(App.DEBUG_TAG, "stoppedEatingMinutes : " + stoppedEatingMinutes);
						}
						c.close();
					}
					
					
					ContentValues mUpdatePatient = new ContentValues();
					mUpdatePatient.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES , moderateToSeverePainMinutes);
					mUpdatePatient.put(SymptomDataContract.SEVERE_PAIN_MINUTES , severePainMinutes);
					mUpdatePatient.put(SymptomDataContract.NO_EAT_MINUTES , stoppedEatingMinutes);
					mUpdatePatient.put(SymptomDataContract.LAST_CHECKIN_DATETIME , strCurrentCheckInDate);
					mUpdatePatient.put(SymptomDataContract.PAIN_LEVEL , painLevel);
					mUpdatePatient.put(SymptomDataContract.STOPPED_EATING_LEVEL , stoppedEatingLevel);
					mUpdatePatient.put(SymptomDataContract.SYNC_ACTION , SymptomDataContract.SYNC_UPDATE);
					mUpdatePatient.put(SymptomDataContract.SYNCED , SymptomDataContract.STATE_NOT_SYNCED);
					
					
					Log.d(App.DEBUG_TAG, "db update");
					db.update(SymptomDataContract.PATIENTS_TABLE, mUpdatePatient, 
							  selection , selectionArgs);
					Log.d(App.DEBUG_TAG, "after db update");
				}
				
				mNewUri = Uri.withAppendedPath(SymptomDataContract.CHECKINS_URI, String.valueOf(insertedId));
				
				break;
				
			case ALARMS_PATIENT:
		    	Log.d(App.DEBUG_TAG, "about to insert new ALARM PATIENT");
		    	insertedId = db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
				mNewUri = Uri.withAppendedPath(SymptomDataContract.ALARMS_URI, String.valueOf(insertedId));
				
			default:
				break;
		}
		
		return mNewUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int deletedRows = 0;
		switch(sUriMatcher.match(uri)) {
			 case MEDICATIONS_PATIENT:
			    	Log.d(App.DEBUG_TAG, "about to delete row MEDICATION PATIENT");
			    	deletedRows = db.delete(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, selection, selectionArgs);
			    	break;
			 case ALARMS_PATIENT:
			    	Log.d(App.DEBUG_TAG, "about to delete row alarm PATIENT");
			    	deletedRows = db.delete(SymptomDataContract.ALARMS_TABLE, selection, selectionArgs);
			    	break;
			 default:
				 break;
			
		}
		return deletedRows;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		Log.d(App.DEBUG_TAG, "in content provider update");
		Log.d(App.DEBUG_TAG, "uri " + uri.toString());
		Log.d(App.DEBUG_TAG, "sUriMatcher.match(uri) " + sUriMatcher.match(uri));
		
		int updatedRows = 0;
		switch(sUriMatcher.match(uri)) {
			 case MEDICATIONS_PATIENT:
			    	Log.d(App.DEBUG_TAG, "about to update row MEDICATION PATIENT");
			    	updatedRows = db.update(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, values, selection, selectionArgs);
			    	break;
			 case ALARMS_PATIENT:
			    	Log.d(App.DEBUG_TAG, "about to update row alarm PATIENT");
			    	updatedRows = db.update(SymptomDataContract.ALARMS_TABLE, values, selection, selectionArgs);
			    	break;
			 case PATIENTS:
				 	Log.d(App.DEBUG_TAG, "about to update row PATIENT");
			    	updatedRows = db.update(SymptomDataContract.PATIENTS_TABLE, values, selection, selectionArgs);
			    	break;
			 case CHECK_INS:
					Log.d(App.DEBUG_TAG, "about to update row PATIENT");
			    	updatedRows = db.update(SymptomDataContract.CHECKINS_TABLE, values, selection, selectionArgs);
			    	break;
			 case DOCTORS:
					Log.d(App.DEBUG_TAG, "about to update row DOCTOR");
			    	updatedRows = db.update(SymptomDataContract.DOCTORS_TABLE, values, selection, selectionArgs);
			    	break;
			 default:
				 break;
			
		}
		Log.d(App.DEBUG_TAG, "updated rows: " + updatedRows);
		return updatedRows;
	}
	
	public static SymptomDatabaseOpenHelper getDbHelper() {
		return dbHelper;
	}

	
	public static SQLiteDatabase getDb() {
		return db;
	}

}

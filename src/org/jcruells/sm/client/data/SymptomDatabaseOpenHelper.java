package org.jcruells.sm.client.data;


import org.jcruells.sm.client.App;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SymptomDatabaseOpenHelper extends SQLiteOpenHelper {
	
		
	final private static String CREATE_CHECKINS_TABLE =

	"CREATE TABLE " + SymptomDataContract.CHECKINS_TABLE + " (" 
			+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SymptomDataContract.PATIENT_RECORD_ID + " INTEGER NOT NULL, "
			+ SymptomDataContract.CHECK_IN_DATETIME + " TEXT NOT NULL, "  
			+ SymptomDataContract.PAIN_LEVEL + " INTEGER NOT NULL, "  
			+ SymptomDataContract.MEDICATION_TAKEN + " INTEGER NOT NULL, "  
			+ SymptomDataContract.MEDICATION_DATETIME + " TEXT NOT NULL, "
			+ SymptomDataContract.MEDICATION_ANSWERS + " TEXT NOT NULL, "
			+ SymptomDataContract.STOPPED_EATING_LEVEL + " INTEGER NOT NULL, "
			+ SymptomDataContract.SERVER_TIMESTAMP + " TEXT NULL, "
			+ SymptomDataContract.SYNC_ACTION + " INTEGER NOT NULL, " 
			+ SymptomDataContract.SYNCED + " INTEGER NOT NULL " 
			+ ")";
	
	final private static String CREATE_MEDICATIONS_TABLE =
	"CREATE TABLE " + SymptomDataContract.MEDICATIONS_TABLE + " (" 
			+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SymptomDataContract.MEDICATION_NAME + " TEXT NOT NULL, "
			+ SymptomDataContract.SERVER_TIMESTAMP + " TEXT NULL, "
			+ SymptomDataContract.SYNC_ACTION + " INTEGER NOT NULL, " 
			+ SymptomDataContract.SYNCED + " INTEGER NOT NULL " 
			+ ")";
	
	final private static String CREATE_PATIENT_MEDICATIONS_TABLE =
	"CREATE TABLE " + SymptomDataContract.PATIENT_MEDICATIONS_TABLE + " (" 
			+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SymptomDataContract.PATIENT_RECORD_ID + " INTEGER NOT NULL, "  
			+ SymptomDataContract.PATIENT_MEDICATION_ID + " INTEGER NOT NULL, " 
			+ SymptomDataContract.PATIENT_MEDICATION_FROM + " TEXT NOT NULL, "
			+ SymptomDataContract.PATIENT_MEDICATION_TO + " TEXT NOT NULL, " 
			+ SymptomDataContract.SERVER_TIMESTAMP + " TEXT NULL, "
			+ SymptomDataContract.SYNC_ACTION + " INTEGER NOT NULL, "
			+ SymptomDataContract.SYNCED + " INTEGER NOT NULL " 
			+ ")"; 
	
	final private static String CREATE_PATIENTS_TABLE =
			"CREATE TABLE " + SymptomDataContract.PATIENTS_TABLE + " (" 
					+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ SymptomDataContract.PATIENT_RECORD_ID + " INTEGER NOT NULL, "
					+ SymptomDataContract.DOCTOR_ID + " INTEGER NOT NULL, "
					+ SymptomDataContract.PATIENT_NAME + " TEXT NOT NULL, " 
					+ SymptomDataContract.PATIENT_LAST_NAME + " TEXT NOT NULL, "
					+ SymptomDataContract.PATIENT_BIRTHDAY + " TEXT NOT NULL, "
					+ SymptomDataContract.SEVERE_PAIN_MINUTES + " INTEGER NOT NULL, "
					+ SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES + " INTEGER NOT NULL, "
					+ SymptomDataContract.NO_EAT_MINUTES + " INTEGER NOT NULL, "
					+ SymptomDataContract.PAIN_LEVEL + " INTEGER NOT NULL, "
					+ SymptomDataContract.STOPPED_EATING_LEVEL + " INTEGER NOT NULL, "
					+ SymptomDataContract.LAST_CHECKIN_DATETIME + " TEXT NULL, "					
					+ SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP + " TEXT NOT NULL, "
					+ SymptomDataContract.SERVER_TIMESTAMP + " TEXT NULL, "
					+ SymptomDataContract.SYNC_ACTION + " INTEGER NOT NULL, " 					
					+ SymptomDataContract.SYNCED + " INTEGER NOT NULL " 
					+ ");";
	
	final private static String CREATE_DOCTORS_TABLE =
			"CREATE TABLE " + SymptomDataContract.DOCTORS_TABLE + " (" 
					+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ SymptomDataContract.USER_ID + " INTEGER NOT NULL, "
					+ SymptomDataContract.DOCTOR_NAME + " TEXT NOT NULL, " 
					+ SymptomDataContract.DOCTOR_LAST_NAME + " TEXT NOT NULL, "					
					+ SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP + " TEXT NOT NULL, "
					+ SymptomDataContract.SERVER_TIMESTAMP + " TEXT NULL, "
					+ SymptomDataContract.SYNC_ACTION + " INTEGER NOT NULL, " 					
					+ SymptomDataContract.SYNCED + " INTEGER NOT NULL " 
					+ ");";
	
	final private static String CREATE_ALARMS_TABLE =
			"CREATE TABLE " + SymptomDataContract.ALARMS_TABLE + " (" 
					+ SymptomDataContract.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ SymptomDataContract.PATIENT_RECORD_ID  + " INTEGER NOT NULL, "
					+ SymptomDataContract.ALARM_TIME  + " TEXT NOT NULL, "
					+ SymptomDataContract.ALARM_ACTIVATED  + " INTEGER NOT NULL, "
					+ SymptomDataContract.ALARM_VIBRATE  + " INTEGER NOT NULL "
					+ ");";
	
	
	
	final private static String DB_NAME = "sm_patient_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public SymptomDatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		Log.d(App.DEBUG_TAG, "PatientDatabaseOpenHelper constructor");
		this.mContext = context;
		Log.d(App.DEBUG_TAG, "mcontext set");
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.d(App.DEBUG_TAG, "CHECKINS TABLE CREATE");
		db.execSQL(CREATE_CHECKINS_TABLE);
		Log.d(App.DEBUG_TAG, "MEDICATIONS TABLE CREATE");
		db.execSQL(CREATE_MEDICATIONS_TABLE);
		Log.d(App.DEBUG_TAG, "PATIENT MEDICATIONS TABLE CREATE");
		db.execSQL(CREATE_PATIENT_MEDICATIONS_TABLE);
		Log.d(App.DEBUG_TAG, "PATIENTS TABLE CREATE");
		db.execSQL(CREATE_PATIENTS_TABLE);		
		Log.d(App.DEBUG_TAG, "DOCTORS TABLE CREATE");
		db.execSQL(CREATE_DOCTORS_TABLE);
		Log.d(App.DEBUG_TAG, "ALARMS TABLE CREATE");
		db.execSQL(CREATE_ALARMS_TABLE);		
		
		populate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	public void deleteDatabase() {
		mContext.deleteDatabase(DB_NAME);
	}
	
	
	private void populate(SQLiteDatabase db) {
		
		Log.d(App.DEBUG_TAG, "PatientDatabaseOpenHelper populate");
		
		
		db.delete(SymptomDataContract.CHECKINS_TABLE, null, null);
		db.delete(SymptomDataContract.MEDICATIONS_TABLE, null, null);
		db.delete(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, null);
		db.delete(SymptomDataContract.PATIENTS_TABLE, null, null);
		db.delete(SymptomDataContract.DOCTORS_TABLE, null, null);
		
		// POPULATE CHECKINS TABLE
		
		ContentValues values = new ContentValues();

		/*values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 09:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:30:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_NO);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();		
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 13:15:05");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_NO);	
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 17:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_NOT_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;2;3;4;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_SOME);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 21:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 20:30:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;2;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 09:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		
		
		// -- second patient
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 13:15:05");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_NO);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 17:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_NOT_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_SOME);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "2;3");
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 21:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 20:30:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;2;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);	
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 09:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN,SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		
		// 4rth patient		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-15 21:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 20:30:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;2;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 09:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
	*/
	 //5th patient
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-28 13:35:22");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_WELL_CONTROLLED );
		values.put(SymptomDataContract.MEDICATION_TAKEN,SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 20:30:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;2;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-28 16:35:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-28 21:00:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_WELL_CONTROLLED );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		

		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-29 09:00:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-29 11:30:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-29 14:00:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		

		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-29 17:50:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_MODERATE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-29 21:50:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, "2014-11-30 09:50:52");
		values.put(SymptomDataContract.PAIN_LEVEL, SymptomDataContract.ANSWER_PAIN_LEVEL_SEVERE );
		values.put(SymptomDataContract.MEDICATION_TAKEN, SymptomDataContract.ANSWER_MEDICATION_TAKEN);
		values.put(SymptomDataContract.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(SymptomDataContract.MEDICATION_ANSWERS, "1;3;5");
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, SymptomDataContract.ANSWER_STOPPED_EATING_YES);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_NOT_SYNCED);	
		db.insert(SymptomDataContract.CHECKINS_TABLE, null, values);
		values.clear();
		
		
		// POPULATE MEDICATIONS_TABLE TABLE		
		values.put(SymptomDataContract.MEDICATION_NAME, "Accupril");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Actonel");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Baygam");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Beconase-AQ");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Benzashave");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Caffeine Citrate");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Alfenta");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Asenapine Sublingual");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Basiliximab");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Calciferol");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Campath");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.MEDICATION_NAME, "Captopril");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		
		// POPULATE PATIENTS MEDICATIONS TABLE
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 5);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-15");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 6);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-21");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT );	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 8);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-21");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT );	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		//--------
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 5);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-15");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT );	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 6);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-21");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT );	
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		//----------
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 8);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-21");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-16 14:35:22");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 5);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-15");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-16 14:35:22");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED,SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 6);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-21");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-16 14:35:22");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.PATIENT_MEDICATION_ID, 3);
		values.put(SymptomDataContract.PATIENT_MEDICATION_FROM, "2014-10-24");
		values.put(SymptomDataContract.PATIENT_MEDICATION_TO, "9999-12-31");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-16 14:35:22");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_INSERT);
		values.put(SymptomDataContract.SYNCED,SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENT_MEDICATIONS_TABLE, null, values);
		values.clear();
		
		// POPULATE PATIENTS TABLE
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.DOCTOR_ID, 1);
		values.put(SymptomDataContract.PATIENT_NAME, "Teresa");
		values.put(SymptomDataContract.PATIENT_LAST_NAME, "Romero");
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, "1955-11-12");
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, 13);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, 18);
		values.put(SymptomDataContract.NO_EAT_MINUTES, 15);
		values.put(SymptomDataContract.PAIN_LEVEL, 3);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, 3);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENTS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1102);
		values.put(SymptomDataContract.DOCTOR_ID, 1);
		values.put(SymptomDataContract.PATIENT_NAME, "Maurice");
		values.put(SymptomDataContract.PATIENT_LAST_NAME, "Lucens");
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, "1947-10-22");
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, 4);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, 20);
		values.put(SymptomDataContract.NO_EAT_MINUTES, 5);
		values.put(SymptomDataContract.PAIN_LEVEL, 3);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, 3);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENTS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1103);
		values.put(SymptomDataContract.PATIENT_NAME, "Ralph");
		values.put(SymptomDataContract.DOCTOR_ID, 1);
		values.put(SymptomDataContract.PATIENT_LAST_NAME, "Sanchez");
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, "1962-01-22");
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, 0);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, 0);
		values.put(SymptomDataContract.NO_EAT_MINUTES, 0);
		values.put(SymptomDataContract.PAIN_LEVEL, 1);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, 1);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENTS_TABLE, null, values);
		values.clear();
		
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1104);
		values.put(SymptomDataContract.DOCTOR_ID, 2);
		values.put(SymptomDataContract.PATIENT_NAME, "Arthur");
		values.put(SymptomDataContract.PATIENT_LAST_NAME, "Moore");
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, "1945-03-05");
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, 1350);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, 1350);
		values.put(SymptomDataContract.NO_EAT_MINUTES, 0);
		values.put(SymptomDataContract.PAIN_LEVEL, 3);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, 3);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENTS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1105);
		values.put(SymptomDataContract.DOCTOR_ID, 2);
		values.put(SymptomDataContract.PATIENT_NAME, "John");
		values.put(SymptomDataContract.PATIENT_LAST_NAME, "Rhydal");
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, "1932-01-24");
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, 1200);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, 1800);
		values.put(SymptomDataContract.NO_EAT_MINUTES, 1500);
		values.put(SymptomDataContract.PAIN_LEVEL, 3);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, 3);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, "2014-10-16 13:35:22");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.PATIENTS_TABLE, null, values);
		values.clear();
		
		
		// POPULATE DOCTORS TABLE
		values.put(SymptomDataContract.USER_ID, 1);
		values.put(SymptomDataContract.DOCTOR_NAME, "Jim");
		values.put(SymptomDataContract.DOCTOR_LAST_NAME, "Adams");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.DOCTORS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.USER_ID, 2);
		values.put(SymptomDataContract.DOCTOR_NAME, "Alicia");
		values.put(SymptomDataContract.DOCTOR_LAST_NAME, "Keys");
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, "2014-11-25 13:35:22");
		values.put(SymptomDataContract.SERVER_TIMESTAMP, "2014-10-17 01:30:00");
		values.put(SymptomDataContract.SYNC_ACTION, SymptomDataContract.SYNC_UPDATE);
		values.put(SymptomDataContract.SYNCED, SymptomDataContract.STATE_SYNCED );	
		db.insert(SymptomDataContract.DOCTORS_TABLE, null, values);
		values.clear();
	
		
		//-- POPULATE ALARMS
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.ALARM_TIME, "10:00");
		values.put(SymptomDataContract.ALARM_ACTIVATED, 1);
		values.put(SymptomDataContract.ALARM_VIBRATE, 1);
		db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.ALARM_TIME, "14:00");
		values.put(SymptomDataContract.ALARM_ACTIVATED, 1);
		values.put(SymptomDataContract.ALARM_VIBRATE, 1);
		db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.ALARM_TIME, "18:00");
		values.put(SymptomDataContract.ALARM_ACTIVATED, 1);
		values.put(SymptomDataContract.ALARM_VIBRATE, 1);
		db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
		values.clear();
		
		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.ALARM_TIME, "22:00");
		values.put(SymptomDataContract.ALARM_ACTIVATED, 1);
		values.put(SymptomDataContract.ALARM_VIBRATE, 1);
		db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
		values.clear();

		values.put(SymptomDataContract.PATIENT_RECORD_ID, 1101);
		values.put(SymptomDataContract.ALARM_TIME, "24:00");
		values.put(SymptomDataContract.ALARM_ACTIVATED, 0);
		values.put(SymptomDataContract.ALARM_VIBRATE, 1);
		db.insert(SymptomDataContract.ALARMS_TABLE, null, values);
		values.clear();
		
		Log.d(App.DEBUG_TAG, "db for patient populated");
			
	}
	
}
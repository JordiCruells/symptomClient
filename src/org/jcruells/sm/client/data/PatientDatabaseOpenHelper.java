package org.jcruells.sm.client.data;


import org.jcruells.sm.client.App;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PatientDatabaseOpenHelper extends SQLiteOpenHelper {
	
	public final static String CHECKINS_TABLE = "checkins";
	public final static String _ID = "_id";
	public final static String CHECK_IN_DATETIME = "datetime";
	public final static String PAIN_LEVEL = "pain_level";
	public final static String MEDICATION_TAKEN = "medication_taken";
	public final static String MEDICATION_DATETIME = "medication_time";
	public final static String STOPPED_EATING = "stopped_eating";
	public final static String SYNCED = "synced";
	
	public final static String[] all_columns = { _ID, CHECK_IN_DATETIME, PAIN_LEVEL, MEDICATION_TAKEN, MEDICATION_DATETIME, STOPPED_EATING };
	public final static String[] checkins_list_columns = { _ID, CHECK_IN_DATETIME};
	
	public final static int VAL_MEDICATION_TAKEN = 1;
	public final static int VAL_MEDICATION_NOT_TAKEN = 1;
		
	
	final private static String CREATE_CMD =

	"CREATE TABLE " + CHECKINS_TABLE + " (" 
			+ _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CHECK_IN_DATETIME + " TEXT NOT NULL, "  
			+ PAIN_LEVEL + " INTEGER NOT NULL, "  
			+ MEDICATION_TAKEN + " INTEGER NOT NULL, "  
			+ MEDICATION_DATETIME + " TEXT NULL, " 
			+ STOPPED_EATING + " INTEGER NOT NULL, " 
			+ SYNCED + " INTEGER NOT NULL " 
			+ ")";

	final private static String DB_NAME = "sm_patient_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public PatientDatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		Log.d(App.DEBUG_TAG, "PatientDatabaseOpenHelper constructor");
		this.mContext = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(App.DEBUG_TAG, "PatientDatabaseOpenHelper onCreate");
		db.execSQL(CREATE_CMD);
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
		
		
		db.delete(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, null);
		
		ContentValues values = new ContentValues();

		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-15 09:35:22");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_MODERATE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_TAKEN);
		values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-15 09:30:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_NO);
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-15 13:15:05");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_SEVERE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_TAKEN);
		values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_NO);	
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-15 17:35:22");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_SEVERE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_NOT_TAKEN);
		//values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-15 13:10:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_SOME);
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-15 21:35:22");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_SEVERE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_TAKEN);
		values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-15 20:30:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_YES);	
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-16 09:35:22");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_SEVERE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_TAKEN);
		values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-15 09:32:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_YES);	
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		values.put(PatientDatabaseOpenHelper.CHECK_IN_DATETIME, "2014-10-16 13:35:22");
		values.put(PatientDatabaseOpenHelper.PAIN_LEVEL, CheckIn.PAIN_LEVEL_SEVERE );
		values.put(PatientDatabaseOpenHelper.MEDICATION_TAKEN, VAL_MEDICATION_TAKEN);
		values.put(PatientDatabaseOpenHelper.MEDICATION_DATETIME, "2014-10-16 13:31:00");
		values.put(PatientDatabaseOpenHelper.STOPPED_EATING, CheckIn.STOPPED_EATING_YES);
		values.put(PatientDatabaseOpenHelper.SYNCED, CheckIn.NOT_SYNCED);	
		db.insert(PatientDatabaseOpenHelper.CHECKINS_TABLE, null, values);
		values.clear();
		
		Log.d(App.DEBUG_TAG, "db for patient populated");
			
	}
	
}
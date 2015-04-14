package org.jcruells.sm.client.data;

import android.content.ContentResolver;
import android.net.Uri;

public final class SymptomDataContract {


	public static final Uri BASE_URI = Uri.parse("content://com.symptom.patient.provider/");
	public static final String BASE_URI_STRING = "com.symptom.patient.provider";
	
	
	/*------------------------------------------------------------------*/
	/* CHECKINS                                                         */
	/*------------------------------------------------------------------*/
		
	public static final int ANSWER_PAIN_LEVEL_WELL_CONTROLLED = 1;
	public static final int ANSWER_PAIN_LEVEL_MODERATE = 2;
	public static final int ANSWER_PAIN_LEVEL_SEVERE = 3;
	
	public static final int ANSWER_STOPPED_EATING_NO = 1;
	public static final int ANSWER_STOPPED_EATING_SOME = 2;
	public static final int ANSWER_STOPPED_EATING_YES = 3;
	
	public final static int ANSWER_MEDICATION_TAKEN = 1;
	public final static int ANSWER_MEDICATION_NOT_TAKEN = 1;
	
	

	public final static String CHECKINS_TABLE = "checkins";
	public final static String ID = "_id";
	public final static String PATIENT_RECORD_ID = "patient_id";
	public final static String CHECK_IN_DATETIME = "datetime";
	public final static String PAIN_LEVEL = "pain_level";
	public final static String MEDICATION_TAKEN = "medication_taken";
	public final static String MEDICATION_DATETIME = "medication_time";
	public final static String MEDICATION_ANSWERS = "medication_answers";
	public final static String STOPPED_EATING_LEVEL = "stopped_eating_level";
	public final static String SERVER_TIMESTAMP = "server_timestamp";
	public final static String SYNC_ACTION = "sync_action";
		public final static int SYNC_INSERT = 1;	
		public final static int SYNC_UPDATE = 2;
	public final static String SYNCED = "synced";
		public final static int STATE_NOT_SYNCED = 0;
		public final static int STATE_SYNCED = 1;
	
	public final static String PAIN_MINUTES = "pain_minutes";
		
	public final static String[] CHECKINS_ALL_COLUMNS = { ID, PATIENT_RECORD_ID, CHECK_IN_DATETIME, PAIN_LEVEL, MEDICATION_TAKEN, 
		                          MEDICATION_DATETIME, MEDICATION_ANSWERS, STOPPED_EATING_LEVEL, SERVER_TIMESTAMP, SYNC_ACTION, SYNCED };
	
	// The URI for the checkins table.
	public static final Uri CHECKINS_URI = Uri.withAppendedPath(BASE_URI, CHECKINS_TABLE);

	private static final String CHECKINS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + CHECKINS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_CHECKINS = ContentResolver.CURSOR_DIR_BASE_TYPE + CHECKINS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_CHECKIN = ContentResolver.CURSOR_ITEM_BASE_TYPE + CHECKINS_SUBTYPE;


	/*------------------------------------------------------------------*/
	/* MEDICATIONS                                                      */
	/*------------------------------------------------------------------*/
	
	public final static String MEDICATIONS_TABLE = "medications";
	public final static String MEDICATION_NAME = "medication_name";
	
	public final static String[] MEDICATION_ALL_COLUMNS = {ID, MEDICATION_NAME, SERVER_TIMESTAMP, SYNC_ACTION, SYNCED };
	
	// The URI for the checkins table.
	public static final Uri MEDICATIONS_URI = Uri.withAppendedPath(BASE_URI, MEDICATIONS_TABLE);

	private static final String MEDICATIONS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + MEDICATIONS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_MEDICATIONS = ContentResolver.CURSOR_DIR_BASE_TYPE + MEDICATIONS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_MEDICATION = ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDICATIONS_SUBTYPE;
	
	
	/*------------------------------------------------------------------*/
	/* PATIENT_MEDICATIONS                                                      */
	/*------------------------------------------------------------------*/
	
	public final static String PATIENT_MEDICATIONS_TABLE = "patient_medications";
	public final static String PATIENT_MEDICATION_ID = "medication_id";
	public final static String PATIENT_MEDICATION_FROM = "medication_from";
	public final static String PATIENT_MEDICATION_TO = "medication_to";
	
	public final static String[] PATIENT_MEDICATION_ALL_COLUMNS = { ID, PATIENT_RECORD_ID, PATIENT_MEDICATION_ID, PATIENT_MEDICATION_FROM, PATIENT_MEDICATION_TO, SERVER_TIMESTAMP, SYNC_ACTION, SYNCED };
	public final static String[] PATIENT_MEDICATION_WITH_NAME_ALL_COLUMNS = { ID, PATIENT_RECORD_ID, PATIENT_MEDICATION_ID, PATIENT_MEDICATION_FROM, PATIENT_MEDICATION_TO, MEDICATION_NAME, SERVER_TIMESTAMP, SYNC_ACTION, SYNCED };
	
	// The URI for the checkins table.
	public static final Uri PATIENT_MEDICATIONS_URI = Uri.withAppendedPath(BASE_URI, PATIENT_MEDICATIONS_TABLE);
	public static final Uri PATIENT_MEDICATIONS_DOCTOR_URI = Uri.withAppendedPath(PATIENT_MEDICATIONS_URI, "doctor");
	
	private static final String PATIENT_MEDICATIONS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + PATIENT_MEDICATIONS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_PATIENT_MEDICATIONS = ContentResolver.CURSOR_DIR_BASE_TYPE + PATIENT_MEDICATIONS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_PATIENT_MEDICATION = ContentResolver.CURSOR_ITEM_BASE_TYPE + PATIENT_MEDICATIONS_SUBTYPE;
	
	/*------------------------------------------------------------------*/
	/* PATIENTS                                       */
	/*------------------------------------------------------------------*/

	public final static String PATIENTS_TABLE = "patients";
	public final static String PATIENT_NAME = "patient_name";
	public final static String DOCTOR_ID = "doctor_id";
	public final static String PATIENT_LAST_NAME = "patient_last_name";
	public final static String PATIENT_BIRTHDAY = "birthday";
	public final static String SEVERE_PAIN_MINUTES = "severe_pain_minutes";
	public final static String MODERATE_TO_SEVERE_PAIN_MINUTES = "moderate_severe_pain_minutes";
	public final static String NO_EAT_MINUTES = "no_eat_minutes";
	public final static String LAST_CHECKIN_DATETIME = "last_checkin_datetime";
	public final static String CLIENT_LAST_SYNC_TIMESTAMP = "client_last_sync_timestamp";
	
	public final static String[] PATIENT_ALL_COLUMNS = {ID, PATIENT_RECORD_ID, DOCTOR_ID, PATIENT_NAME, PATIENT_LAST_NAME, PATIENT_BIRTHDAY, 
														SEVERE_PAIN_MINUTES, MODERATE_TO_SEVERE_PAIN_MINUTES, NO_EAT_MINUTES,
														PAIN_LEVEL, STOPPED_EATING_LEVEL, LAST_CHECKIN_DATETIME, CLIENT_LAST_SYNC_TIMESTAMP, 
														SERVER_TIMESTAMP,SYNC_ACTION, SYNCED };
	
	// The URI for the checkins table.
	public static final Uri PATIENTS_URI = Uri.withAppendedPath(BASE_URI, PATIENTS_TABLE);

	private static final String PATIENTS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + PATIENTS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_PATIENTS = ContentResolver.CURSOR_DIR_BASE_TYPE + PATIENTS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_PATIENT = ContentResolver.CURSOR_ITEM_BASE_TYPE + PATIENTS_SUBTYPE;
	
	/*------------------------------------------------------------------*/
	/* DOCTORS                                       */
	/*------------------------------------------------------------------*/

	public final static String DOCTORS_TABLE = "doctors";
	public final static String USER_ID = "user_id";
	public final static String DOCTOR_NAME = "doctor_name";
	public final static String DOCTOR_LAST_NAME = "doctor_last_name";
	
	public final static String[] DOCTOR_ALL_COLUMNS = {ID, USER_ID, DOCTOR_NAME, DOCTOR_LAST_NAME, CLIENT_LAST_SYNC_TIMESTAMP,  
													   SERVER_TIMESTAMP,SYNC_ACTION, SYNCED };
	
	// The URI for the checkins table.
	public static final Uri DOCTORS_URI = Uri.withAppendedPath(BASE_URI, DOCTORS_TABLE);

	private static final String DOCTORS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + DOCTORS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_DOCTORSS = ContentResolver.CURSOR_DIR_BASE_TYPE + DOCTORS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_DOCTOR = ContentResolver.CURSOR_ITEM_BASE_TYPE + DOCTORS_SUBTYPE;
	
	
	/*------------------------------------------------------------------*/
	/* ALARMS                                       */
	/*------------------------------------------------------------------*/
	
	public final static String ALARMS_TABLE = "alarms";
	public final static String ALARM_TIME = "alarm_time";
	public final static String ALARM_ACTIVATED = "alarm_activated";
	public final static String ALARM_VIBRATE = "alarm_vibrate";
	
	
	public final static String[] ALARM_ALL_COLUMNS = {ID, PATIENT_RECORD_ID, ALARM_TIME, ALARM_ACTIVATED, ALARM_VIBRATE };
	
	// The URI for the checkins table.
	public static final Uri ALARMS_URI = Uri.withAppendedPath(BASE_URI, ALARMS_TABLE);

	private static final String ALARMS_SUBTYPE = "/vnd." + BASE_URI_STRING + "." + ALARMS_TABLE;
	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE_ALARMS = ContentResolver.CURSOR_DIR_BASE_TYPE + ALARMS_SUBTYPE;
	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE_ALARM = ContentResolver.CURSOR_ITEM_BASE_TYPE + ALARMS_SUBTYPE;
	
}
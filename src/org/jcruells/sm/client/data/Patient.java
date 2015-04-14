package org.jcruells.sm.client.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




import org.jcruells.sm.client.App;

import android.content.ContentValues;
import android.util.Log;

public class Patient implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static final int PAIN_LEVEL_WELL_CONTROLLED = 1;
	public static final int PAIN_LEVEL_MODERATE = 2;
	public static final int PAIN_LEVEL_SEVERE = 3;
	
	public static final int STOPPED_EATING_NO = 1;
	public static final int STOPPED_EATING_SOME = 2;
	public static final int STOPPED_EATING_YES = 3;
	

	private long id;
	private int recordId;
	private long doctorId;
	private String patientName;
	private String patientLastName;
	private String patientBirthday;
	private int severePainMinutes;
	private int moderateToSeverePainMinutes;
	private int noEatMinutes;
	private int painLevel;
	private int stoppedEatingLevel;
	private String lastCheckinDatetime;
	private String clientLastSyncTimestamp;
	private String serverTimestamp;
	private int syncAction;
	private int synced;
	

	
	public Patient(long id, int recordId, long doctorId, String patientName, String patientLastName, String patientBirthday,
			int severePainMinutes, int moderateToSeverePainMinutes, int noEatMinutes, int painLevel,
			int stoppedEatingLevel, String lastCheckinDatetime, String clientLastSyncTimestamp, String serverTimestamp,
			int syncAction, int synced) {
		super();
		this.id = id;
		this.recordId = recordId;
		this.doctorId = doctorId;
		this.patientName = patientName;
		this.patientLastName = patientLastName;
		this.patientBirthday = patientBirthday;
		this.severePainMinutes = severePainMinutes;
		this.moderateToSeverePainMinutes = moderateToSeverePainMinutes;
		this.noEatMinutes = noEatMinutes;
		this.painLevel = painLevel;
		this.stoppedEatingLevel = stoppedEatingLevel;
		this.lastCheckinDatetime = lastCheckinDatetime;
		this.clientLastSyncTimestamp = clientLastSyncTimestamp;
		this.serverTimestamp = serverTimestamp;
		this.syncAction = syncAction;
		this.synced = synced;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientLastName() {
		return patientLastName;
	}
	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}
	public String getPatientBirthday() {
		return patientBirthday;
	}
	public void setPatientBirthday(String patientBirthday) {
		this.patientBirthday = patientBirthday;
	}
	public int getSeverePainMinutes() {
		return severePainMinutes;
	}
	public void setSeverePainMinutes(int severePainMinutes) {
		this.severePainMinutes = severePainMinutes;
	}
	public int getModerateToSeverePainMinutes() {
		return moderateToSeverePainMinutes;
	}
	public void setModerateToSeverePainMinutes(int moderateToSeverePainMinutes) {
		this.moderateToSeverePainMinutes = moderateToSeverePainMinutes;
	}
	public int getNoEatMinutes() {
		return noEatMinutes;
	}
	public void setNoEatMinutes(int noEatMinutes) {
		this.noEatMinutes = noEatMinutes;
	}
	public int getPainLevel() {
		return painLevel;
	}
	public void setPainLevel(int painLevel) {
		this.painLevel = painLevel;
	}
	public int getStoppedEatingLevel() {
		return stoppedEatingLevel;
	}
	public void setStoppedEatingLevel(int stoppedEatingLevel) {
		this.stoppedEatingLevel = stoppedEatingLevel;
	}
	public String getLastCheckinDatetime() {
		return lastCheckinDatetime;
	}
	public void setLastCheckinDatetime(String lastCheckinDatetime) {
		this.lastCheckinDatetime = lastCheckinDatetime;
	}
	public String getClientLastSyncTimestamp() {
		return clientLastSyncTimestamp;
	}
	public void setClientLastSyncTimestamp(String clientLastSyncTimestamp) {
		this.clientLastSyncTimestamp = clientLastSyncTimestamp;
	}
	public String getServerTimestamp() {
		return serverTimestamp;
	}
	public void setServerTimestamp(String serverTimestamp) {
		this.serverTimestamp = serverTimestamp;
	}
	public int getSyncAction() {
		return syncAction;
	}
	public void setSyncAction(int syncAction) {
		this.syncAction = syncAction;
	}
	public int getSynced() {
		return synced;
	}
	public void setSynced(int synced) {
		this.synced = synced;
	}
	
	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}
	
	public int getAge() {
		
		Log.d(App.DEBUG_TAG, "get AGE");
		int age;
		Calendar today = Calendar.getInstance();
		Date dateBorn = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateBorn = sdf.parse(getPatientBirthday());
		} 
		catch(ParseException e) {
			Log.d(App.DEBUG_TAG, "Error while parsinf date at Patient.getAge");
		}
		Calendar born = Calendar.getInstance();
		born.setTime(dateBorn);
		
		age = today.get(Calendar.YEAR) - born.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < born.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == born.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < born.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}
		
		Log.d(App.DEBUG_TAG, "RETURN AGE");
		return age;
		
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.ID, id);
		values.put(SymptomDataContract.PATIENT_RECORD_ID, recordId);
		values.put(SymptomDataContract.PATIENT_NAME, patientName);
		values.put(SymptomDataContract.PATIENT_LAST_NAME, patientLastName);
		values.put(SymptomDataContract.PATIENT_BIRTHDAY, patientBirthday);
		values.put(SymptomDataContract.SEVERE_PAIN_MINUTES, severePainMinutes);
		values.put(SymptomDataContract.MODERATE_TO_SEVERE_PAIN_MINUTES, moderateToSeverePainMinutes);
		values.put(SymptomDataContract.NO_EAT_MINUTES, noEatMinutes);
		values.put(SymptomDataContract.PAIN_LEVEL, painLevel);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, stoppedEatingLevel);
		values.put(SymptomDataContract.LAST_CHECKIN_DATETIME, lastCheckinDatetime);
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, clientLastSyncTimestamp);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, serverTimestamp);
		values.put(SymptomDataContract.SYNC_ACTION, syncAction);
		values.put(SymptomDataContract.SYNCED, synced);
		return values;
	}


	
}


	
	

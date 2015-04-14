package org.jcruells.sm.client.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

public class CheckIn implements Serializable {
	

	//@SerializedName("id")
	private long id;	
	//@SerializedName("patientRecordId")
	private int patientRecordId;
	//@SerializedName("checkinDatetime")
	private String checkinDatetime;
	//@SerializedName("painLevel")
	private int painLevel;
	//@SerializedName("medicationTaken")
	private int medicationTaken;
	//@SerializedName("medicationDatetime")
	private String medicationDatetime;
	//@SerializedName("medicationAnswers")
	private String medicationAnswers;
	//@SerializedName("stoppedEatingLevel")
	private int stoppedEatingLevel;
	//@SerializedName("serverTimestamp")
	private String serverTimestamp;	
	//@SerializedName("syncAction")
	private int syncAction;	
	//@SerializedName("synced")
	private int synced;
	
	public ArrayList<Integer> getMedicationAnswersArrayList() {

		ArrayList<Integer> list = new ArrayList<Integer>();
		
		if (medicationAnswers.length() > 0) {
			String[] ints = medicationAnswers.split(";");
			int i;
			for (i=0; i<ints.length;i++ ) {
				list.add(Integer.valueOf(ints[i]));
			}
		}
		
		return list;

	}
	
	public CheckIn() {}

	public CheckIn(long id, Integer patientRecordId, String checkinDatetime, Integer painLevel, Integer medicationTaken,
			String medicationDatetime, String medicationAnswers, Integer stoppedEatingLevel, String serverTimestamp,
			Integer syncAction, Integer synced) {
		super();
		this.id = id;
		this.patientRecordId = patientRecordId;
		this.checkinDatetime = checkinDatetime;
		this.painLevel = painLevel;
		this.medicationTaken = medicationTaken;
		this.medicationDatetime = medicationDatetime;
		this.medicationAnswers = medicationAnswers;
		this.stoppedEatingLevel = stoppedEatingLevel;
		this.serverTimestamp = serverTimestamp;
		this.syncAction = syncAction;
		this.synced = synced;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		System.out.println("set id");
		this.id = id;
	}

	public Integer getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(Integer patientRecordId) {
		System.out.println("set patient id");
		this.patientRecordId = patientRecordId;
	}

	public String getCheckinDatetime() {
		
		return checkinDatetime;
	}

	public void setCheckinDatetime(String checkinDatetime) {
		System.out.println("set patient checkin datetime");
		this.checkinDatetime = checkinDatetime;
	}

	public Integer getPainLevel() {
		System.out.println("set painllevel");
		return painLevel;
	}

	public void setPainLevel(Integer painLevel) {
		this.painLevel = painLevel;
	}

	public Integer getMedicationTaken() {
		return medicationTaken;
	}

	public void setMedicationTaken(Integer medicationTaken) {
		System.out.println("set medication taken");
		this.medicationTaken = medicationTaken;
	}

	public String getMedicationDatetime() {
		return medicationDatetime;
	}

	public void setMedicationDatetime(String medicationDatetime) {
		System.out.println("set medication deteime");
		this.medicationDatetime = medicationDatetime;
	}

	public String getMedicationAnswers() {
		return medicationAnswers;
	}

	public void setMedicationAnswers(String medicationAnswers) {
		System.out.println("set medication answers");
		this.medicationAnswers = medicationAnswers;
	}

	public Integer getStoppedEatingLevel() {
		return stoppedEatingLevel;
	}

	public void setStoppedEatingLevel(Integer stoppedEatingLevel) {
		System.out.println("set stopped eating");
		this.stoppedEatingLevel = stoppedEatingLevel;
	}

	public String getServerTimestamp() {
		return serverTimestamp;
	}

	public void setServerTimestamp(String serverTimestamp) {
		System.out.println("set server timestamp");
		this.serverTimestamp = serverTimestamp;
	}

	public Integer getSyncAction() {
		return syncAction;
	}

	public void setSyncAction(Integer syncAction) {
		System.out.println("set sync action");
		this.syncAction = syncAction;
	}

	public Integer getSynced() {
		return synced;
	}

	public void setSynced(Integer synced) {
		System.out.println("set synced");
		this.synced = synced;
	}

	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(id);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CheckIn) {
			CheckIn other = (CheckIn) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(id, other.id);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return " this.id " +  id +
		"this.patientRecordId " +  patientRecordId +
		"this.checkinDatetime " + checkinDatetime +
		"this.painLevel " +  painLevel +
		"this.medicationTaken " + medicationTaken +
		"this.medicationDatetime " + medicationDatetime +
		"this.medicationAnswers " +  medicationAnswers +
		"this.stoppedEatingLevel " + stoppedEatingLevel +
		"this.serverTimestamp " + serverTimestamp + 
		"this.syncAction " +  syncAction +
		"this.synced " + synced;
	}

	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.ID, id);
		values.put(SymptomDataContract.PATIENT_RECORD_ID, patientRecordId);
		values.put(SymptomDataContract.CHECK_IN_DATETIME, checkinDatetime);
		values.put(SymptomDataContract.PAIN_LEVEL, painLevel);
		values.put(SymptomDataContract.MEDICATION_TAKEN, medicationTaken);		
		values.put(SymptomDataContract.MEDICATION_DATETIME, medicationDatetime);
		values.put(SymptomDataContract.MEDICATION_ANSWERS, medicationAnswers);
		values.put(SymptomDataContract.STOPPED_EATING_LEVEL, stoppedEatingLevel);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, serverTimestamp);
		values.put(SymptomDataContract.SYNC_ACTION, syncAction);
		values.put(SymptomDataContract.SYNCED, synced);
		return values;
	}
	
}

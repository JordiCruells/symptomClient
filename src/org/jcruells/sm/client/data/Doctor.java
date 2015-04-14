package org.jcruells.sm.client.data;

import java.io.Serializable;
import android.content.ContentValues;

public class Doctor implements Serializable {

	/**
	 * 
	 */
	private long id;
	private long userId;
	private String doctorName;
	private String doctorLastName;
	private String clientLastSyncTimestamp;
	private String serverTimestamp;
	private int syncAction;
	private int synced;
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.ID, id);
		values.put(SymptomDataContract.USER_ID, userId);
		values.put(SymptomDataContract.DOCTOR_NAME, doctorName);
		values.put(SymptomDataContract.DOCTOR_LAST_NAME, doctorLastName);
		values.put(SymptomDataContract.CLIENT_LAST_SYNC_TIMESTAMP, clientLastSyncTimestamp);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, serverTimestamp);
		values.put(SymptomDataContract.SYNC_ACTION, syncAction);
		values.put(SymptomDataContract.SYNCED, synced);
		return values;
	}
	public Doctor() {}
	public Doctor(long id, long userId, String doctorName, String doctorLastName, String clientLastSyncTimestamp,
			String serverTimestamp, int syncAction, int synced) {
		super();
		this.id = id;
		this.userId = userId;
		this.doctorName = doctorName;
		this.doctorLastName = doctorLastName;
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorLastName() {
		return doctorLastName;
	}
	public void setDoctorLastName(String doctorLastName) {
		this.doctorLastName = doctorLastName;
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


	
}


	
	

package org.jcruells.sm.client.data;

import android.content.ContentValues;


public class Medication {

	
	private long id;
	private String name;
	private String serverTimestamp;
	private int syncAction;
	private int synced;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Medication(long id, String name, String serverTimestamp, int syncAction, int synced) {
		super();
		this.id = id;
		this.name = name;
		this.serverTimestamp = serverTimestamp;
		this.syncAction = syncAction;
		this.synced = synced;
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(SymptomDataContract.ID, id);
		values.put(SymptomDataContract.MEDICATION_NAME, name);
		values.put(SymptomDataContract.SERVER_TIMESTAMP, serverTimestamp);
		values.put(SymptomDataContract.SYNC_ACTION, syncAction);
		values.put(SymptomDataContract.SYNCED, synced);
		return values;
	}
	
	
	
	
	

}

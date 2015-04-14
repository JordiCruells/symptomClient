package org.jcruells.sm.client.data;

import java.io.Serializable;

public class Alarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final int ALARM_ACTIVATED = 1;
	public static final int ALARM_DEACTIVATED = 0;
	
	public static final int ALARM_VIBRATE = 1;
	public static final int ALARM_NO_VIBRATE = 0;
	
	
	private long  id;
	private int recordId;
	private String alarmTime;
	private int activated;
	private int vibrate;
	
	public long getId() {
		return id;
	}
	public void set_id(long id) {
		this.id = id;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public int getActivated() {
		return activated;
	}
	public void setActivated(int activated) {
		this.activated = activated;
	}
	public Alarm(long id, int recordId, String alarmTime, int activated, int vibrate) {
		super();
		this.id = id;
		this.recordId = recordId;
		this.alarmTime = alarmTime;
		this.activated = activated;
		this.vibrate = vibrate;
	}
	public int getVibrate() {
		return vibrate;
	}
	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}
	
	

	
}

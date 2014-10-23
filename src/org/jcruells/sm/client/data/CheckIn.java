package org.jcruells.sm.client.data;

import java.util.Collection;
import java.util.Date;

public class CheckIn {
	
	
	public static final Integer PAIN_LEVEL_WELL_CONTROLLED = 0;
	public static final Integer PAIN_LEVEL_MODERATE = 1;
	public static final Integer PAIN_LEVEL_SEVERE = 2;
	
	public static final Integer STOPPED_EATING_NO = 0;
	public static final Integer STOPPED_EATING_SOME = 1;
	public static final Integer STOPPED_EATING_YES = 2;
	public static final Integer NOT_SYNCED = 0;
	public static final Integer SYNCED = 1;
	
	
	private Integer _id;
	private Date datetime;
	private Integer painLevel;
	private boolean medicationTaken;
	private Date medicationDatetime;
	private Integer stoppedEating;
	private Collection<MedicationQuestion> medicationQuestions;
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public Integer getPainLevel() {
		return painLevel;
	}
	public void setPainLevel(Integer painLevel) {
		this.painLevel = painLevel;
	}
	public boolean isMedicationTaken() {
		return medicationTaken;
	}
	public void setMedicationTaken(boolean medicationTaken) {
		this.medicationTaken = medicationTaken;
	}
	public Date getMedicationDatetime() {
		return medicationDatetime;
	}
	public void setMedicationDatetime(Date medicationDatetime) {
		this.medicationDatetime = medicationDatetime;
	}
	public Integer getStoppedEating() {
		return stoppedEating;
	}
	public void setStoppedEating(Integer stoppedEating) {
		this.stoppedEating = stoppedEating;
	}
	public Collection<MedicationQuestion> getMedicationQuestions() {
		return medicationQuestions;
	}
	public void setMedicationQuestions(Collection<MedicationQuestion> medicationQuestions) {
		this.medicationQuestions = medicationQuestions;
	}
	
	

}

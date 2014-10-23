package org.jcruells.sm.client.data;

public class MedicationQuestion {
	
	private Integer idMedication;
	private String nameMedication;
	private boolean answer;
	public Integer getIdMedication() {
		return idMedication;
	}
	public void setIdMedication(Integer idMedication) {
		this.idMedication = idMedication;
	}
	public String getNameMedication() {
		return nameMedication;
	}
	public void setNameMedication(String nameMedication) {
		this.nameMedication = nameMedication;
	}
	public boolean isAnswer() {
		return answer;
	}
	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
	
	

}

package org.jcruells.sm.client;

import java.util.Collection;

import org.jcruells.sm.client.data.PatientMedication;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MedicationSvcApi {
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String MEDICATIONS_SVC_PATH = "/medications";
	
	
	@POST(MEDICATIONS_SVC_PATH)
	public Void updatePatientMedications(@Body Collection<PatientMedication> patientMedications);
	
	@GET(MEDICATIONS_SVC_PATH)
	public Collection<PatientMedication> getNewMedications(@Query("fromTimestamp") String fromTimestamp);
	
}

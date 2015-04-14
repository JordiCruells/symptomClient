package org.jcruells.sm.client;

import java.util.Collection;

import org.jcruells.sm.client.data.Patient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface PatientSvcApi {
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String PATIENTS_SVC_PATH = "/patients";
	public static final String PATIENT_BY_ID_SVC_PATH = PATIENTS_SVC_PATH + "/{id}";
	
	
	@POST(PATIENTS_SVC_PATH)
	public Void updatePatient(@Body Patient patient);
	
	@GET(PATIENT_BY_ID_SVC_PATH)
	public Patient getPatient(@Path("id") int id );
	
	@GET(PATIENTS_SVC_PATH)
	public Collection<Patient> getUpdatedPatients(@Query("fromTimestamp") String fromTimestamp);
	
	
	
	
}

package org.jcruells.sm.client;

import org.jcruells.sm.client.data.Doctor;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


public interface DoctorSvcApi {
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String DOCTORS_SVC_PATH = "/doctors";
	public static final String DOCTOR_BY_ID_SVC_PATH = DOCTORS_SVC_PATH + "/{id}";
	
	
	@POST(DOCTORS_SVC_PATH)
	public Void updateDoctor(@Body Doctor doctor);
	
	@GET(DOCTOR_BY_ID_SVC_PATH)
	public Doctor getDoctor(@Path("id") int id );
	
	
	
	
	
	
}

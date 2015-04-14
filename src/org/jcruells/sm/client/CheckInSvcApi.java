package org.jcruells.sm.client;

import java.util.Collection;

import org.jcruells.sm.client.data.CheckIn;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface CheckInSvcApi {
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TOKEN_PATH = "/oauth/token";
	
	
	// The path where we expect the check-ins service to live
	public static final String CHECKINS_SVC_PATH = "/checkins";
		
	@POST(CHECKINS_SVC_PATH)
	public Void addNewCheckIns(@Body Collection<CheckIn> checkins);
	
	@GET(CHECKINS_SVC_PATH)
	public Collection<CheckIn> getNewCheckIns(@Query("fromTimestamp") String fromTimestamp);
	
	
	
}

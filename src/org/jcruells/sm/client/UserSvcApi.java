package org.jcruells.sm.client;


import org.jcruells.sm.client.data.User;

import retrofit.http.GET;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface UserSvcApi {
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	
	// The path where we expect the User Data Service to live
	public static final String USER_SVC_PATH = "/user";
	
	
	@GET(USER_SVC_PATH)
	public User getUserData();
	
	
}

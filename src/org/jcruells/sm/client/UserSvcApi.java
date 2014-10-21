package org.jcruells.sm.client;

import retrofit.http.GET;

/**
 * This interface defines an API for a UserSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 *
 */
public interface UserSvcApi {
	
	// The path where we expect the UserSvc to live
	public static final String USER_SVC_PATH = "/user";

	
	@GET(USER_SVC_PATH)
	public User getUserData();
	
	
	/*@GET(USER_SVC_PATH + "/{userId}")
	public User getUserByUserId(@Path("userId") long userId);*/
	
}

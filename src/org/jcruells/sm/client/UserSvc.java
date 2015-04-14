/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.jcruells.sm.client;

import org.jcruells.sm.client.oauth.SecuredRestBuilder;
import org.jcruells.sm.client.unsafe.EasyHttpClient;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UserSvc {

	public static final String CLIENT_ID = "mobile";

	private static UserSvcApi userSvc_;

	public static synchronized UserSvcApi getOrShowLogin(Context ctx) {
		
		Log.d(App.DEBUG_TAG, "SymptomSvc getOrShowLogin");
		
		
		if (userSvc_ != null) {
			return userSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized UserSvcApi init(String user,
			String pass, App app) {

		Log.d(App.DEBUG_TAG, "symptom Svc init");
				
		userSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(App.SERVER + UserSvcApi.TOKEN_PATH)
				.setUserDataEndpoint(App.SERVER + UserSvcApi.USER_SVC_PATH)
				.setApp(app)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(App.SERVER).setLogLevel(LogLevel.FULL).build()
				.create(UserSvcApi.class);

		return userSvc_;
	}


}

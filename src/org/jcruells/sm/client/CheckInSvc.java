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

public class CheckInSvc {

	public static final String CLIENT_ID = "mobile";

	private static CheckInSvcApi checkInSvc_;

	public static synchronized CheckInSvcApi getOrShowLogin(Context ctx) {
		
		Log.d(App.DEBUG_TAG, "SymptomSvc getOrShowLogin");
				
		if (checkInSvc_ != null) {
			return checkInSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized CheckInSvcApi init(App app) {

		Log.d(App.DEBUG_TAG, "check in Svc init");
				
		checkInSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(App.SERVER + CheckInSvcApi.TOKEN_PATH)
				.setUserDataEndpoint(App.SERVER + UserSvcApi.USER_SVC_PATH)
				.setApp(app)
				.setUsername(app.getUser().getUsername())
				.setPassword(app.getUser().getPassword())
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(App.SERVER).setLogLevel(LogLevel.FULL).build()
				.create(CheckInSvcApi.class);

		return checkInSvc_;
	}


}

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
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class VideoSvc {

	public static final String CLIENT_ID = "mobile";

	private static VideoSvcApi videoSvc_;

	public static synchronized VideoSvcApi getOrShowLogin(Context ctx) {
		
		Log.d(App.DEBUG_TAG, "VideoSvc getOrShowLogin");
		
		
		if (videoSvc_ != null) {
			return videoSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized VideoSvcApi init(String server, String user,
			String pass, Application app) {

		Log.d(App.DEBUG_TAG, "VideoSvc init");
				
		videoSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + VideoSvcApi.TOKEN_PATH)
				.setUserDataEndpoint(server + UserSvcApi.USER_SVC_PATH)
				.setContext(app)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(VideoSvcApi.class);

		return videoSvc_;
	}
}

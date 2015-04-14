package org.jcruells.sm.client;

import org.jcruells.sm.client.oauth.SecuredRestBuilder;
import org.jcruells.sm.client.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DoctorSvc {

	public static final String CLIENT_ID = "mobile";

	private static DoctorSvcApi doctorSvc_;

	public static synchronized DoctorSvcApi getOrShowLogin(Context ctx) {
		
		Log.d(App.DEBUG_TAG, "SymptomSvc getOrShowLogin");
				
		if (doctorSvc_ != null) {
			return doctorSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized DoctorSvcApi init(App app) {

		Log.d(App.DEBUG_TAG, "check in Svc init");
				
		doctorSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(App.SERVER + DoctorSvcApi.TOKEN_PATH)
				.setUserDataEndpoint(App.SERVER + UserSvcApi.USER_SVC_PATH)
				.setApp(app)
				.setUsername(app.getUser().getUsername())
				.setPassword(app.getUser().getPassword())
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(App.SERVER).setLogLevel(LogLevel.FULL).build()
				.create(DoctorSvcApi.class);

		return doctorSvc_;
	}


}

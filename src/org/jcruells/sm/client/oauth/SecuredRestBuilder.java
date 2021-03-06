/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.jcruells.sm.client.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.io.IOUtils;
import org.jcruells.sm.client.App;
import org.jcruells.sm.client.data.User;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Log;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.mime.FormUrlEncodedTypedOutput;
import android.app.Application;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;



/**
 * A Builder class for a Retrofit REST Adapter. Extends the default implementation by providing logic to
 * handle an OAuth 2.0 password grant login flow. The RestAdapter that it produces uses an interceptor
 * to automatically obtain a bearer token from the authorization server and insert it into all client
 * requests.
 * 
 * You can use it like this:
 * 
  	private VideoSvcApi videoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);
 * 
 * @author Jules, Mitchell
 *
 */
public class SecuredRestBuilder extends RestAdapter.Builder {

	private class OAuthHandler implements RequestInterceptor {

		private Client client;
		private String tokenIssuingEndpoint;
		private String userDataEndpoint;
		private App app;
		private String username;
		private String password;
		private String clientId;
		private String clientSecret;
		private String accessToken;

		public OAuthHandler(Client client, String tokenIssuingEndpoint, String userDataEndpoint, App app, String username,
				String password, String clientId, String clientSecret) {
			super();
			this.client = client;
			this.tokenIssuingEndpoint = tokenIssuingEndpoint;
			this.userDataEndpoint =userDataEndpoint;
			this.app = app;
			this.username = username;
			this.password = password;
			this.clientId = clientId;
			this.clientSecret = clientSecret;
		}

		/**
		 * Every time a method on the client interface is invoked, this method is
		 * going to get called. The method checks if the client has previously obtained
		 * an OAuth 2.0 bearer token. If not, the method obtains the bearer token by
		 * sending a password grant request to the server. 
		 * 
		 * Once this method has obtained a bearer token, all future invocations will
		 * automatically insert the bearer token as the "Authorization" header in 
		 * outgoing HTTP requests.
		 * 
		 */
		@Override
		public void intercept(RequestFacade request) {
			

			android.util.Log.d(App.DEBUG_TAG, "SecuredRestBuilder intercept");
			
			android.util.Log.d(App.DEBUG_TAG, "logged in: " + app.isUserLoggedIn());
			
			
			// If we're not logged in, login and store the authentication token.
			if (!app.isUserLoggedIn()) {
				
				android.util.Log.d(App.DEBUG_TAG, "in if intercept");
				
				try {
					// This code below programmatically builds an OAuth 2.0 password
					// grant request and sends it to the server. 
					
					// Encode the username and password into the body of the request.
					FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
					to.addField("username", username);
					to.addField("password", password);
					
					// Add the client ID and client secret to the body of the request.
					to.addField("client_id", clientId);
					to.addField("client_secret", clientSecret);
					
					// Indicate that we're using the OAuth Password Grant Flow
					// by adding grant_type=password to the body
					to.addField("grant_type", "password");
					
					// The password grant requires BASIC authentication of the client.
					// In order to do BASIC authentication, we need to concatenate the
					// client_id and client_secret values together with a colon and then
					// Base64 encode them. The final value is added to the request as
					// the "Authorization" header and the value is set to "Basic " 
					// concatenated with the Base64 client_id:client_secret value described
					// above.
					String base64Auth = BaseEncoding.base64().encode(new String(clientId + ":" + clientSecret).getBytes());
					// Add the basic authorization header
					List<Header> headers = new ArrayList<Header>();
					headers.add(new Header("Authorization", "Basic " + base64Auth));

					// Create the actual password grant request using the data above
					Request req = new Request("POST", tokenIssuingEndpoint, headers, to);
					
					// Request the password grant.
					Response resp = client.execute(req);
					
					android.util.Log.d(App.DEBUG_TAG, "after client execute");
					
					// Make sure the server responded with 200 OK
					if (resp.getStatus() < 200 || resp.getStatus() > 299) {
						
						android.util.Log.d(App.DEBUG_TAG, "bad credentials 1");
						app.setUserLoggedIn(false);
						// If not, we probably have bad credentials
						throw new SecuredRestException("Login failure: "
								+ resp.getStatus() + " - " + resp.getReason());
						
					} else {
						// Extract the string body from the response
						
						
				        String body = IOUtils.toString(resp.getBody().in());
						
				        android.util.Log.d(App.DEBUG_TAG, "BODY:" + body);
				        
						// Extract the access_token (bearer token) from the response so that we
				        // can add it to future requests.
				        
				        
						accessToken = new Gson().fromJson(body, JsonObject.class).get("access_token").getAsString();
						
						app.setToken(accessToken);
						
						//Add the login to request the user's data whenever a successful login is done
						List<Header> lh = new ArrayList<Header>();
						lh.add(new Header("Authorization", "Bearer " + accessToken));
						
						android.util.Log.d(App.DEBUG_TAG, "call to user data");
						
						Request reqUserData = new Request("GET", userDataEndpoint,lh, null);
						Response respUserData = client.execute(reqUserData);
						
						System.out.println("status:" + respUserData.getStatus());
						
						// Make sure the server responded with 200 OK
						if (respUserData.getStatus() < 200 || respUserData.getStatus() > 299) {
							// Something went wrong retrieving the user's data
							throw new SecuredRestException("Failure when retrieving user's data: "
									+ respUserData.getStatus() + " - " + respUserData.getReason());
							
							
						} else {
							
							android.util.Log.d(App.DEBUG_TAG, "call to user data ok");
							
							// Obtain user from message body and store it the application object (context)
							body = IOUtils.toString(respUserData.getBody().in());
							
							android.util.Log.d(App.DEBUG_TAG, "BODY:" + body);
							
							Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
							User user = (User) (gson.fromJson(body, User.class));
							
							android.util.Log.d(App.DEBUG_TAG, "username: " + username);
							android.util.Log.d(App.DEBUG_TAG, "password: " + password);
							
							android.util.Log.d(App.DEBUG_TAG, "user saved to application");
							android.util.Log.d(App.DEBUG_TAG, "-------------------------------");
							
							android.util.Log.d(App.DEBUG_TAG, "user ID: " + user.getId());
							/*android.util.Log.d(App.DEBUG_TAG, "getBirthDate: " + user.getBirthDate());
							android.util.Log.d(App.DEBUG_TAG, "getLastName: " + user.getLastName());
							android.util.Log.d(App.DEBUG_TAG, "getName: " + user.getName());*/
							android.util.Log.d(App.DEBUG_TAG, "getRecordNumber: " + user.getRecordNumber());
							android.util.Log.d(App.DEBUG_TAG, "getPassword: " + user.getPassword());
							android.util.Log.d(App.DEBUG_TAG, "getUsername: " + user.getUsername());
							
							app.setUser(user);
							
							android.util.Log.d(App.DEBUG_TAG, "app user ID: " + app.getUser().getId());
							
							android.util.Log.d(App.DEBUG_TAG, "user is:" + user);
							
							// Add the access_token to this request as the "Authorization"
							// header.
							request.addHeader("Authorization", "Bearer " + accessToken);	
							
							// Let future calls know we've already fetched the access token
							app.setUserLoggedIn(true);
						}
							
					}
				} catch (Exception e) {
					throw new SecuredRestException(e);
				}
			}
			else {
				// Add the access_token that we previously obtained to this request as 
				// the "Authorization" header.
				request.addHeader("Authorization", "Bearer " + app.getToken());
			}
		}

	}

	private String username;
	private String password;
	private String loginUrl;
	private App app;
	private String userDataUrl;
	private String clientId;
	private String clientSecret = "";
	private Client client;
	
	public SecuredRestBuilder setLoginEndpoint(String endpoint){
		loginUrl = endpoint;
		return this;
	}

	public SecuredRestBuilder setUserDataEndpoint(String endpoint){
		userDataUrl = endpoint;
		return this;
	}
	
	public SecuredRestBuilder setApp(App app){
		this.app= app;
		return this;
	}
	
	@Override
	public SecuredRestBuilder setEndpoint(String endpoint) {
		return (SecuredRestBuilder) super.setEndpoint(endpoint);
	}

	@Override
	public SecuredRestBuilder setEndpoint(Endpoint endpoint) {
		return (SecuredRestBuilder) super.setEndpoint(endpoint);
	}

	@Override
	public SecuredRestBuilder setClient(Client client) {
		this.client = client;
		return (SecuredRestBuilder) super.setClient(client);
	}

	@Override
	public SecuredRestBuilder setClient(Provider clientProvider) {
		client = clientProvider.get();
		return (SecuredRestBuilder) super.setClient(clientProvider);
	}

	@Override
	public SecuredRestBuilder setErrorHandler(ErrorHandler errorHandler) {

		return (SecuredRestBuilder) super.setErrorHandler(errorHandler);
	}

	@Override
	public SecuredRestBuilder setExecutors(Executor httpExecutor,
			Executor callbackExecutor) {

		return (SecuredRestBuilder) super.setExecutors(httpExecutor,
				callbackExecutor);
	}

	@Override
	public SecuredRestBuilder setRequestInterceptor(
			RequestInterceptor requestInterceptor) {

		return (SecuredRestBuilder) super
				.setRequestInterceptor(requestInterceptor);
	}

	@Override
	public SecuredRestBuilder setConverter(Converter converter) {

		return (SecuredRestBuilder) super.setConverter(converter);
	}

	@Override
	public SecuredRestBuilder setProfiler(@SuppressWarnings("rawtypes") Profiler profiler) {

		return (SecuredRestBuilder) super.setProfiler(profiler);
	}

	@Override
	public SecuredRestBuilder setLog(Log log) {

		return (SecuredRestBuilder) super.setLog(log);
	}

	@Override
	public SecuredRestBuilder setLogLevel(LogLevel logLevel) {

		return (SecuredRestBuilder) super.setLogLevel(logLevel);
	}

	public SecuredRestBuilder setUsername(String username) {
		this.username = username;
		return this;
	}

	public SecuredRestBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public SecuredRestBuilder setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}
	
	public SecuredRestBuilder setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}
	
	
	
		

	@Override
	public RestAdapter build() {
		if (username == null || password == null) {
			throw new SecuredRestException(
					"You must specify both a username and password for a "
							+ "SecuredRestBuilder before calling the build() method.");
		}

		if (client == null) {
			client = new OkClient();
		}
		OAuthHandler hdlr = new OAuthHandler(client, loginUrl, userDataUrl, app, username, password, clientId, clientSecret);
		setRequestInterceptor(hdlr);

		return super.build();
	}
}
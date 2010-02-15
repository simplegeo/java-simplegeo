/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;


import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

//import android.util.Log;
import org.apache.log4j.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * @author Derek Smith
 *
 */
public class OAuthHttpClient extends DefaultHttpClient {
	
	private static String TAG = OAuthHttpClient.class.getSimpleName();
	private static Logger logger = Logger.getLogger(OAuthHttpClient.class);
	
	private OAuthConsumer token;
	
	public OAuthHttpClient() {
		this(null, null);
	}
	
	public OAuthHttpClient(String key, String secret) {
		setToken(key, secret);
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		this.setParams(params);
		
	}
		
	public String getKey() {
		return token.getConsumerKey();
	}

	public String getSecret() {
		return token.getTokenSecret();
	}
	
	public void setToken(String key, String secret) {
		if(key != null && secret != null) {
			
			token  = new CommonsHttpOAuthConsumer(key, secret);
			
			if(token == null)
				logger.debug(String.format("failure to create OAuth token %s,%s", key, secret));
			else
				logger.debug(String.format("token was created with %s,%s", key, secret));
		}
	}
	
	public Object executeOAuthRequest(HttpUriRequest request, ResponseHandler<Object> responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException {
		
		this.token.sign(request);
		
		return super.execute(request, responseHandler);
	}
}

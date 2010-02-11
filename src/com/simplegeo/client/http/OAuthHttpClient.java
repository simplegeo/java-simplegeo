/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;


import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.SignatureMethod;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.util.Log;


/**
 * @author dsmith
 *
 */
public class OAuthHttpClient extends DefaultHttpClient {
	
	// TODO: Save and delete a cached copy of key/secret
	
	private static String TAG = OAuthHttpClient.class.getSimpleName();
	private OAuthConsumer token;
	
	public OAuthHttpClient() {
		this(null, null);
	}
	
	public OAuthHttpClient(String key, String secret) {
		setToken(key, secret);
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		this.setParams(params);
		
//		this.addRequestInterceptor(new PrintRequestInterceptor());
	}
		
	public String getKey() {
		return token.getConsumerKey();
	}

	public String getSecret() {
		return token.getTokenSecret();
	}
	
	public void setToken(String key, String secret) {
		if(key != null && secret != null) {
			
			token  = new CommonsHttpOAuthConsumer(key, secret, SignatureMethod.HMAC_SHA1);
			
			if(token == null)
				Log.e(TAG, String.format("failure to create OAuth token %s,%s", key, secret));
			else
				Log.d(TAG, String.format("token was created with %s,%s", key, secret));
		}
	}
	
	public Object executeOAuthRequest(HttpUriRequest request, ResponseHandler<Object> responseHandler) 
		throws OAuthMessageSignerException, OAuthExpectationFailedException, ClientProtocolException, IOException {
		
		this.token.sign(request);
		
		if(Log.isLoggable(TAG, Log.DEBUG))			
			Log.d(TAG, String.format("sending %s to %s", request.getMethod(), request.getURI().getPath()));
		
		return super.execute(request, responseHandler);
	}
	
	private class PrintRequestInterceptor implements HttpRequestInterceptor {

		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			
			HttpParams httpParams = new BasicHttpParams();
			
			Header[] headers = request.getAllHeaders();
			for(Header header : headers) {
				String name = header.getName();
				if(name.equals("Authorization")) {
					
					httpParams.setParameter(name, header.getValue());
					
					break;
				}
			}
			
			request.setParams(httpParams);
		}
		
		
		
	}
}

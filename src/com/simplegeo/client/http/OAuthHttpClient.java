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
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * A subclass of {@link org.apache.http.impl.client.DefaultHttpClient}
 * that signs requests using a given OAuth key and secret.
 * 
 * @author Derek Smith
 */
public class OAuthHttpClient extends DefaultHttpClient {
	
	private static String TAG = OAuthHttpClient.class.getSimpleName();
	private static Logger logger = Logger.getLogger(OAuthHttpClient.class);
	
	private OAuthConsumer token = null;
			
	/**
	 * @param connManager
	 * @param params
	 */
	public OAuthHttpClient(ThreadSafeClientConnManager connManager,
			HttpParams params) {
		super(connManager, params);
	}

	/**
	 * Returns the consumer key that is used to sign Http requests.
	 * 
	 * @return the consumer key
	 */
	public String getKey() {
		
		return token.getConsumerKey();
		
	}

	/**
	 * Returns the consumer secret that is used to sign Http requests.
	 * 
	 * @return the consumer secret
	 */
	public String getSecret() {
		
		return token.getTokenSecret();
		
	}
	
	/**
	 * Set the key/secret pair that will be used to sign Http
	 * requests.
	 * 
	 * @param key the consumer key
	 * @param secret the secret key
	 */
	public void setToken(String key, String secret) {
		
		if(key != null && secret != null) {
			
			token  = new CommonsHttpOAuthConsumer(key, secret);
			
			if(token == null)
				logger.debug(String.format("failure to create OAuth token %s,%s", key, secret));
			else
				logger.debug(String.format("token was created with %s,%s", key, secret));
		}
		
	}
	
	/**
	 * Signs the Http request with the registered token before
	 * execution.
	 * 
	 * @param request the request that will be sent
	 * @param responseHandler the handler that will be used on a successful
	 * response
	 * @return an Object that was created by the handler
	 * @throws OAuthMessageSignerException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object executeOAuthRequest(HttpUriRequest request, ResponseHandler<Object> responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException {
		
		this.token.sign(request);
		
		return super.execute(request, responseHandler);
	}
}

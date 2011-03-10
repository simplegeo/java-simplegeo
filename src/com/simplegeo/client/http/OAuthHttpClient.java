/**
 * Copyright (c) 2010-2011, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simplegeo.client.http;

import java.io.IOException;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

/**
 * A subclass of {@link org.apache.http.impl.client.DefaultHttpClient}
 * that signs requests using a given OAuth key and secret.
 * 
 * @author Derek Smith
 */
public class OAuthHttpClient extends DefaultHttpClient implements OAuthClient {
	
	private static Logger logger = Logger.getLogger(OAuthHttpClient.class.getName());
	
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
				logger.info(String.format("Failed to created OAuth token."));
			else
				logger.info(String.format("Successfully created OAuth token."));
		}
		
	}
	
	/**
	 * Signs the Http request with the registered token before
	 * execution.
	 * 
	 * @param urlString The url that the request should be sent to.
	 * @param type The type of request that should be sent.
	 * @param jsonPayload The json string that should be sent along with POSTs and PUTs.
	 * @param responseHandler the handler that will be used on a successful
	 * response
	 * @return an Object that was created by the handler
	 * @throws OAuthMessageSignerException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object executeOAuthRequest(String urlString, HttpRequestMethod method, String jsonPayload, ResponseHandler<Object> responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException {
		HttpUriRequest request = this.buildRequest(urlString, method, jsonPayload);
		logger.info(String.format("sending %s", request.toString()));
		this.token.sign(request);
		return super.execute(request, responseHandler);
	}
	
	/**
	 * Factory method that builds a HttpUriRequest based on the information passed in.
	 * 
	 * @param urlString The url that the request should be sent to.
	 * @param type The type of request that should be sent.
	 * @param jsonPayload The json string that should be sent along with POSTs and PUTs.
	 * @return HttpUriRequest Either a HttpGet, HttpPost, HttpPut or HttpDelete
	 */
	private HttpUriRequest buildRequest(String urlString, HttpRequestMethod method, String jsonPayload) {
		switch (method) {
			case GET:
				HttpGet requestGet = new HttpGet(urlString);
				return requestGet;
			case POST:
				HttpPost requestPost = new HttpPost(urlString);
				requestPost.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
				requestPost.addHeader("Content-type", "application/json");
				return requestPost;
			case PUT:
				HttpPut requestPut = new HttpPut(urlString);
				requestPut.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
				requestPut.addHeader("Content-type", "application/json");
				return requestPut;
			case DELETE:
				HttpDelete requestDelete = new HttpDelete(urlString);
				return requestDelete;
			default:
				return null;
		}
	}
}

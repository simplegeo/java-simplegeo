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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

/**
 * A subclass of {@link org.apache.http.impl.client.DefaultHttpClient}
 * that signs requests using a given OAuth key and secret.
 * 
 * @author Casey Crites
 */
public class OAuthHttpClient implements OAuthClient {
	
	private static Logger logger = Logger.getLogger(OAuthHttpClient.class.getName());
	
	private OAuthConsumer token = null;
			
	public OAuthHttpClient() {
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
			
			token  = new DefaultOAuthConsumer(key, secret);
			
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
	public Object executeOAuthRequest(String urlString, HttpRequestMethod method, String jsonPayload, SimpleGeoHandler responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
		switch (method) {
			case GET:
				connection.setRequestMethod("GET");
				this.token.sign(connection);
				break;
			case POST:
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestMethod("POST");
				this.token.sign(connection);
				OutputStream outputPost = connection.getOutputStream();
				outputPost.write(jsonPayload.getBytes("UTF-8"));
				outputPost.close();
				break;
			case PUT:
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestMethod("PUT");
				this.token.sign(connection);
				OutputStream outputPut = connection.getOutputStream();
				outputPut.write(jsonPayload.getBytes("UTF-8"));
				outputPut.close();
				break;
			case DELETE:
				connection.setRequestMethod("DELETE");
				this.token.sign(connection);
				break;
			default:
				return null;
		}
		
		InputStream response = connection.getInputStream();
		int responseCode = ((HttpURLConnection) connection).getResponseCode();
		return responseHandler.handleResponse(response, responseCode);
	}
}

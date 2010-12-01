/**
 * Copyright (c) 2009-2010, SimpleGeo
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

package com.simplegeo.client.urlconn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.simplegeo.client.AbstractSimpleGeoClient;
import com.simplegeo.client.ISimpleGeoClient;
import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.NoSuchEntityException;
import com.simplegeo.client.http.exceptions.NotAuthorizedException;

/**
 * A wrapper around a URLConnection designed to hide the of the request generation from the rest of the
 * REST code.  This enables all of the REST logic to be implemented in AbstractSimpleGeoClient and the derived
 * class is responsible only for the transport.
 * 
 * @author Mark Fogle
 */

public class SimpleGeoURLConnClient extends AbstractSimpleGeoClient {
	
	/* Status codes */
	public static final int GET_SUCCESS = 200;
	public static final int PUT_SUCCESS = 202;
	public static final int BAD_REQUEST = 400;
	public static final int NO_SUCH = 404;
	public static final int NOT_AUTHORIZED = 401;
	
	protected OAuthURLConnection urlConn = null;
	
	/**
	 * @return the shared instance of this class
	 */
	static public ISimpleGeoClient getInstance() {
		
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoURLConnClient();
		
		return sharedLocationService;
	}
	
	private SimpleGeoURLConnClient() {
		
		super();

		this.urlConn = new OAuthURLConnection();
	}

	/**
	 * @return the Http client used to execute all requests
	 */
	public IOAuthClient getHttpClient() {
		return this.urlConn;
	}
	
	protected Object execute(String uri, String request, String payload, ISimpleGeoJSONHandler handler)
									throws IOException {
		
		logger.info(String.format("sending %s", request.toString()));
		
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod(request);
		conn.setDoInput(true);
		try {
			urlConn.sign(conn);
		} catch (OAuthMessageSignerException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthExpectationFailedException e) {
			dealWithAuthorizationException(e);	
		} catch (OAuthCommunicationException e) {
			dealWithAuthorizationException(e);
		};
		
		if (payload != null)
		{
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", 
		       "application/x-www-form-urlencoded");
		      DataOutputStream wr = new DataOutputStream (
		              conn.getOutputStream ());
		      wr.writeBytes (payload);
		      wr.flush ();
		      wr.close ();
		}
		
		switch(conn.getResponseCode()) {
			case GET_SUCCESS:
			case PUT_SUCCESS:
				//
				// Fall through and continue
				//
				break;
			case BAD_REQUEST:
				throw new APIException (conn.getResponseCode(), null);
			case NO_SUCH:
				throw new NoSuchEntityException(conn.getResponseCode(), null);
			case NOT_AUTHORIZED:
				throw new NotAuthorizedException (conn.getResponseCode(), null);
			default:
				throw new APIException (conn.getResponseCode(), null);
		
		}

		//Get Response	
		InputStream inputStream = conn.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		StringBuffer response = new StringBuffer(); 
		while((line = bufferedReader.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		bufferedReader.close();
  
	    return handler.parseResponse(response.toString());
	}
	
	@Override
	protected Object executeGet(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		return execute(uri, "GET", null, handler);
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		
		return execute(uri, "POST", jsonPayload, handler);
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {

		return execute(uri, "DELETE", null, handler);
	}
}

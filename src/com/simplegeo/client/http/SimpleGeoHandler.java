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
package com.simplegeo.client.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

//import android.util.Log;
import org.apache.log4j.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;



import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.NoSuchRecordException;
import com.simplegeo.client.http.exceptions.NotAuthorizedException;

/**
 * A handler used to parse requests sent to http://api.simplegeo.com.
 * 
 * @author Derek Smith
 */
public class SimpleGeoHandler implements ResponseHandler<Object> {
	
	static private String TAG = SimpleGeoHandler.class.getCanonicalName();
	private static Logger logger = Logger.getLogger(SimpleGeoHandler.class);
	
	/* Status codes */
	public static final int GET_SUCCESS = 200;
	public static final int PUT_SUCCESS = 202;
	public static final int BAD_REQUEST = 400;
	public static final int NO_SUCH = 404;
	public static final int NOT_AUTHORIZED = 401;
	
	/* (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		logger.debug("recieved response " + response);

		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		HttpEntity entity = response.getEntity();

		HttpResponse validResponse = null;
		switch(statusCode) {
		
			case GET_SUCCESS:
			case PUT_SUCCESS:
				validResponse = response; 
				break;
			case BAD_REQUEST:
				throw APIException.createException(entity, statusLine);
			case NO_SUCH:
				throw NoSuchRecordException.createException(entity, statusLine);
			case NOT_AUTHORIZED:
				throw NotAuthorizedException.createException(entity, statusLine);
			default:
				throw APIException.createException(entity, statusLine);
		
		}
	
		return validResponse;
	}
}

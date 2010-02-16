/**
 * Copyright 2010 SimpleGeo. All rights reserved.
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

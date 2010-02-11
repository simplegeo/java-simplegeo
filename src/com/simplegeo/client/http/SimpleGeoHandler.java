/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import android.util.Log;

import com.simplegeo.android.sdk.http.exceptions.APIException;
import com.simplegeo.android.sdk.http.exceptions.NoSuchRecordException;
import com.simplegeo.android.sdk.http.exceptions.NotAuthorizedException;

/**
 * @author dsmith
 *
 */
public class SimpleGeoHandler implements ResponseHandler<Object> {
	
	static private String TAG = SimpleGeoHandler.class.getCanonicalName();
	
	/* Status codes */
	public static final int GET_SUCCESS = 200;
	public static final int PUT_SUCCESS = 202;
	public static final int BAD_REQUEST = 400;
	public static final int NO_SUCH = 404;
	public static final int NOT_AUTHORIZED = 401;
	
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		Log.d(TAG, "recieved response " + response);

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
				throw new APIException(entity, statusLine);
			case NO_SUCH:
				throw new NoSuchRecordException(entity, statusLine);
			case NOT_AUTHORIZED:
				throw new NotAuthorizedException(entity, statusLine);
			default:
				throw new APIException(entity, statusLine);
		
		}
	
		return validResponse;
	}
}

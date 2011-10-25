package com.simplegeo.client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.NoSuchEntityException;
import com.simplegeo.client.http.exceptions.NotAuthorizedException;

/**
 * A handler used to parse requests sent to http://api.simplegeo.com.
 * 
 * @author Derek Smith
 */
@SuppressWarnings("unused")
public class SimpleGeoHandler {
	private static Logger logger = Logger.getLogger(SimpleGeoHandler.class.getName());
	
	/* Status codes */
	public static final int GET_SUCCESS = 200;
	public static final int PUT_SUCCESS = 202;
	public static final int POST_SUCCESS = 301;
	public static final int BAD_REQUEST = 400;
	public static final int NO_SUCH = 404;
	public static final int NOT_AUTHORIZED = 401;
	
	public SimpleGeoHandler() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public String handleResponse(InputStream response, int statusCode) throws ClientProtocolException, IOException {

		String jsonString = "";

		if (response != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
				}
			} finally {
				response.close();
			}
			jsonString = writer.toString();
		}

		switch(statusCode) {
			case GET_SUCCESS:
			case POST_SUCCESS:
			case PUT_SUCCESS:
				break;
			case BAD_REQUEST:
				throw new APIException(statusCode, jsonString);
			case NO_SUCH:
				throw new NoSuchEntityException(statusCode, jsonString);
			case NOT_AUTHORIZED:
				throw new NotAuthorizedException(statusCode, jsonString);
			default:
				throw new APIException(statusCode, jsonString);
		}
		return jsonString;
	}
}

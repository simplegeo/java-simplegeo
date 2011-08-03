package com.simplegeo.client.http;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.NoSuchEntityException;
import com.simplegeo.client.http.exceptions.NotAuthorizedException;

/**
 * A handler used to parse requests sent to http://api.simplegeo.com.
 * 
 * @author Derek Smith
 */
public class SimpleGeoHandler implements ResponseHandler<String> {
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
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

		logger.info("received response " + response);

		int statusCode = response.getStatusLine().getStatusCode();

		switch(statusCode) {
			case GET_SUCCESS:
			case POST_SUCCESS:
			case PUT_SUCCESS:
				break;
			case BAD_REQUEST:
				throw APIException.createException(response.getEntity(), response.getStatusLine());
			case NO_SUCH:
				throw NoSuchEntityException.createException(response.getEntity(), response.getStatusLine());
			case NOT_AUTHORIZED:
				throw NotAuthorizedException.createException(response.getEntity(), response.getStatusLine());
			default:
				throw APIException.createException(response.getEntity(), response.getStatusLine());
		}
		return EntityUtils.toString(response.getEntity());
	}
}

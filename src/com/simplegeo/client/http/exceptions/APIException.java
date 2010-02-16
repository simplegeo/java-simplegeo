package com.simplegeo.client.http.exceptions;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

//import android.util.Log;
import org.apache.log4j.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

/**
 * An exception class that can be built from the components of
 * a {@link org.apache.http.HttpResponse}.
 * 
 * @author dsmith
 */
@SuppressWarnings("serial")
public class APIException extends ClientProtocolException {

	private static String TAG = APIException.class.getName();
	private static Logger logger = Logger.getLogger(APIException.class);
	
	/**
	 * The Http status code that generated the exception.
	 */
	public int statusCode;
	
	/**
	 * A static factory method that creates proper API exceptions from 
	 * a {@link org.apache.http.HttpEntity} and {@link org.apache.http.StatusLine}.
	 * The message is built using a combination of both the status code and the payload.
	 * 
	 * @param entity the entity retrieved from a Http response
	 * @param statusLine the {@link org.apache.http.StatusLine} that was retrieved
	 * from a Http response
	 * @return a new APIException object
	 */
	public static APIException createException(HttpEntity entity, StatusLine statusLine) {
		
		int statusCode = statusLine.getStatusCode();
		String reason = null;
		
		try {
			
			InputStream inputStream = entity.getContent();
			DataInputStream dis = new DataInputStream(inputStream);
			reason = dis.readUTF();
			
		} catch (EOFException e) {
			
			;
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(reason == null)
			reason = statusLine.getReasonPhrase();
	
		logger.debug(String.format("(status %d) %s", statusCode, reason));
		
		return new APIException(statusCode, reason);
	}
		
	/**
	 * Creates an exception with the given status code and message. 
	 * 
	 * @param statusCode the Http status code that generated the exception
	 * @param reason the reason why the this exception is being created
	 */
	public APIException(int statusCode, String reason) {
		
		super(reason);
		this.statusCode = statusCode;
		
		logger.debug(String.format("(status %d) %s", statusCode, reason));
	}

}

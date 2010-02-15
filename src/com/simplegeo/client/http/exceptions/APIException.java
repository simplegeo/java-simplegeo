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

@SuppressWarnings("serial")
public class APIException extends ClientProtocolException {

	private static String TAG = APIException.class.getName();
	private static Logger logger = Logger.getLogger(APIException.class);
	
	public int statusCode;
	
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
		
	public APIException(int statusCode, String reason) {
		
		super(reason);
		this.statusCode = statusCode;
		
		logger.debug(String.format("(status %d) %s", statusCode, reason));
	}

}

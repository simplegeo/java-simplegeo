package com.simplegeo.client.http.exceptions;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import android.util.Log;

@SuppressWarnings("serial")
public class APIException extends ClientProtocolException {

	private static String TAG = APIException.class.getName();
	
	public int statusCode;
	public String reason;
	
	public APIException(HttpEntity entity, StatusLine statusLine) {
		
		this.statusCode = statusLine.getStatusCode();
		
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
		
		
		Log.e(TAG, String.format("(status %d) %s", statusCode, reason));
	}
	
	public APIException(int statusCode, String reason) {
		
		this.statusCode = statusCode;
		this.reason = reason;
		
		Log.e(TAG, String.format("(status %d) %s", statusCode, reason));
	}

}

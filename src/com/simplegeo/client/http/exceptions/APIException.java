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
 * @author Derek Smith
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

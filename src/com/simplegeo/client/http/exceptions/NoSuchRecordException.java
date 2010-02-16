/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http.exceptions;

/**
 * An exception that is created when an Http response contains a status
 * code of 404.
 * 
 * @author Derek Smith
 */
@SuppressWarnings("serial")
public class NoSuchRecordException extends APIException {

	public NoSuchRecordException(int statusCode, String reason) {
		super(statusCode, reason);
	}

}

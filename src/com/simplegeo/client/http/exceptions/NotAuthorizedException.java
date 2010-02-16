/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http.exceptions;

import com.simplegeo.client.http.exceptions.APIException;

/**
 * An exception that is created when an Http response contains a status
 * code of 401.
 * 
 * @author Derek Smith
 */
@SuppressWarnings("serial")
public class NotAuthorizedException extends APIException {

	public NotAuthorizedException(int statusCode, String reason) {
		super(statusCode, reason);
	}

}

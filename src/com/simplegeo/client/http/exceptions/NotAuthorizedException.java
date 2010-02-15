/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http.exceptions;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

import com.simplegeo.client.http.exceptions.APIException;

/**
 * @author dsmith
 *
 */
@SuppressWarnings("serial")
public class NotAuthorizedException extends APIException {

	public NotAuthorizedException(int statusCode, String reason) {
		super(statusCode, reason);
	}


}

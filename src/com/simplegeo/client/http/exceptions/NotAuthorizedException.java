/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http.exceptions;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

import com.simplegeo.android.sdk.http.exceptions.APIException;

/**
 * @author dsmith
 *
 */
@SuppressWarnings("serial")
public class NotAuthorizedException extends APIException {

	/**
	 * @param entity
	 * @param statusLine
	 */
	public NotAuthorizedException(HttpEntity entity, StatusLine statusLine) {
		super(entity, statusLine);
	}

}

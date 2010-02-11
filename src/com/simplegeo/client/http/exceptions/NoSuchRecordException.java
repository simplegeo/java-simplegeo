/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http.exceptions;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

/**
 * @author dsmith
 *
 */
@SuppressWarnings("serial")
public class NoSuchRecordException extends APIException {

	/**
	 * @param entity
	 * @param statusLine
	 */
	public NoSuchRecordException(HttpEntity entity, StatusLine statusLine) {
		super(entity, statusLine);
	}

}

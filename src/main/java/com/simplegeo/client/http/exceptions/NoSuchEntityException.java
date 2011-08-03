package com.simplegeo.client.http.exceptions;

/**
 * An exception that is created when an Http response contains a status
 * code of 404.
 * 
 * @author Derek Smith
 */
@SuppressWarnings("serial")
public class NoSuchEntityException extends APIException {

	public NoSuchEntityException(int statusCode, String reason) {
		super(statusCode, reason);
	}

}

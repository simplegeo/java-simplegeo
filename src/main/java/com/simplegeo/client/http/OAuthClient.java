package com.simplegeo.client.http;

import java.io.IOException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simplegeo.client.SimpleGeoClient.HttpRequestMethod;

/**
 * An interface that defines the only method of the OAuth clients that is used outside of the 
 * response handler; the method that specifies the key & secret.  This is needed because the client
 * that is based off of the URLConnection needs to use a different OAuth client.
 * 
 * @author Mark Fogle
 */

public interface OAuthClient {

	/**
	 * Set the key/secret pair that will be used to sign Http
	 * requests.
	 * 
	 * @param key the consumer key
	 * @param secret the secret key
	 */
	public void setToken(String key, String secret);
	
	/**
	 * Execute an OAuth request.
	 * 
	 * @param urlString
	 * @param method
	 * @param jsonPayload
	 * @param responseHandler
	 * @return Object
	 * @throws OAuthMessageSignerException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String executeOAuthRequest(String urlString, HttpRequestMethod method, String jsonPayload, ResponseHandler<String> responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException;
	
}

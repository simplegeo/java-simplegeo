/**
 * Copyright (c) 2010-2011, SimpleGeo
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
	 * @return
	 * @throws OAuthMessageSignerException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object executeOAuthRequest(String urlString, HttpRequestMethod method, String jsonPayload, ResponseHandler<Object> responseHandler) 
		throws OAuthMessageSignerException, OAuthCommunicationException, OAuthExpectationFailedException, ClientProtocolException, IOException;
	
}

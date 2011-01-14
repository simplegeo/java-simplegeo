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

package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.simplegeo.client.callbacks.ISimpleGeoCallback;
import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;

/**
 * Extracts as much common code as possible between the SimpleGeoPlacesClient and the SimpleGeoContextClient.
 * 
 * @author Casey Crites
 */

public abstract class AbstractSimpleGeoClient implements ISimpleGeoClient {
	
	private RequestThreadPoolExecutor threadExecutor;
	protected OAuthHttpClient httpClient;
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());
	
	protected String baseUrl = "http://api.simplegeo.com";
	protected String port = "80";
	protected String apiVersion = "1.0";
	public HashMap<String, String> endpoints = new HashMap<String, String>();
	
	/**
	 * Main constructor class for setting up the client that the specific Places/Context clients
	 * extend from.
	 * @param baseUrl String - Default is http://api.simplegeo.com, but this can be overridden
	 * @param port String - Default is 80, but this can be overridden
	 * @param apiVersion - Default is 1.0, but this can be overridden
	 */
	protected AbstractSimpleGeoClient(String baseUrl, String port, String apiVersion) {
		this.baseUrl = baseUrl == "" ? DEFAULT_HOST : baseUrl;
		this.port = port == "" ? DEFAULT_PORT : port;
		this.apiVersion = apiVersion == "" ? DEFAULT_VERSION : apiVersion;
		
		// We want to make sure the client is threadsafe
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);

		this.httpClient = new OAuthHttpClient(connManager, params);
		this.threadExecutor = new RequestThreadPoolExecutor("SimpleGeoClient");
	}
	
	/**
	 * Grab the desired endpoint and add it to the server, port and version.
	 * @param endpointName
	 * @return String A URL pointing at the desired server
	 */
	protected String getEndpoint(String endpointName) {
		return String.format("%s:%s/%s/%s", baseUrl, port, apiVersion, endpoints.get(endpointName));
	}
	
	/**
	 * Method for executing HttpRequests synchronously.
	 * @param request HttpUriRequest
	 * @param handler {@link com.simplegeo.client.http.SimpleGeoHandler} to call back when the request completes.
	 * It will then in turn hand off to an instance of  {@link com.simplegeo.client.handler.ISimpleGeoHandler}
	 * @return Either a {@link com.simplegeo.client.types.Feature}, {@link com.simplegeo.client.types.FeatureCollection}
	 * or a regular HashMap<Sring, Object>.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected Object execute(HttpUriRequest request, SimpleGeoHandler handler)
		throws ClientProtocolException, IOException {

		logger.info(String.format("sending %s", request.toString()));
	
		Object object = null;
		try {
			object = httpClient.executeOAuthRequest(request, handler);
		} catch (OAuthMessageSignerException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthExpectationFailedException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthCommunicationException e) {
			dealWithAuthorizationException(e);
		};

		return object;

	}
	
	/**
	 * Method for executing HttpRequests asynchronously.
	 * @param request HttpUriRequest
	 * @param handler {@link com.simplegeo.client.http.SimpleGeoHandler} to call back when the request completes.
	 * It will then in turn hand off to an instance of  {@link com.simplegeo.client.handler.ISimpleGeoHandler}
	 * @param callback ISimpleGeoCallback Any object implementing the ISimpleGeoCallback interface
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected void execute(HttpUriRequest request, SimpleGeoHandler handler, ISimpleGeoCallback callback)
		throws ClientProtocolException, IOException {

		final HttpUriRequest finalRequest = request;
		final SimpleGeoHandler finalHandler = handler;
		final ISimpleGeoCallback finalCallback = callback;
		
		threadExecutor.execute(new Thread() {
			@Override
			public void run() {
				Object object = null;
				try {
					object = httpClient.executeOAuthRequest(finalRequest, finalHandler);
				} catch (OAuthMessageSignerException e) {
					finalCallback.onError(e.getMessage());
				} catch (OAuthExpectationFailedException e) {
					finalCallback.onError(e.getMessage());
				} catch (OAuthCommunicationException e) {
					finalCallback.onError(e.getMessage());
				} catch (IOException e) {
					finalCallback.onError(e.getMessage());
				}
				finalCallback.onSuccess(object);
			}
		});
	}
	
	/**
	 * Method called when AuthorizationExceptions are raised during execute.
	 * @param e
	 * @throws APIException
	 */
	protected void dealWithAuthorizationException(Exception e) throws APIException {
		e.printStackTrace();
		throw new APIException(SimpleGeoHandler.NOT_AUTHORIZED, e.getMessage());
	}
	
	/**
	 * Return the OAuthHttpClient
	 */
	public IOAuthClient getHttpClient() {
		return httpClient;
	}	
	
	protected abstract Object executeGet(String uri, ISimpleGeoJSONHandler handler) throws IOException;
	protected abstract void executeGet(String uri, ISimpleGeoJSONHandler handler, ISimpleGeoCallback callback) throws IOException;
	
	protected abstract Object executePost(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;
	protected abstract void executePost(String uri, String jsonPayload, ISimpleGeoJSONHandler handler, ISimpleGeoCallback callback) throws IOException;
	
	protected abstract Object executePut(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;
	protected abstract void executePut(String uri, String jsonPayload, ISimpleGeoJSONHandler handler, ISimpleGeoCallback callback) throws IOException;

	protected abstract Object executeDelete(String uri, ISimpleGeoJSONHandler handler) throws IOException;
	protected abstract void executeDelete(String uri, ISimpleGeoJSONHandler handler, ISimpleGeoCallback callback) throws IOException;

}

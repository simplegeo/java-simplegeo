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

package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
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

import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;

/**
 * Extracts as much common code as possible between the SimpleGeoClient and the SimpleGeoURLConnClient.
 * 
 * @author Casey Crites
 */

public abstract class AbstractSimpleGeoClient implements ISimpleGeoClient {
	
	private RequestThreadPoolExecutor threadExecutor;
	protected OAuthHttpClient httpClient;
	
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
	public static final String DEFAULT_HOST = "http://api.simplegeo.com";
	public static final String DEFAULT_PORT = "80";
	public static final String DEFAULT_VERSION = "1.0";
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());
		
	protected static ISimpleGeoClient sharedLocationService = null;
	
	protected GeoJSONHandler geoJSONHandler = null;
	protected JSONHandler jsonHandler = null;
	
	protected String baseUrl = "http://api.simplegeo.com";
	protected String port = "80";
	protected String apiVersion = "1.0";
	public HashMap<String, String> endpoints = new HashMap<String, String>();
	
	/**
	 * Tells the service whether to make the Http call on the same thread.  Note: if the underlying
	 * client doesn't handle future tasks, this flag will be ignored.
	 */
	protected boolean futureTask = false; 
	
	/**
	 * Main constructor class for setting up the client that the specific Places/Context clients
	 * extend from.
	 * @param baseUrl String - Default is http://api.simplegeo.com, but this can be overridden
	 * @param port String - Default is 80, but this can be overridden
	 * @param apiVersion - Default is 1.0, but this can be overridden
	 */
	protected AbstractSimpleGeoClient(String baseUrl, String port, String apiVersion) {
		this.baseUrl = baseUrl;
		this.port = port;
		this.apiVersion = apiVersion;
		
		setHandler(Handler.JSON, new JSONHandler());
		setHandler(Handler.GEOJSON, new GeoJSONHandler());
		
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
	 * Default constructor class
	 */
	protected AbstractSimpleGeoClient() {
		this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}

	/**
	 * Set the handler per type.
	 * 
	 * This is useful if subclasses or implementations of the model
	 * objects change and the response object needs to be built 
	 * differently.
	 * 
	 * @param type the Handler type to override
	 * @param handler the handler must implement ISimpleGeoJSONHandler.
	 */
	public void setHandler(Handler type, ISimpleGeoJSONHandler handler) {
		
		switch(type) {
			case GEOJSON:
				geoJSONHandler = (GeoJSONHandler)handler;
				break;
			case JSON:
				jsonHandler = (JSONHandler)handler;
				break;
			default:
				break;
		}
	}
	
	/**
	 * @param type the type of handler to retrieved defined by 
	 * {@link com.simplegeo.client.Handler}
	 * @return the instance of {@link com.simplegeo.client.handler.ISimpleGeoJSONHandler}
	 * that is associated with the type
	 */
	public ISimpleGeoJSONHandler getHandler(Handler type) {
		
		ISimpleGeoJSONHandler handler = null;
		switch(type) {
			
			case GEOJSON:
				handler = geoJSONHandler;
				break;
			case JSON:
				handler = jsonHandler;
				break;
			default:
				break;
				
		}
		
		return handler;
	}
	
	/**
	 * Method for executing HttpRequests either synchronously or asynchronously.
	 * @param request HttpUriRequest
	 * @param handler {@link com.simplegeo.client.http.SimpleGeoHandler} to call back when the request completes.
	 * It will then in turn hand off to an instance of  {@link com.simplegeo.client.handler.ISimpleGeoHandler}
	 * @return If futureTask is true, it will return a FutureTask. If futureTask is false, it will return either
	 * a {@link com.simplegeo.client.types.Feature}, {@link com.simplegeo.client.types.FeatureCollection} or a
	 * regular HashMap<Sring, Object>.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected Object execute(HttpUriRequest request, SimpleGeoHandler handler)
		throws ClientProtocolException, IOException {

		logger.info(String.format("sending %s", request.toString()));
	
		if(futureTask) {
	
			final HttpUriRequest finalRequest = request;
			final SimpleGeoHandler finalHandler = handler;
	
			FutureTask<Object> future = 
				new FutureTask<Object>(new Callable<Object>() {
					public Object call() throws ClientProtocolException, IOException {
						Object object = null;
						try {
							object = httpClient.executeOAuthRequest(finalRequest, finalHandler);
						} catch (OAuthMessageSignerException e) {
							dealWithAuthorizationException(e);
						} catch (OAuthExpectationFailedException e) {
							dealWithAuthorizationException(e);
						} catch (OAuthCommunicationException e) {
							dealWithAuthorizationException(e);
						}
	
						return object;
					}
				});
	
			threadExecutor.execute(future);
			return future;
	
		} else {
	
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

	}
	
	/**
	 * Return the OAuthHttpClient
	 */
	public IOAuthClient getHttpClient() {
		return httpClient;
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
	 * Whether or not the httpclient supports FutureTasks
	 */
	@Override
	public boolean supportsFutureTasks() {
		// 
		// By default, future tasks aren't supported.
		//
		return false;
	}

	@Override
	public void setFutureTask(boolean futureTask) {
		this.futureTask = futureTask;		
	}

	@Override
	public boolean getFutureTask() {
		return futureTask;	
	}

	protected abstract Object executeGet(String uri, ISimpleGeoJSONHandler handler) throws IOException;
	
	protected abstract Object executePost(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;
	
	protected abstract Object executePut(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;

	protected abstract Object executeDelete(String uri, ISimpleGeoJSONHandler handler) throws IOException;

}

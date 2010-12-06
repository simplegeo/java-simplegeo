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
		
	protected AbstractSimpleGeoClient() {
		this("http://api.simplegeo.com", "80", "1.0");
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

	private ISimpleGeoJSONHandler getHandler(Object record) {
		return geoJSONHandler;
	}
	
	public IOAuthClient getHttpClient() {
		return httpClient;
	}
	
	protected void dealWithAuthorizationException(Exception e) throws APIException {
		
		e.printStackTrace();
		throw new APIException(SimpleGeoHandler.NOT_AUTHORIZED, e.getMessage());
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#supportsFutureTasks()
	 */
	@Override
	public boolean supportsFutureTasks() {
		// 
		// By default, future tasks aren't supported.
		//
		return false;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#setFutureTask(boolean)
	 */
	@Override
	public void setFutureTask(boolean futureTask) {
		this.futureTask = futureTask;		
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#getFutureTask()
	 */
	@Override
	public boolean getFutureTask() {
		return futureTask;	
	}
	
	public String createQueryString(HashMap<String, Object> queryParams) {
		if (queryParams.size() == 0)
			return "";
		String queryString = "";
		Iterator<String> keys = queryParams.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			queryString += "," + key + "=" + queryParams.get(key);
		}
		return queryString.replaceFirst(",", "");
	}

	protected abstract Object executeGet(String uri, ISimpleGeoJSONHandler handler) throws IOException;
	
	protected abstract Object executePost(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;
	
	protected abstract Object executePut(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;

	protected abstract Object executeDelete(String uri, ISimpleGeoJSONHandler handler) throws IOException;

}

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
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.handler.SimpleGeoJSONHandlerIfc;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;

/**
 * Interfaces with the SimpleGeo API. Requests are created through 
 * {@link com.simplegeo.client.http.OAuthHttpClient}, which is created
 * with a thread-safe connection manager.
 * 
 * Requests can be created and set on the same thread or they can be
 * used in a non-blocking fashion. The default behavior is to send the
 * request on the same thread the call was made. By setting 
 * {@link com.simplegeo.client.SimpleGeoClient#futureTask} to true,
 * requests will be built and sent on a thread chosen by
 * {@link com.simplegeo.client.concurrent.RequestThreadPoolExcecutor} and
 * a {@link java.util.concurrent.FutureTask} will be returned instead of
 * the return object that is build from the ResponseHandler.
 * 
 * In order to properly authenticate requests, an OAuth token is required.
 * This property is set by calling 
 * {@link com.simplegeo.client.http.OAuthHttpClient#setToken(String, String)}.
 * 
 * @author Derek Smith
 */
public class SimpleGeoClient extends AbstractSimpleGeoClient {
	
	private RequestThreadPoolExecutor threadExecutor = null;
	
	protected OAuthHttpClient httpClient = null;
	
	protected static Logger logger = Logger.getLogger(SimpleGeoClient.class.getName());
	
	/**
	 * @return the shared instance of this class
	 */
	static public SimpleGeoClient getInstance() {
		
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoClient();
		
		return (SimpleGeoClient) sharedLocationService;
	}
	
	private SimpleGeoClient() {
		
		super();

		this.threadExecutor = new RequestThreadPoolExecutor("SimpleGeoClient");
		
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
	 * @return the Http client used to execute all requests
	 */
	public IOAuthClient getHttpClient() {
		return this.httpClient;
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
	
	@Override
	protected Object executeGet(String uri, SimpleGeoJSONHandlerIfc handler)
			throws IOException {
		return execute (new HttpGet(uri), new SimpleGeoHandler (handler));
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			SimpleGeoJSONHandlerIfc handler) throws IOException {
		
		HttpPost post = new HttpPost(uri);
		
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
			
		return execute(post, new SimpleGeoHandler (handler));
	}

	@Override
	protected Object executeDelete(String uri, SimpleGeoJSONHandlerIfc handler)
			throws IOException {

		return execute (new HttpDelete(uri), new SimpleGeoHandler (handler));
	}

	@Override
	public boolean supportsFutureTasks() {
		return true;
	}
	
}

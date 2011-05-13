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
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;

/**
 * Extracts as much common code as possible between the SimpleGeoPlacesClient, 
 * SimpleGeoContextClient and SimpleGeoStorageClient.
 * 
 * @author Casey Crites
 */

public abstract class AbstractSimpleGeoClient implements SimpleGeoClient {
	
	private RequestThreadPoolExecutor threadExecutor;
	protected OAuthHttpClient httpClient;
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());
	
	protected String baseUrl = DEFAULT_HOST;
	protected String port = DEFAULT_PORT;
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
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
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
	 * Set the client to enable or disable the use of SSL for SimpleGeo server requests
	 * @param boolean, true to enable SSL, false to disable SSL
	 */
	public void enableSSL(boolean useSSL) {
		if (useSSL) {
			baseUrl = DEFAULT_HOST;
			port = DEFAULT_PORT;
		} else {
			baseUrl = DEFAULT_HTTP_HOST;
			port = DEFAULT_HTTP_PORT;
		}
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
	protected Object execute(String urlString, HttpRequestMethod method, String jsonPayload, SimpleGeoHandler handler)
		throws ClientProtocolException, IOException {
	
		Object object = null;
		try {
			object = httpClient.executeOAuthRequest(this.removeEmptyParameters(urlString), method, jsonPayload, handler);
		} catch (OAuthMessageSignerException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthExpectationFailedException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthCommunicationException e) {
			dealWithAuthorizationException(e);
		}

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
	protected void execute(String urlString, HttpRequestMethod method, String jsonPayload, SimpleGeoHandler handler, SimpleGeoCallback callback)
		throws ClientProtocolException, IOException {

		final SimpleGeoHandler finalHandler = handler;
		final SimpleGeoCallback finalCallback = callback;
		final String finalUrlString = this.removeEmptyParameters(urlString);
		final HttpRequestMethod finalMethod = method;
		final String finalJsonPayload = jsonPayload;
		
		threadExecutor.execute(new Thread() {
			@Override
			public void run() {
				Object object = null;
				try {
					object = httpClient.executeOAuthRequest(finalUrlString, finalMethod, finalJsonPayload, finalHandler);
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
	public OAuthClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * Remove empty parameters so we're not sending q=&category=.
	 * 
	 * @param uri String uri containing parameters
	 * @return String uri with empty parameters removed
	 */
	private String removeEmptyParameters(String uri) {
		if (uri.indexOf("?") == -1)
			return uri;
		
		String base = uri.substring(0, uri.indexOf("?"));
		String[] parameters = uri.substring(uri.indexOf("?") + 1).split("&");
		String newQuery = "";
		for (String parameter : parameters) {
			if (!parameter.endsWith("=")) {
				newQuery += "&" + parameter;
			}
		}
		return base + "?" + newQuery.replaceFirst("&", "");
	}
}

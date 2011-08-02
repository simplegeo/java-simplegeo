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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.types.Annotation;

/**
 * Extracts as much common code as possible from the SimpleGeoPlacesClient, 
 * SimpleGeoContextClient and SimpleGeoStorageClient.
 * 
 * @author Casey Crites
 */

public abstract class AbstractSimpleGeoClient implements SimpleGeoClient {
	
	private RequestThreadPoolExecutor threadExecutor;
	private OAuthHttpClient httpClient;
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());
	
	public HashMap<String, String> endpoints = new HashMap<String, String>();
	
	/**
	 * Main constructor class for setting up the client that the specific Places/Context clients
	 * extend from.
	 */
	protected AbstractSimpleGeoClient() {
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("https", PORT, SSLSocketFactory.getSocketFactory()));
		ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(schemeRegistry);

		this.httpClient = new OAuthHttpClient(connManager, params);
		this.threadExecutor = new RequestThreadPoolExecutor("SimpleGeoClient");
		endpoints.put("features", "1.0/features/%s.json");
		endpoints.put("annotations", "1.0/features/%s/annotations.json");
	}

	/**
	 * Method for executing HttpRequests synchronously.
	 * @param urlString String URL endpoint.
	 * @param method {@link com.simplegeo.client.SimpleGeoClient.HttpRequestMethod}
	 * @param extraParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param jsonPayload String A string with the json that will be sent with the request.
	 * @param handler {@link com.simplegeo.client.http.SimpleGeoHandler} to call back when the request completes.
	 * It will then in turn hand off to an instance of  {@link com.simplegeo.client.handler.SimpleGeoResponseHandler}
	 * @return String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected String execute(String urlString, HttpRequestMethod method, HashMap<String, String[]> extraParams, String jsonPayload) throws ClientProtocolException, IOException {
	
		String response = "";
		try {
			response = httpClient.executeOAuthRequest(urlString + this.buildQueryString(extraParams), method, jsonPayload, new SimpleGeoHandler());
		} catch (OAuthMessageSignerException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthExpectationFailedException e) {
			dealWithAuthorizationException(e);
		} catch (OAuthCommunicationException e) {
			dealWithAuthorizationException(e);
		}

		return response;

	}
	
	/**
	 * Method for executing HttpRequests asynchronously.
	 * @param urlString String URL endpoint.
	 * @param method {@link com.simplegeo.client.SimpleGeoClient.HttpRequestMethod}
	 * @param extraParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param jsonPayload String A string with the json that will be sent with the request.
	 * @param handler {@link com.simplegeo.client.http.SimpleGeoHandler} to call back when the request completes.
	 * It will then in turn hand off to an instance of  {@link com.simplegeo.client.handler.SimpleGeoResponseHandler}
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the SimpleGeoCallback interface
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected void execute(String urlString, HttpRequestMethod method, HashMap<String, String[]> extraParams, String jsonPayload, SimpleGeoCallback callback) throws ClientProtocolException, IOException {

		final SimpleGeoCallback finalCallback = callback;
		final String finalUrlString = urlString + this.buildQueryString(extraParams);
		final HttpRequestMethod finalMethod = method;
		final String finalJsonPayload = jsonPayload;
		
		threadExecutor.execute(new Thread() {
			@Override
			public void run() {
				String response = "";
				try {
					response = httpClient.executeOAuthRequest(finalUrlString, finalMethod, finalJsonPayload, new SimpleGeoHandler());
				} catch (OAuthMessageSignerException e) {
					finalCallback.onError(e.getMessage());
				} catch (OAuthExpectationFailedException e) {
					finalCallback.onError(e.getMessage());
				} catch (OAuthCommunicationException e) {
					finalCallback.onError(e.getMessage());
				} catch (IOException e) {
					finalCallback.onError(e.getMessage());
				}
				finalCallback.onSuccess(response);
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
	
	// Common API endpoints

	/**
	 * Synchronously get a list of all the possible Feature categories
	 * 
	 * @return {@link com.simplegeo.client.types.CategoryCollection} containing a list of {@link com.simplegeo.client.types.Category} objects
	 */
	public String getCategories() throws IOException{
		return this.execute(String.format(Locale.US, this.getEndpoint("features"), "categories"), HttpRequestMethod.GET, null, "");
	}
	
	/**
	 * Asynchronously get a list of all the possible Feature categories
	 * 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 */
	public void getCategories(SimpleGeoCallback callback) throws IOException{
		this.execute(String.format(Locale.US, this.getEndpoint("features"), "categories"), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously retrieve a SimpleGeo feature.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @return String
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String getFeature(String simpleGeoId) throws ClientProtocolException, IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), HttpRequestMethod.GET, null, "");
	}
		
	/**
	 * Asynchronously retrieve a SimpleGeo feature.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void getFeature(String simpleGeoId, SimpleGeoCallback callback) throws ClientProtocolException, IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously retrieve a SimpleGeo feature's annotations.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @return String
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String getFeatureAnnotations(String simpleGeoId) throws ClientProtocolException, IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("annotations"), simpleGeoId), HttpRequestMethod.GET, null, "");
	}
		
	/**
	 * Asynchronously retrieve a SimpleGeo feature's annotations.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void getFeatureAnnotations(String simpleGeoId, SimpleGeoCallback callback) throws ClientProtocolException, IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("annotations"), simpleGeoId), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously retrieve a SimpleGeo feature's annotations.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param annotation {@link Annotation}.
	 * @return String
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public String setFeatureAnnotations(String simpleGeoId, Annotation annotation) throws ClientProtocolException, IOException, JSONException {
		return this.execute(String.format(Locale.US, this.getEndpoint("annotations"), simpleGeoId), HttpRequestMethod.POST, null, annotation.toJSONString());
	}
		
	/**
	 * Asynchronously retrieve a SimpleGeo feature's annotations.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param annotation {@link Annotation}.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public void setFeatureAnnotations(String simpleGeoId, Annotation annotation, SimpleGeoCallback callback) throws ClientProtocolException, IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("annotations"), simpleGeoId), HttpRequestMethod.POST, null, annotation.toJSONString(), callback);
	}
	
	// Util methods
	
	private String buildQueryString(HashMap<String, String[]> params) throws UnsupportedEncodingException {
		if (params == null || params.size() == 0) { return ""; }
		Set<Entry<String, String[]>> entries = params.entrySet();
		StringBuffer queryBuffer = new StringBuffer("?");
		for (Entry<String, String[]> entry : entries) {
			for (String value : entry.getValue()) {
				if (queryBuffer.length() > 1) { queryBuffer.append("&"); }
				queryBuffer.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
			}
		}
		return queryBuffer.toString();
	}

	/**
	 * Grab the desired endpoint and add it to the server and version.
	 * @param endpointName
	 * @return String A URL pointing at the desired server
	 */
	protected String getEndpoint(String endpointName) {
		return String.format(Locale.US, "%s/%s", HOST, endpoints.get(endpointName));
	}
}
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
import java.net.URLEncoder;
import java.util.Locale;

import org.json.JSONObject;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.http.OAuthClient;


/**
 * Class for interacting with the SimpleGeo Context API.
 * 
 * @author Casey Crites
 */

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {
	
	protected static SimpleGeoContextClient sharedContextService;
	
	/**
	 * Method that ensures we only have one instance of the {@link com.simplegeo.client.SimpleGeoContextClient} instantiated.
	 * 
	 * @return {@link com.simplegeo.client.SimpleGeoContextClient}
	 */
	public static SimpleGeoContextClient getInstance() {
		if(sharedContextService == null) { sharedContextService = new SimpleGeoContextClient(); }
		return sharedContextService;
	}

	/**
	 * {@link com.simplegeo.client.SimpleGeoContextClient} constructor
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 */
	private SimpleGeoContextClient() {
		super();
		
		endpoints.put("address", "1.0/context/address.json?address=%s");
		endpoints.put("context", "1.0/context/%f,%f.json");
		endpoints.put("ip", "1.0/context/%s.json");
		endpoints.put("myIp", "1.0/context/ip.json");
	}
	
	/**
	 * Synchronously get context for the given latitude and longitude.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @return JSONObject
	 * @throws IOException
	 */
	public JSONObject getContext(double lat, double lon) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("context"), lat, lon), HttpRequestMethod.GET, null, "");
	}
	
	/**
	 * Asynchronously get context for the given latitude and longitude.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContext(double lat, double lon, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("context"), lat, lon), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously get context for a specific IP.
	 * 
	 * @param ip String IP Address If blank, your IP address will be used
	 * @return HashMap<String, Object> HashMap containing weather, features, demographics and query
	 * @throws IOException
	 */
	public JSONObject getContextByIP(String ip) throws IOException {
		if (ip == null || "".equals(ip)) { return this.getContextByIP(); }
		return this.execute(String.format(Locale.US, this.getEndpoint("ip"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, null, "");
	}
	
	/**
	 * Asynchronously get context for a specific IP.
	 * 
	 * @param ip String IP Address If blank, your IP address will be used
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByIP(String ip, SimpleGeoCallback callback) throws IOException {
		if (ip == null || "".equals(ip)) { 
			this.getContextByIP(ip, callback);
		} else {
			this.execute(String.format(Locale.US, this.getEndpoint("ip"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
		}
	}
		
	/**
	 * Synchronously get context for your IP.
	 * 
	 * @return JSONObject containing weather, features, demographics and query
	 * @throws IOException
	 */
	public JSONObject getContextByIP() throws IOException {
		return this.execute(this.getEndpoint("myIp"), HttpRequestMethod.GET, null, "");
	}
	
	/**
	 * Asynchronously get context for your IP.
	 * 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByIP(SimpleGeoCallback callback) throws IOException {
		this.execute(this.getEndpoint("myIp"), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously get context for a physical street address.
	 * 
	 * @param address String Physical street address
	 * @return HashMap<String, Object> HashMap containing weather, features, demographics and query
	 * @throws IOException
	 */
	public JSONObject getContextByAddress(String address) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8")), HttpRequestMethod.GET, null, "");
	}
	
	/**
	 * Asynchronously get context for a physical street address.
	 * 
	 * @param address String Physical street address
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByAddress(String address, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
	}

	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}
}

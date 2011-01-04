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

import org.apache.http.client.methods.HttpGet;

import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {
	
	protected static SimpleGeoContextClient sharedContextService = null;
	
	/**
	 * Method that ensures we only have one instance of the SimpleGeoContextClient instantiated and allows
	 * server connection variables to be overridden.
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 * @return SimpleGeoContextClient
	 */
	public static SimpleGeoContextClient getInstance(String baseUrl, String port, String apiVersion) {
		if(sharedContextService == null)
			sharedContextService = new SimpleGeoContextClient(baseUrl, port, apiVersion);

		return (SimpleGeoContextClient) sharedContextService;		
	}
	
	/**
	 * Default method for retrieving a SimpleGeoContextClient.
	 * 
	 * @return SimpleGeoContextClient
	 */
	public static SimpleGeoContextClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}
	
	/**
	 * SimpleGeoContextClient constructor
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 */
	private SimpleGeoContextClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("address", "context/address.json?address=%s");
		endpoints.put("context", "context/%f,%f.json");
		endpoints.put("ip", "context/%s.json");
		endpoints.put("myIp", "context/ip.json");
		
		this.setFutureTask(true);
	}
	
	/**
	 * Retrieve context for the given latitude and longitude.
	 * 
	 * @param lat Double latitude.
	 * @param lon Double longitude.
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else HashMap containing weather, features,
	 * demographics and query.
	 * @throws IOException
	 */
	public Object getContext(double lat, double lon) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("context"), lat, lon), new JSONHandler());
	}
	
	/**
	 * Retrieve context for the requesting IP.
	 * 
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else HashMap containing weather, features,
	 * demographics and query.
	 * @throws IOException
	 */
	public Object getContextByIP() throws IOException {
		return this.getContextByIP("");
	}
	
	/**
	 * Retrieve context for a specific IP.
	 * 
	 * @param ip IP Address
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else HashMap containing weather, features,
	 * demographics and query.
	 * @throws IOException
	 */
	public Object getContextByIP(String ip) throws IOException {
		if ("".equals(ip)) {
			return this.executeGet(this.getEndpoint("myIp"), new JSONHandler());
		} else {
			return this.executeGet(String.format(this.getEndpoint("ip"), URLEncoder.encode(ip, "UTF-8")), new JSONHandler());
		}
	}
	
	/**
	 * Retrieve context for a physical street address.
	 * 
	 * @param address Physical street address
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else HashMap containing weather, features,
	 * demographics and query.
	 * @throws IOException
	 */
	public Object getContextByAddress(String address) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8")), new JSONHandler());
	}

	@Override
	public IOAuthClient getHttpClient() {
		return super.getHttpClient();
	}

	@Override
	protected Object executeGet(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		return super.execute(new HttpGet(uri), new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		throw new UnsupportedOperationException("Posts are not allowed in the Context service.");
	}

	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		throw new UnsupportedOperationException("Puts are not allowed in the Context service.");
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		throw new UnsupportedOperationException("Deletes are not allowed in the Context service.");
	}

}

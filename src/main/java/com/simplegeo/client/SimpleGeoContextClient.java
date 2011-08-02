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
import java.util.HashMap;
import java.util.Locale;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.http.OAuthClient;


/**
 * Class for interacting with the SimpleGeo Context API.
 * 
 * @author Casey Crites
 */

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoContextClient} constructor
	 */
	public SimpleGeoContextClient() {
		super();
		
		endpoints.put("address", "1.0/context/address.json?address=%s");
		endpoints.put("context", "1.0/context/%f,%f.json");
		endpoints.put("ip", "1.0/context/%s.json");
		endpoints.put("myIp", "1.0/context/ip.json");
		endpoints.put("boundingBox", "1.0/context/%f,%f,%f,%f.json");
		endpoints.put("searchDemographics", "1.0/context/demographics/search.json");
	}

	/**
	 * Synchronously get context for the given latitude and longitude.
	 * 
	 * https://simplegeo.com/docs/api-endpoints/simplegeo-context
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param queryParams HashMap<String, String[]>
	 * @return String
	 * @throws IOException
	 */
	public String getContext(double lat, double lon, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("context"), lat, lon), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get context for the given latitude and longitude.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContext(double lat, double lon, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("context"), lat, lon), HttpRequestMethod.GET, null, "", callback);
	}
		
	/**
	 * Synchronously get context for a specific IP.
	 * 
	 * @param ip String IP Address If blank, your IP address will be used
	 * @param queryParams HashMap<String, String>
	 * @return String
	 * @throws IOException
	 */
	public String getContextByIP(String ip, HashMap<String, String[]> queryParams) throws IOException {
		if (ip == null || "".equals(ip)) { return this.getContextByIP(queryParams); }
		return this.execute(String.format(Locale.US, this.getEndpoint("ip"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get context for a specific IP.
	 * 
	 * @param ip String IP Address If blank, your IP address will be used
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByIP(String ip, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		if (ip == null || "".equals(ip)) { 
			this.getContextByIP(queryParams, callback);
		} else {
			this.execute(String.format(Locale.US, this.getEndpoint("ip"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
		}
	}
	
	/**
	 * Synchronously get context for your IP.
	 * 
	 * @param queryParams HashMap<String, String>
	 * @return String containing weather, features, demographics and query
	 * @throws IOException
	 */
	public String getContextByIP(HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(this.getEndpoint("myIp"), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get context for your IP.
	 * 
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByIP(HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(this.getEndpoint("myIp"), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously get context for a physical street address.
	 * 
	 * @param address String Physical street address
	 * @param queryParams HashMap<String, String>
	 * @return HashMap<String, Object> HashMap containing weather, features, demographics and query
	 * @throws IOException
	 */
	public String getContextByAddress(String address, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get context for a physical street address.
	 * 
	 * @param address String Physical street address
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByAddress(String address, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
	}

	/**
	 * Synchronously get context for the given bounding box.
	 * 
	 * https://simplegeo.com/docs/api-endpoints/simplegeo-context
	 * 
	 * @param nwLat double Northwest corner latitude
	 * @param nwLon double Northwest corner longitude
	 * @param seLat double Southeast corner latitude
	 * @param seLon double Southeast corner longitude
	 * @param queryParams HashMap<String, String>
	 * @return String
	 * @throws IOException
	 */
	public String getContextByBoundingBox(double nwLat, double nwLon, double seLat, double seLon, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("boundingBox"), nwLat, nwLon, seLat, seLon), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get context for the given bounding box.
	 * 
	 * https://simplegeo.com/docs/api-endpoints/simplegeo-context
	 * 
	 * @param nwLat double Northwest corner latitude
	 * @param nwLon double Northwest corner longitude
	 * @param seLat double Southeast corner latitude
	 * @param seLon double Southeast corner longitude
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getContextByBoundingBox(double nwLat, double nwLon, double seLat, double seLon, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("boundingBox"), nwLat, nwLon, seLat, seLon), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously search demographics.
	 * 
	 * https://simplegeo.com/docs/api-endpoints/simplegeo-context#demographics
	 * 
	 * @param queryParams HashMap<String, String>
	 * @return String
	 * @throws IOException
	 */
	public String searchDemographics(HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchDemographics")), HttpRequestMethod.GET, queryParams, "");
	}
		
	/**
	 * Asynchronously search demographics.
	 * 
	 * https://simplegeo.com/docs/api-endpoints/simplegeo-context#demographics
	 * 
	 * @param queryParams HashMap<String, String>
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @return String
	 * @throws IOException
	 */
	public void searchDemographics(HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchDemographics")), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}
}

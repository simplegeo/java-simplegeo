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

import org.json.JSONException;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.types.Feature;

/**
 * Class for interacting with the SimpleGeo Places API.
 * 
 * @author Casey Crites
 */

public class SimpleGeoPlacesClient extends AbstractSimpleGeoClient {
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoPlacesClient} constructor
	 * 
	 */
	public SimpleGeoPlacesClient() {
		super();
		
		endpoints.put("address", "1.0/places/address.json");
		endpoints.put("places", "1.0/places");
		endpoints.put("search", "1.0/places/%f,%f.json");
		endpoints.put("searchByIP", "1.0/places/%s.json");
		endpoints.put("searchByMyIP", "1.0/places/ip.json");
	}

	/**
	 * Synchronously add a new place to the places database
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing a new place.
	 * @return String containing a polling token, simplegeoid and a uri.
	 * @throws IOException
	 * @throws JSONException
	 */
	public String addPlace(Feature feature) throws IOException, JSONException {
		return this.execute(String.format(Locale.US, this.getEndpoint("places")), HttpRequestMethod.POST, null, feature.toJSONString());
	}
	
	/**
	 * Asynchronously add a new place to the places database
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing a new place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addPlace(Feature feature, SimpleGeoCallback callback) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("places")), HttpRequestMethod.POST, null, feature.toJSONString(), callback);
	}
	
	/**
	 * Synchronously update an existing place in the places database.
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing an existing place.
	 * @return String containing a polling token.
	 * @throws IOException
	 * @throws JSONException
	 */
	public String updatePlace(Feature feature) throws IOException, JSONException {
		return this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), HttpRequestMethod.POST, null, feature.toJSONString());
	}
	
	/**
	 * Asynchronously update an existing place in the places database.
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void updatePlace(Feature feature, SimpleGeoCallback callback) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), HttpRequestMethod.POST, null, feature.toJSONString(), callback);
	}
	
	/**
	 * Synchronously delete an existing place from the places database.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @return String containing a polling token.
	 * @throws IOException
	 */
	public String deletePlace(String simpleGeoId) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), HttpRequestMethod.DELETE, null, "");
	}
	
	/**
	 * Asynchronously delete an existing place from the places database.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deletePlace(String simpleGeoId, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), HttpRequestMethod.DELETE, null, "", callback);
	}

	/**
	 * Synchronously search for nearby places.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String search(double lat, double lon, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("search"), lat, lon), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search for nearby places.
	 * 
	 * @param lat Double latitude
	 * @param lon double longitude
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(double lat, double lon, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("search"), lat, lon), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously search by a physical address.
	 * 
	 * @param address String Physical address, such as 41 Decatur St, San Francisco, CA
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByAddress(String address, HashMap<String, String[]> queryParams) throws IOException {
		if (queryParams == null) { queryParams = new HashMap<String, String[]>(); }
		queryParams.put("address", new String[] {address});
		return this.execute(this.getEndpoint("address"), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search by a physical address.
	 * 
	 * @param address Physical address, such as 41 Decatur St, San Francisco, CA
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByAddress(String address, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		if (queryParams == null) { queryParams = new HashMap<String, String[]>(); }
		queryParams.put("address", new String[] {address});
		this.execute(this.getEndpoint("address"), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously search by a specific IP.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByIP(String ip, HashMap<String, String[]> queryParams) throws IOException {
		if (ip == null || "".equals(ip)) { return this.searchByIP(queryParams); }
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search by a specific IP.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(String ip, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		if (ip == null || "".equals(ip)) {
			this.searchByIP(queryParams, callback);
		} else {
			this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
		}
	}
		
	/**
	 * Synchronously search by your IP.
	 * 
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByIP(HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search by your IP.
	 * 
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP")), HttpRequestMethod.GET, queryParams, "", callback);
	}

	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}

}
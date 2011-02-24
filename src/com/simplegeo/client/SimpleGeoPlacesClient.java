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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.ListHandler;
import com.simplegeo.client.handler.SimpleGeoResponseHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.types.CategoryCollection;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.Point;

public class SimpleGeoPlacesClient extends AbstractSimpleGeoClient {
	
	/**
	 * Class for interacting with the SimpleGeo Places API.
	 * 
	 * @author Casey Crites
	 */
	
	protected static SimpleGeoPlacesClient placesClient = null;
	
	/**
	 * Method that ensures we only have one instance of the {@link com.simplegeo.client.SimpleGeoPlacesClient} instantiated.  Also allows
	 * server connection variables to be overridden.
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 * @return SimpleGeoPlacesClient
	 */
	public static SimpleGeoPlacesClient getInstance(String baseUrl, String port, String apiVersion) {
		if(placesClient == null)
			placesClient = new SimpleGeoPlacesClient(baseUrl, port, apiVersion);

		return (SimpleGeoPlacesClient) placesClient;		
	}
	
	/**
	 * Default method for retrieving a {@link com.simplegeo.client.SimpleGeoPlacesClient}.
	 * 
	 * @return SimpleGeoPlacesClient
	 */
	public static SimpleGeoPlacesClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoPlacesClient} constructor
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 */
	private SimpleGeoPlacesClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("address", "places/address.json?address=%s&q=%s&category=%s&radius=%s");
		endpoints.put("endpoints", "endpoints.json");
		endpoints.put("features", "features/%s.json");
		endpoints.put("places", "places");
		endpoints.put("search", "places/%f,%f.json?q=%s&category=%s&radius=%s");
		endpoints.put("searchByIP", "places/%s.json?q=%s&category=%s&radius=%s");
		endpoints.put("searchByMyIP", "places/ip.json?q=%s&category=%s&radius=%s");
	}
	
	/**
	 * Synchronously get the place that corresponds to the simpleGeoId
	 * 
	 * @param simpleGeoId String SimpleGeo generated id that corresponds to a place
	 * @return {@link com.simplegeo.client.types.Feature} {@link com.simplegeo.client.types.Feature} representing the place
	 * @throws IOException
	 */
	public Feature getPlace(String simpleGeoId) throws IOException {
		return (Feature) this.executeGet(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new GeoJSONHandler());
	}
	
	/**
	 * Asynchronously get the place that corresponds to the simpleGeoId
	 * @param simpleGeoId String SimpleGeo generated id that corresponds to a place
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback}
	 * @throws IOException
	 */
	public void getPlace(String simpleGeoId, SimpleGeoCallback<Feature> callback) throws IOException {
		this.executeGet(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new GeoJSONHandler(), callback);
	}
	
	/**
	 * Synchronously add a new place to the places database
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing a new place.
	 * @return HashMap<String, Object> HashMap containing a polling token, simplegeoid and a uri.
	 * @throws IOException
	 * @throws JSONException
	 */
	public HashMap<String, Object> addPlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		return (HashMap<String, Object>) this.executePost(String.format(this.getEndpoint("places")), jsonString, new JSONHandler());
	}
	
	/**
	 * Asynchronously add a new place to the places database
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing a new place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addPlace(Feature feature, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		this.executePost(String.format(this.getEndpoint("places")), jsonString, new JSONHandler(), callback);
	}
	
	/**
	 * Synchronously update an existing place in the places database.
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing an existing place.
	 * @return HashMap<String, Object> HashMap containing a polling token.
	 * @throws IOException
	 * @throws JSONException
	 */
	public HashMap<String, Object> updatePlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		return (HashMap<String, Object>) this.executePost(String.format(this.getEndpoint("places"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), jsonString, new JSONHandler());
	}
	
	/**
	 * Asynchronously update an existing place in the places database.
	 * 
	 * @param feature {@link com.simplegeo.client.types.Feature} representing an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void updatePlace(Feature feature, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		this.executePost(String.format(this.getEndpoint("places"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), jsonString, new JSONHandler(), callback);
	}
	
	/**
	 * Synchronously delete an existing place from the places database.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @return HashMap<String, Object> HashMap containing a polling token.
	 * @throws IOException
	 */
	public HashMap<String, Object> deletePlace(String simpleGeoId) throws IOException {
		return (HashMap<String, Object>) this.executeDelete(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new JSONHandler());
	}
	
	/**
	 * Asynchronously delete an existing place from the places database.
	 * 
	 * @param simpleGeoId String corresponding to an existing place.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deletePlace(String simpleGeoId, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException {
		this.executeDelete(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new JSONHandler(), callback);
	}
	
	/**
	 * Synchronously search for nearby places.
	 * 
	 * @param point Point {@link com.simplegeo.client.types.Point}
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results
	 * @throws IOException
	 */
	public FeatureCollection search(Point point, String query, String category, double radius) throws IOException {
		return (FeatureCollection) this.search(point.getLat(), point.getLon(), query, category, radius);
	}
	
	/**
	 * Asynchronously search for nearby places.
	 * 
	 * @param point Point {@link com.simplegeo.client.types.Point}
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(Point point, String query, String category, double radius, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
		this.search(point.getLat(), point.getLon(), query, category, radius, callback);
	}
	
	/**
	 * Synchronously search for nearby places.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public FeatureCollection search(double lat, double lon, String query, String category, double radius) throws IOException {
		return (FeatureCollection) this.executeGet(String.format(this.getEndpoint("search"), lat, lon, URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
	}
	
	/**
	 * Asynchronously search for nearby places.
	 * 
	 * @param lat Double latitude
	 * @param lon double longitude
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(double lat, double lon, String query, String category, double radius, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
		this.executeGet(String.format(this.getEndpoint("search"), lat, lon, URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler(), callback);
	}
	
	/**
	 * Synchronously search by a physical address.
	 * 
	 * @param address String Physical address, such as 41 Decatur St, San Francisco, CA
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public FeatureCollection searchByAddress(String address, String query, String category, double radius) throws IOException {
		return (FeatureCollection) this.executeGet(String.format(this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
	}
	
	/**
	 * Asynchronously search by a physical address.
	 * 
	 * @param address Physical address, such as 41 Decatur St, San Francisco, CA
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByAddress(String address, String query, String category, double radius, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
		this.executeGet(String.format(this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler(), callback);
	}
	
	/**
	 * Synchronously search by a specific IP.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public FeatureCollection searchByIP(String ip, String query, String category, double radius) throws IOException {
		if ("".equals(ip)) {
			return (FeatureCollection) this.executeGet(String.format(this.getEndpoint("searchByMyIP"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
		} else {
			return (FeatureCollection) this.executeGet(String.format(this.getEndpoint("searchByIP"), URLEncoder.encode(ip, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
		}
	}
	
	/**
	 * Asynchronously search by a specific IP.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param query String A term/phrase to search for
	 * @param category String A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(String ip, String query, String category, double radius, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
		if ("".equals(ip)) {
			this.executeGet(String.format(this.getEndpoint("searchByMyIP"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler(), callback);
		} else {
			this.executeGet(String.format(this.getEndpoint("searchByIP"), URLEncoder.encode(ip, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler(), callback);
		}
	}
	
	/**
	 * Synchronously get a list of all the possible Feature categories
	 * 
	 * @return {@link com.simplegeo.client.types.CategoryCollection} containing a list of {@link com.simplegeo.client.types.Category} objects
	 */
	public CategoryCollection getCategories() throws IOException{
		return (CategoryCollection) this.executeGet(String.format(this.getEndpoint("features"), "categories"), new ListHandler());
	}
	
	/**
	 * Asynchronously get a list of all the possible Feature categories
	 * 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 */
	public void getCategories(SimpleGeoCallback<CategoryCollection> callback) throws IOException{
		this.executeGet(String.format(this.getEndpoint("features"), "categories"), new ListHandler(), callback);

	}

	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
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
	
	@Override
	protected Object executeGet(String uri, SimpleGeoResponseHandler handler)
			throws IOException {
		uri = this.removeEmptyParameters(uri);
		HttpGet get = new HttpGet(uri);
		return super.execute(get, new SimpleGeoHandler(handler));
	}
	
	@Override
	protected void executeGet(String uri, SimpleGeoResponseHandler handler, SimpleGeoCallback callback)
			throws IOException {
		uri = this.removeEmptyParameters(uri);
		HttpGet get = new HttpGet(uri);
		super.execute(get, new SimpleGeoHandler(handler), callback);
	}
	
	@Override
	protected Object executePost(String uri, String jsonPayload,
			SimpleGeoResponseHandler handler) throws IOException {
		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		post.addHeader("Content-type", "application/json");
		return super.execute(post, new SimpleGeoHandler(handler));
	}

	@Override
	protected void executePost(String uri, String jsonPayload,
			SimpleGeoResponseHandler handler, SimpleGeoCallback callback) throws IOException {
		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		post.addHeader("Content-type", "application/json");
		super.execute(post, new SimpleGeoHandler(handler), callback);
	}
	
	@Override
	protected Object executePut(String uri, String jsonPayload,
			SimpleGeoResponseHandler handler) throws IOException {
		HttpPut put = new HttpPut(uri);
		put.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		put.addHeader("Content-type", "application/json");
		return super.execute(put, new SimpleGeoHandler(handler));
	}

	@Override
	protected void executePut(String uri, String jsonPayload,
			SimpleGeoResponseHandler handler, SimpleGeoCallback callback) throws IOException {
		HttpPut put = new HttpPut(uri);
		put.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		put.addHeader("Content-type", "application/json");
		super.execute(put, new SimpleGeoHandler(handler), callback);
	}

	@Override
	protected Object executeDelete(String uri, SimpleGeoResponseHandler handler)
			throws IOException {
		return super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler));
	}
	
	@Override
	protected void executeDelete(String uri, SimpleGeoResponseHandler handler, SimpleGeoCallback callback)
			throws IOException {
		super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler), callback);
	}

}

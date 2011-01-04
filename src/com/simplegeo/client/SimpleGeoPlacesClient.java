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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;

import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.Point;

public class SimpleGeoPlacesClient extends AbstractSimpleGeoClient {
	
	protected static SimpleGeoPlacesClient sharedPlacesService = null;
	
	/**
	 * Method that ensures we only have one instance of the SimpleGeoPlacesClient instantiated and allows
	 * server connection variables to be overridden.
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 * @return SimpleGeoPlacesClient
	 */
	public static SimpleGeoPlacesClient getInstance(String baseUrl, String port, String apiVersion) {
		if(sharedPlacesService == null)
			sharedPlacesService = new SimpleGeoPlacesClient(baseUrl, port, apiVersion);

		return (SimpleGeoPlacesClient) sharedPlacesService;		
	}
	
	/**
	 * Default method for retrieving a SimpleGeoPlacesClient.
	 * @return SimpleGeoPlacesClient
	 */
	public static SimpleGeoPlacesClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}
	
	/**
	 * SimpleGeoPlacesClient constructor
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
		endpoints.put("searcByIP", "places/%s.json?q=%s&category=%s&radius=%s");
		endpoints.put("searchByMyIP", "places/ip.json?q=%s&category=%s&radius=%s");
		
		this.setFutureTask(true);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Object getEndpointDescriptions() throws IOException {
		return this.executeGet(String.format(this.getEndpoint("endpoints")), new JSONHandler());
	}
	
	/**
	 * Return the place that corresponds to the simpleGeoId
	 * 
	 * @param simpleGeoId String SimpleGeo generated id that corresponds to a place
	 * @return FutureTask/Feature FutureTask if supported, if not a Feature representing the place
	 * @throws IOException
	 */
	public Object getPlace(String simpleGeoId) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new GeoJSONHandler());
	}
	
	/**
	 * Add a new place to the places database
	 * 
	 * @param feature Feature representing a new place.
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else a HashMap containing a polling token,
	 * simplegeoid and a uri.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Object addPlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		return this.executePost(String.format(this.getEndpoint("places")), jsonString, new JSONHandler());
	}
	
	/**
	 * Update an existing place in the places database.
	 * 
	 * @param feature Feature representing an existing place.
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else a HashMap containing a polling token.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Object updatePlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJSONString();
		return this.executePost(String.format(this.getEndpoint("places"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), jsonString, new JSONHandler());
	}
	
	/**
	 * Delete an existing place from the places database.
	 * @param simpleGeoId String corresponding to an existing place.
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else a HashMap containing a polling token.
	 * @throws IOException
	 */
	public Object deletePlace(String simpleGeoId) throws IOException {
		return this.executeDelete(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new JSONHandler());
	}
	
	/**
	 * Search for nearby places.
	 * 
	 * @param point Point {@link com.simplegeo.client.types.Point}
	 * @param query String A term/phrase to search for.
	 * @param category String A type of place to search for.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object search(Point point, String query, String category) throws IOException {
		return this.search(point.getLat(), point.getLon(), query, category);
	}
	
	/**
	 * Search for nearby places.
	 * 
	 * @param point Point {@link com.simplegeo.client.types.Point}
	 * @param query String A term/phrase to search for.
	 * @param category String A type of place to search for.
	 * @param radius double A distance in kilometers used to restrict searches.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object search(Point point, String query, String category, double radius) throws IOException {
		return this.search(point.getLat(), point.getLon(), query, category, radius);
	}
	
	/**
	 * Search for nearby places.
	 * 
	 * @param lat Double latitude.
	 * @param lon Double longitude.
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object search(double lat, double lon, String query, String category) throws IOException {
		return this.search(lat, lon, query, category, DEFAULT_RADIUS);
	}
	
	/**
	 * Search for nearby places.
	 * 
	 * @param lat Double latitude.
	 * @param lon Double longitude.
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @param radius double A distance in kilometers used to restrict searches.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object search(double lat, double lon, String query, String category, double radius) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("search"), lat, lon, URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
	}
	
	/**
	 * Do a search by a physical address.
	 * 
	 * @param address Physical address, such as 41 Decatur St, San Francisco, CA.
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object searchByAddress(String address, String query, String category) throws IOException {
		return this.searchByAddress(address, query, category, DEFAULT_RADIUS);
	}
	
	/**
	 * Do a search by a physical address.
	 * 
	 * @param address Physical address, such as 41 Decatur St, San Francisco, CA.
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @param radius double A distance in kilometers used to restrict searches.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object searchByAddress(String address, String query, String category, double radius) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("address"), URLEncoder.encode(address, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
	}
	
	/**
	 * Do a search by your IP.
	 * 
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object searchByIP(String query, String category) throws IOException {
		return this.searchByIP("", query, category, DEFAULT_RADIUS);
	}
	
	/**
	 * Do a search by your IP.
	 * 
	 * @param query A term/phrase to search for.
	 * @param category A type of place to search for.
	 * @param radius double A distance in kilometers used to restrict searches.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	public Object searchByIP(String query, String category, double radius) throws IOException {
		return this.searchByIP("", query, category, radius);
	}
	
	/**
	 * Do a search by a specific IP.
	 * 
	 * @param ip IP address
	 * @param query A term/phrase to search for
	 * @param category A type of place to search for
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	protected Object searchByIP(String ip, String query, String category) throws IOException {
		return this.searchByIP(query, category, DEFAULT_RADIUS);
	}
	
	/**
	 * Do a search by a specific IP.
	 * 
	 * @param ip IP address
	 * @param query A term/phrase to search for
	 * @param category A type of place to search for
	 * @param radius double A distance in kilometers used to restrict searches.
	 * @return FutureTask/FeatureCollection FutureTask if supported, else a FeatureCollection containing search results.
	 * @throws IOException
	 */
	protected Object searchByIP(String ip, String query, String category, double radius) throws IOException {
		if ("".equals(ip)) {
			return this.executeGet(String.format(this.getEndpoint("searchByMyIP"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
		} else {
			return this.executeGet(String.format(this.getEndpoint("searchByIP"), URLEncoder.encode(ip, "UTF-8"), URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"), radius), new GeoJSONHandler());
		}
	}
	
	@Override
	public IOAuthClient getHttpClient() {
		return super.getHttpClient();
	}
	
	/**
	 * Remove empty parameters so we're not sending q=&category=.
	 * @param uri String uri containing parameters.
	 * @return String uri with empty parameters removed.
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
	protected Object executeGet(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		uri = this.removeEmptyParameters(uri);
		HttpGet get = new HttpGet(uri);
		return super.execute(get, new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		post.addHeader("Content-type", "application/json");
		return super.execute(post, new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPut put = new HttpPut(uri);
		put.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		put.addHeader("Content-type", "application/json");
		return super.execute(new HttpPut(uri), new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		return super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler));
	}

}

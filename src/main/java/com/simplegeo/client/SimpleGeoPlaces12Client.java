package com.simplegeo.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import com.simplegeo.client.callbacks.SimpleGeoCallback;

/**
 * Class for interacting with the SimpleGeo Places 1.2 API.
 * 
 * @author Casey Crites
 */

public class SimpleGeoPlaces12Client extends AbstractSimpleGeoClient {
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoPlacesClient} constructor
	 * 
	 */
	public SimpleGeoPlaces12Client() {
		super();
		
		endpoints.put("address12", "1.2/places/address.json");
		endpoints.put("search12", "1.2/places/%f,%f.json");
		endpoints.put("searchNonGeo", "1.2/places/search.json");
		endpoints.put("searchByIP12", "1.2/places/%s.json");
		endpoints.put("searchByMyIP12", "1.2/places/ip.json");
		endpoints.put("searchByBoundingBox", "1.2/places/%f,%f,%f,%f.json");
	}
	
	/**
	 * Synchronously search for nearby places.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String search(double lat, double lon, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("search12"), lat, lon), HttpRequestMethod.GET, queryParams, "");
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
		this.execute(String.format(Locale.US, this.getEndpoint("search12"), lat, lon), HttpRequestMethod.GET, queryParams, "", callback);
	}
		
	/**
	 * Synchronously search for places.
	 * 
	 * @param query String Search phrase.
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String search(String query, HashMap<String, String[]> queryParams) throws IOException {
		if (queryParams == null) queryParams = new HashMap<String, String[]>();
		queryParams.put("q", new String[] { query });
		return this.execute(String.format(Locale.US, this.getEndpoint("searchNonGeo")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search for places.
	 * 
	 * @param query String Search phrase.
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(String query, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		if (queryParams == null) queryParams = new HashMap<String, String[]>();
		queryParams.put("q", new String[] { query });
		this.execute(String.format(Locale.US, this.getEndpoint("searchNonGeo")), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously search by a physical address.
	 * 
	 * @param address String Physical address, such as 41 Decatur St, San Francisco, CA
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByAddress(String address, HashMap<String, String[]> queryParams) throws IOException {
		if (queryParams == null) queryParams = new HashMap<String, String[]>();
		queryParams.put("address", new String[] {address});
		return this.execute(this.getEndpoint("address12"), HttpRequestMethod.GET, queryParams, "");
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
		if (queryParams == null) queryParams = new HashMap<String, String[]>();
		queryParams.put("address", new String[] {address});
		this.execute(this.getEndpoint("address12"), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously search by a specific IP.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByIP(String ip, HashMap<String, String[]> queryParams) throws IOException {
		if (ip == null || "".equals(ip)) return this.searchByIP(queryParams);
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByIP12"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
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
			this.execute(String.format(Locale.US, this.getEndpoint("searchByIP12"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
		}
	}
		
	/**
	 * Synchronously search by your IP.
	 * 
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByIP(HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP12")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search by your IP.
	 * 
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP12")), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously search for places in a bounding box.
	 * 
	 * @param nwLat double Northwest corner latitude
	 * @param nwLon double Northwest corner longitude
	 * @param seLat double Southeast corner latitude
	 * @param seLon double Southeast corner longitude
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @return String JSON string that can be converted to a {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String searchByBoundingBox(double nwLat, double nwLon, double seLat, double seLon, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByBoundingBox"), nwLat, nwLon, seLat, seLon), HttpRequestMethod.GET, queryParams, null);
	}
		
	/**
	 * Asynchronously search for places in a bounding box.
	 * 
	 * @param nwLat double Northwest corner latitude
	 * @param nwLon double Northwest corner longitude
	 * @param seLat double Southeast corner latitude
	 * @param seLon double Southeast corner longitude
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as, radius, q and category.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByBoundingBox(double nwLat, double nwLon, double seLat, double seLon, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByBoundingBox"), nwLat, nwLon, seLat, seLon), HttpRequestMethod.GET, queryParams, null, callback);
	}
	
}
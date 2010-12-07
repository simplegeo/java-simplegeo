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
	
	/**
	 * Method that ensures we only have one instance of the SimpleGeoPlacesClient instantiated and allows
	 * server connection variables to be overridden.
	 * @param baseUrl
	 * @param port
	 * @param apiVersion
	 * @return
	 */
	public static SimpleGeoPlacesClient getInstance(String baseUrl, String port, String apiVersion) {
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoPlacesClient(baseUrl, port, apiVersion);

		return (SimpleGeoPlacesClient) sharedLocationService;		
	}
	
	/**
	 * Default method for retrieving a SimpleGeoPlacesClient.  This should be used unless a test
	 * server is being hit.
	 * @return
	 */
	public static SimpleGeoPlacesClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}
	
	/**
	 * SimpleGeoPlacesClient constructor
	 * @param baseUrl
	 * @param port
	 * @param apiVersion
	 */
	private SimpleGeoPlacesClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("endpoints", "endpoints.json");
		endpoints.put("features", "features/%s.json");
		endpoints.put("places", "places");
		endpoints.put("search", "places/%f,%f.json?q=%s&category=%s");
		
		this.setFutureTask(true);
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
	 * 
	 * @return
	 * @throws IOException
	 */
	public Object getEndpointDescriptions() throws IOException {
		return this.executeGet(String.format(this.getEndpoint("endpoints")), new JSONHandler());
	}
	
	/**
	 * 
	 * @param simpleGeoId
	 * @return
	 * @throws IOException
	 */
	public Object getPlace(String simpleGeoId) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new GeoJSONHandler());
	}
	
	/**
	 * 
	 * @param feature
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Object addPlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJsonString();
		return this.executePost(String.format(this.getEndpoint("places")), jsonString, new JSONHandler());
	}
	
	/**
	 * 
	 * @param feature
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Object updatePlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJsonString();
		return this.executePost(String.format(this.getEndpoint("places"), URLEncoder.encode(feature.getSimpleGeoId(), "UTF-8")), jsonString, new JSONHandler());
	}
	
	/**
	 * 
	 * @param simpleGeoId
	 * @return
	 * @throws IOException
	 */
	public Object deletePlace(String simpleGeoId) throws IOException {
		return this.executeDelete(String.format(this.getEndpoint("features"), URLEncoder.encode(simpleGeoId, "UTF-8")), new JSONHandler());
	}
	
	/**
	 * 
	 * @param point
	 * @param query
	 * @param category
	 * @return
	 * @throws IOException
	 */
	public Object search(Point point, String query, String category) throws IOException {
		return this.search(point.getLat(), point.getLon(), query, category);
	}
	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param query
	 * @param category
	 * @return
	 * @throws IOException
	 */
	public Object search(double lat, double lon, String query, String category) throws IOException {
		String uri = String.format(this.getEndpoint("search"), lat, lon, 
				URLEncoder.encode(query, "UTF-8"), URLEncoder.encode(category, "UTF-8"));
		return this.executeGet(uri, new GeoJSONHandler());
	}
	
	/**
	 * 
	 */
	@Override
	public IOAuthClient getHttpClient() {
		return super.getHttpClient();
	}

	/**
	 * 
	 */
	@Override
	protected Object executeGet(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		System.out.println(uri);
		uri = this.removeEmptyParameters(uri);
		System.out.println(uri);
		HttpGet get = new HttpGet(uri);
		System.out.println(get.getURI().getQuery());
		return super.execute(get, new SimpleGeoHandler(handler));
	}
	
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

	/**
	 * 
	 */
	@Override
	protected Object executePost(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		post.addHeader("Content-type", "application/json");
		return super.execute(post, new SimpleGeoHandler(handler));
	}
	
	/**
	 * 
	 */
	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPut put = new HttpPut(uri);
		put.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		put.addHeader("Content-type", "application/json");
		return super.execute(new HttpPut(uri), new SimpleGeoHandler(handler));
	}

	/**
	 * 
	 */
	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		System.out.println(uri);
		return super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler));
	}

}

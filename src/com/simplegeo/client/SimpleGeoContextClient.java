package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.HttpGet;

import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {
	
	/**
	 * Method that ensures we only have one instance of the SimpleGeoContextClient instantiated and allows
	 * server connection variables to be overridden.
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 * @return SimpleGeoContextClient
	 */
	public static SimpleGeoContextClient getInstance(String baseUrl, String port, String apiVersion) {
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoContextClient(baseUrl, port, apiVersion);

		return (SimpleGeoContextClient) sharedLocationService;		
	}
	
	/**
	 * Default method for retrieving a SimpleGeoContextClient.  This should be used unless a test
	 * server is being hit.
	 * @return SimpleGeoContextClient
	 */
	public static SimpleGeoContextClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_VERSION);
	}
	
	/**
	 * SimpleGeoContextClient constructor
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 */
	private SimpleGeoContextClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("context", "context/%f,%f.json");
		
		this.setFutureTask(true);
	}
	
	/**
	 * Retrieve context for the given latitude and longitude.
	 * @param lat Double latitude.
	 * @param lon Double longitude.
	 * @return FutureTask/HashMap<String, Object> FutureTask if supported, else HashMap containing weather, features,
	 * demographics and query.
	 * @throws IOException
	 */
	public Object getContext(double lat, double lon) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("context"), lat, lon), new JSONHandler());
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

package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.HttpGet;

import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {
	
	public HashMap<String, String> endpoints = new HashMap<String, String>();
	
	public static SimpleGeoContextClient getInstance(String baseUrl, String port, String apiVersion) {
		if(sharedLocationService == null)
			sharedLocationService = new SimpleGeoContextClient(baseUrl, port, apiVersion);

		return (SimpleGeoContextClient) sharedLocationService;		
	}
	
	public static SimpleGeoContextClient getInstance() {
		return getInstance("http://api.simplegeo.com", "80", "1.0");
	}
	
	private SimpleGeoContextClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("context", "context/%f,%f.json");
		
		this.setFutureTask(true);
	}
	
	protected String getEndpoint(String endpointName) {
		return String.format("%s:%s/%s/%s", baseUrl, port, apiVersion, endpoints.get(endpointName));
	}
	
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

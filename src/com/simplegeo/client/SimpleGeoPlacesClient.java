package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.http.IOAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.types.Place;

public class SimpleGeoPlacesClient extends AbstractSimpleGeoClient {
	
	public HashMap<String, String> endpoints;
	String baseUrl = "http://api.simplegeo.com";
	String port = "80";
	String apiVersion = "1.0";
	
	public SimpleGeoPlacesClient() {
		endpoints.put("endpoints", "/endpoints.json");
		endpoints.put("places", "/places/%s.json");
		endpoints.put("place", "/places/place.json");
		endpoints.put("search", "/places/%f,%f/search.json?q=%s&category=%s");
	}
	
	protected String getEndpoint(String endpointName) {
		return endpoints.get(endpointName);
	}
	
	public void getEndpointDescriptions() throws IOException {
		this.executeGet(String.format(this.getEndpoint("endpoints")), new JSONHandler());
	}
	
	public void getPlace(Place place) throws IOException {
		this.executeGet(String.format(this.getEndpoint("places"), place.getSimpleGeoId()), new JSONHandler());
	}
	
	public void addPlace(Object place) throws IOException {
		// TODO Convert the place object to a json string
		this.executePut(String.format(this.getEndpoint("place")), "", new JSONHandler());
	}
	
	public void updatePlace(Place place) throws IOException {
		// TODO Convert the place object to a json string
		this.executePost(String.format(this.getEndpoint("places"), place.getSimpleGeoId()), "", new JSONHandler());
	}
	
	public void deletePlace(String simpleGeoId) throws IOException {
		this.executeDelete(String.format(this.getEndpoint("places"), simpleGeoId), new JSONHandler());
	}
	
	public void search(double lat, double lon, String query, String category) throws IOException {
		this.executeGet(String.format(this.getEndpoint("search"), lat, lon, query, category), new JSONHandler());
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
		HttpPost post = new HttpPost(uri);
		// TODO Need to set post.entity here
		return super.execute(post, new SimpleGeoHandler(handler));
	}
	
	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPut put = new HttpPut(uri);
		// TODO Need to set put.entity here
		return super.execute(new HttpPut(uri), new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		return super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler));
	}

}

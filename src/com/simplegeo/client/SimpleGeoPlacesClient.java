package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

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
	
	public Object getEndpointDescriptions() throws IOException {
		return this.executeGet(String.format(this.getEndpoint("endpoints")), new JSONHandler());
	}
	
	public Object getPlace(String simpleGeoId) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("places"), simpleGeoId), new GeoJSONHandler());
	}
	
	public Object addPlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJsonString();
		return this.executePut(String.format(this.getEndpoint("place")), jsonString, new GeoJSONHandler());
	}
	
	public Object updatePlace(Feature feature) throws IOException, JSONException {
		String jsonString = feature.toJsonString();
		return this.executePost(String.format(this.getEndpoint("places"), feature.getSimpleGeoId()), jsonString, new GeoJSONHandler());
	}
	
	public Object deletePlace(String simpleGeoId) throws IOException {
		return this.executeDelete(String.format(this.getEndpoint("places"), simpleGeoId), new GeoJSONHandler());
	}
	
	public Object search(Point point, String query, String category) throws IOException {
		return this.search(point.getLat(), point.getLon(), query, category);
	}
	
	public Object search(double lat, double lon, String query, String category) throws IOException {
		return this.executeGet(String.format(this.getEndpoint("search"), lat, lon, query, category), new GeoJSONHandler());
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
		post.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		return super.execute(post, new SimpleGeoHandler(handler));
	}
	
	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		HttpPut put = new HttpPut(uri);
		put.setEntity(new ByteArrayEntity(jsonPayload.getBytes()));
		return super.execute(new HttpPut(uri), new SimpleGeoHandler(handler));
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		return super.execute(new HttpDelete(uri), new SimpleGeoHandler(handler));
	}

}

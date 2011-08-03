package com.simplegeo.client;

import com.simplegeo.client.http.OAuthClient;

public interface SimpleGeoClient {
	
	public static final String HOST = "https://api.simplegeo.com";
	public static final int PORT = 443;

	static public enum Handler { JSON, GEOJSON, SIMPLEGEO }
	static public enum HttpRequestMethod { GET, POST, PUT, DELETE }
	
	/**
	 * @return the Http client used to execute all requests
	 */
	public OAuthClient getHttpClient();

}
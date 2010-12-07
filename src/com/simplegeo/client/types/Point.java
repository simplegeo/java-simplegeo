package com.simplegeo.client.types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Point {
	
	private double lat;
	private double lon;
	
	public Point() {
		
	}
	
	public Point(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public JSONArray toJSONArray() throws JSONException {
		JSONArray pointArray = new JSONArray();
		pointArray.put(this.getLat());
		pointArray.put(this.getLon());
		return pointArray;
	}
	
}
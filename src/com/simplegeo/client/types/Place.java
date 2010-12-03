package com.simplegeo.client.types;

import java.util.HashMap;

public class Place {

	private String simpleGeoId;
	private double lat;
	private double lon;
	private String type;
	private HashMap<String, ?> properties;
	
	public Place(String simpleGeoId, double lat, double lon, String type, HashMap<String, ?> properties) {
		this.simpleGeoId = simpleGeoId;
		this.lat = lat;
		this.lon = lon;
		this.type = type;
		this.properties = properties;
	}
	
	public String getSimpleGeoId() {
		return simpleGeoId;
	}

	public void setSimpleGeoId(String simpleGeoId) {
		this.simpleGeoId = simpleGeoId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, ?> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, ?> properties) {
		this.properties = properties;
	}
	
}

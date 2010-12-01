package com.simplegeo.client.types;

import java.util.HashMap;

public class Feature {
	
	private String featureId;
	private Point geometry;
	private HashMap<String, String> properties;
	private String rawBody;
	
	public Feature(String featureId, Point geometry, 
			HashMap<String, String> properties, String rawBody) {
		this.featureId = featureId;
		this.geometry = geometry;
		this.properties = properties;
		this.rawBody = rawBody;
	}
	
	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public Point getGeometry() {
		return geometry;
	}

	public void setGeometry(Point geometry) {
		this.geometry = geometry;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public String getRawBody() {
		return rawBody;
	}

	public void setRawBody(String rawBody) {
		this.rawBody = rawBody;
	}

}

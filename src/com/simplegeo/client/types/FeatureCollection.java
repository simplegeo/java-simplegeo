package com.simplegeo.client.types;

public class FeatureCollection {

	private Feature[] features;
	
	public FeatureCollection(Feature[] features) {
		this.features = features;
	}

	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}
	
}

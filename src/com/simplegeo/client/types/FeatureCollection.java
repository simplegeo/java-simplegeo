package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class FeatureCollection {

	private ArrayList<Feature> features;
	
	public FeatureCollection() {
		
	}
	
	public FeatureCollection(ArrayList<Feature> features) {
		this.features = features;
	}

	public ArrayList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}
	
	public static FeatureCollection fromJson(JSONArray jsonArray) throws JSONException {
		FeatureCollection featureCollection = new FeatureCollection();
		int numOfFeatures = jsonArray.length();
		ArrayList<Feature> features = new ArrayList<Feature>();
		for (int i=0; i<numOfFeatures; i++) {
			features.add(Feature.fromJson(jsonArray.getJSONObject(i)));
		}
		featureCollection.setFeatures(features);
		return featureCollection;
	}
	
	public static FeatureCollection fromJsonString(String jsonString) throws JSONException {
		return fromJson(new JSONArray(jsonString));
	}
	
	public JSONArray toJson() {
		JSONArray jsonArray = new JSONArray();
		
		return jsonArray;
	}
	
}
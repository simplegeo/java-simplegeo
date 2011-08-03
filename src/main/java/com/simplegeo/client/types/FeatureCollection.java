package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeatureCollection {

	private ArrayList<Feature> features;
	private String cursor;

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
	
	/**
	 * @param cursor the cursor to set
	 */
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	
	/**
	 * @return the cursor
	 */
	public String getCursor() {
		return cursor;
	}	

	public static FeatureCollection fromJSON(JSONObject json) throws JSONException {
		FeatureCollection featureCollection = new FeatureCollection();
		ArrayList<Feature> features = new ArrayList<Feature>();
		JSONArray featuresArray = json.getJSONArray("features");
		int numOfFeatures = featuresArray.length();
		for (int i=0; i<numOfFeatures; i++) {
			features.add(Feature.fromJSON(featuresArray.getJSONObject(i)));
		}
		featureCollection.setFeatures(features);
		featureCollection.setCursor(json.optString("cursor"));
		
		return featureCollection;
	}
	
	public static FeatureCollection fromJSONString(String jsonString) throws JSONException {
		return fromJSON(new JSONObject(jsonString));
	}
	
	public JSONArray toJSON() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Feature> features = this.getFeatures();
		for (Feature feature : features) {
			jsonArray.put(feature.toJSON());
		}
		return jsonArray;
	}
	
	public String toJSONString() throws JSONException {
		return this.toJSON().toString();
	}
	
}
/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import org.apache.log4j.Logger;

import com.simplegeo.client.encoder.GeoJSONEncoder;



/**
 * @author Derek Smith
 *
 */
public class GeoJSONObject extends JSONObject {
	
	private static final String TAG = GeoJSONObject.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONObject.class);
	
	public GeoJSONObject(String type, String jsonString) throws JSONException {
		// Ignoring the type for the moment
		super(jsonString);		
	}
	
	
	public GeoJSONObject(String type) {
		setupStructure(type);
	}
	
	public GeoJSONObject() {
		setupStructure("Feature");
	}
	
	private void setupStructure(String type) {
		
		try {
			
			this.put("type",type);
			
			if(type.equals("Feature")) {
				
				JSONObject geometry = new JSONObject();
				geometry.putOpt("type", "Point");
				setGeometry(geometry);
				JSONArray coords = new JSONArray("[0.0,0.0]");
				setCoordinates(coords);
				setProperties(new JSONObject());
				
			} else if(type.equals("FeatureCollection")) {
				
				setFeatures(new JSONArray());
				
			}
			
		} catch (JSONException e) {
			
			logger.debug("unable to initialize properly");
		}

	}
	
	public boolean isFeature() {
		boolean isFeature = false;
		try {
			String type = this.getType();
			isFeature = type.equals("Feature");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return isFeature;
	}
	
	public boolean isFeatureCollection() {
		boolean isFeatureCollection = false;
		try {
			String type = this.getType();
			isFeatureCollection = type.equals("FeatureCollection");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return isFeatureCollection;
	}

	public double getLatitude() throws JSONException {
		return this.getCoordinates().getDouble(1);
	}
	
	public void setLatitude(double latitude) throws JSONException {
		this.getCoordinates().put(1, latitude);
	}
	
	public double getLongitude() throws JSONException {
		return this.getCoordinates().getDouble(0);
	}
	
	public void setLongitude(double longitude) throws JSONException {
		this.getCoordinates().put(0, longitude);
	}
	
	public JSONArray getCoordinates() throws JSONException {
		return this.getGeometry().getJSONArray("coordinates");
	}
	
	public void setCoordinates(JSONArray coordinates) throws JSONException {
		this.getGeometry().put("coordinates", coordinates);
	}
	
	public JSONObject getGeometry() throws JSONException {
		return this.getJSONObject("geometry");
	}
	
	public void setGeometry(JSONObject geometry) throws JSONException {
		this.put("geometry", geometry);
	}
	
	public String getGeometryType() throws JSONException {
		return this.getGeometry().getString("type");
	}
	
	public void setGeometryType(String type) throws JSONException {
		this.getGeometry().put("type", type);
	}
	
	public String getType() throws JSONException {
		return this.getString("type");
	}
	
	public void setType(String type) throws JSONException {
		this.put("type", type);
	}
	
	public JSONObject getProperties() throws JSONException {
		return this.getJSONObject("properties");
	}
	
	public void setProperties(JSONObject properties) throws JSONException {
		this.put("properties", properties);
	}
	
	public void setFeatures(JSONArray features) throws JSONException {
		if(isFeatureCollection())
			this.putOpt("features", features);
		else
			throw new JSONException("GeoJSONObject is not of type FeatureCollection");
	}
	
	public JSONArray getFeatures() throws JSONException {
		if(isFeatureCollection())
			return (JSONArray)this.get("features");
		
		throw new JSONException("GeoJSONObject is not of type FeatureCollection");
	}
	
	// Ummmmm yea.
	public void flatten() throws JSONException {
		
		if(isFeatureCollection()) {
			
			List<GeoJSONObject> newFeatures = new ArrayList<GeoJSONObject>();
			JSONArray features = getFeatures();
			int length = features.length();
			for(int index = 0; index < length; index++) {
				
				GeoJSONObject geoJSONObject = (GeoJSONObject)features.get(index);
				if(geoJSONObject.isFeatureCollection()) {
					JSONArray subfeatures = geoJSONObject.getFeatures();
					int sublength = subfeatures.length();
					for(int subindex = 0; subindex < sublength; subindex++) {
						GeoJSONObject subGeoJSONObject = (GeoJSONObject)subfeatures.get(subindex);
						subGeoJSONObject.flatten();
						newFeatures.add(subGeoJSONObject);
					}
					
				}
			}
			
			if(!newFeatures.isEmpty()) {
				for(GeoJSONObject object : newFeatures)
					features.put(object);
			}
		}
		
	}
	
	public JSONArray geoJSONArray(JSONArray jsonArray) throws JSONException {
		
		JSONArray geoJSONArray = new JSONArray();
		int length = jsonArray.length();
		for(int index = 0; index < length; index++) {
			jsonArray.put(new GeoJSONObject("Feature", jsonArray.get(index).toString()));
		}
		
		return geoJSONArray;
	}
}

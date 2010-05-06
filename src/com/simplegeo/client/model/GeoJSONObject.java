/**
 * Copyright (c) 2009-2010, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simplegeo.client.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import org.apache.log4j.Logger;


/**
 * A subclass of {@link org.json.JSONObject} that enables
 * easier access to GeoJSON specific key/value pairs.
 * 
 * The current implementation only supports two types:
 * <ul>
 * <li>Feature</li>
 * <li>FeatureCollection</li>
 * </ul>
 * 
 * @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a>
 * 
 * @author Derek Smith
 */
public class GeoJSONObject extends JSONObject {
	
	private static final String TAG = GeoJSONObject.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONObject.class);
	
	/**
	 * Initializes the GeoJSON object with the given JSON string
	 * representation.
	 * 
	 * @param type the type of GeoJSON object (ignored for now) 
	 * @param jsonString a string representation of a GeoJSON object
	 * @throws JSONException
	 */
	public GeoJSONObject(String type, String jsonString) throws JSONException {
		// Ignoring the type for the moment
		super(jsonString);		
	}
	
	
	/**
	 * @param type the type of GeoJSON object to initialize
	 * @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a>
	 */
	public GeoJSONObject(String type) {
		setupStructure(type);
	}
	
	/**
	 * Initializes a new object of type Feature.
	 */
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
	
	/**
	 * @return true if the object is of type Feature; otherwise false
	 */
	public boolean isFeature() {
		return isType("Feature");
	}
	
	/**
	 * @return true if the object is of type FeatureCollection; otherwise false
	 */
	public boolean isFeatureCollection() {
		return isType("FeatureCollection");
	}
	
	/**
	 * @return true if the object is of type MultiPolygon; otherwise false
	 */
	public boolean isMultiPolygon() {
		return isType("MultiPolygon");
	}

	/**
	 * @return true if the object is of type Polygon; otherwise false
	 */
	public boolean isPolygon() {
		return isType("Polygon");
	}

	private boolean isType(String type) {
		boolean isType = false;
		try {
			String objectType = this.getType();
			isType = objectType.equals(type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return isType;		
	}
	
	/**
	 * @return the latitude of the record
	 * @throws JSONException
	 */
	public double getLatitude() throws JSONException {
		return this.getCoordinates().getDouble(1);
	}
	
	/**
	 * @param latitude the latitude of the record
	 * @throws JSONException
	 */
	public void setLatitude(double latitude) throws JSONException {
		this.getCoordinates().put(1, latitude);
	}
	
	/**
	 * @return the longitude of the record
	 * @throws JSONException
	 */
	public double getLongitude() throws JSONException {
		return this.getCoordinates().getDouble(0);
	}
	
	/**
	 * @param longitude the longitude of the record
	 * @throws JSONException
	 */
	public void setLongitude(double longitude) throws JSONException {
		this.getCoordinates().put(0, longitude);
	}
	
	/**
	 * @return @see <a href="http://geojson.org/geojson-spec.html#point"</a>
	 * @throws JSONException
	 */
	public JSONArray getCoordinates() throws JSONException {
		return this.getGeometry().getJSONArray("coordinates");
	}
	
	/**
	 * @param coordinates @see <a href="http://geojson.org/geojson-spec.html#point"</a>
	 * @throws JSONException
	 */
	public void setCoordinates(JSONArray coordinates) throws JSONException {
		this.getGeometry().put("coordinates", coordinates);
	}
	
	/**
	 * @return @see <a href="http://geojson.org/geojson-spec.html#geometry-objects"</a>
	 * @throws JSONException
	 */
	public JSONObject getGeometry() throws JSONException {
		return this.getJSONObject("geometry");
	}
	
	/**
	 * @param geometry
	 * @throws JSONException
	 * @see <a href="http://geojson.org/geojson-spec.html#geometry-objects"</a>
	 */
	public void setGeometry(JSONObject geometry) throws JSONException {
		this.put("geometry", geometry);
	}
	
	/**
	 * @return @see <a href="http://geojson.org/geojson-spec.html#geometry-objects"</a> 
	 * @throws JSONException
	 */
	public String getGeometryType() throws JSONException {
		return this.getGeometry().getString("type");
	}
	
	/**
	 * Only type "Point" is currently supported.
	 * 
	 * @see <a href="http://geojson.org/geojson-spec.html#point"</a>
	 * 
	 * @param type the type of geometry that is associated with the record
	 * @throws JSONException
	 */
	public void setGeometryType(String type) throws JSONException {
		this.getGeometry().put("type", type);
	}
	
	/**
	 * @return @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a>
	 * @throws JSONException
	 */
	public String getType() throws JSONException {
		return this.getString("type");
	}
	
	/**
	 * @param type either the value "Feature" or "FeatureCollection"
	 * @throws JSONException
	 * @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a>
	 */
	public void setType(String type) throws JSONException {
		this.put("type", type);
	}
	
	/**
	 * @return @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a> 
	 * @throws JSONException
	 */
	public JSONObject getProperties() throws JSONException {
		return this.getJSONObject("properties");
	}
	
	/**
	 * @param properties the JSONArray to set at key "properties"
	 * @throws JSONException
	 * @see <a href="http://geojson.org/geojson-spec.html#feature-objects"</a>
	 */
	public void setProperties(JSONObject properties) throws JSONException {
		this.put("properties", properties);
	}
	
	/**
	 * @param features the {@link org.json.JSONArray} to set at key "features"
	 * @throws JSONException
	 * @see <a href="http://geojson.org/geojson-spec.html#feature-collection-objects"</a>
	 */
	public void setFeatures(JSONArray features) throws JSONException {
		if(isFeatureCollection())
			this.putOpt("features", features);
		else
			throw new JSONException("GeoJSONObject is not of type FeatureCollection");
	}
	
	/**
	 * @return if the GeoJSON object is of type FeatureCollection, then this will
	 * be a {@link org.json.JSONArray}
	 * @throws JSONException
	 * @see <a href="http://geojson.org/geojson-spec.html#feature-collection-objects"</a>
	 */
	public JSONArray getFeatures() throws JSONException {
		if(isFeatureCollection())
			return (JSONArray)this.get("features");
		
		throw new JSONException("GeoJSONObject is not of type FeatureCollection");
	}
	
	/**
	 * Attempts to flatten the GeoJSON object if it is a FeatureCollection
	 * of FeatureCollections.
	 * 
	 * @throws JSONException
	 */
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

/**
 * Copyright (c) 2010-2011, SimpleGeo
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

package com.simplegeo.client.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feature {

	private String simpleGeoId;
	private Geometry geometry;
	private String type;
	private HashMap<String, Object> properties;
	
	public Feature() {
		
	}
	
	public Feature(String simpleGeoId, Geometry geometry, String type, HashMap<String, Object> properties) {
		this.simpleGeoId = simpleGeoId;
		this.geometry = geometry;
		this.type = type;
		this.properties = properties;
	}
	
	public String getSimpleGeoId() {
		return simpleGeoId;
	}

	public void setSimpleGeoId(String simpleGeoId) {
		this.simpleGeoId = simpleGeoId;
	}
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	
	public static Feature fromJson(JSONObject json) throws JSONException {
		Feature feature = new Feature();
		feature.setSimpleGeoId(json.getString("id"));
		feature.setType("Feature");
		JSONObject jsonGeometry = json.getJSONObject("geometry");
		if ("Point".equals(jsonGeometry.getString("type"))) {
			JSONArray coordinates = jsonGeometry.getJSONArray("coordinates");
			Point point = new Point(coordinates.getDouble(0), coordinates.getDouble(1));
			feature.setGeometry(new Geometry(point));
		} else {
			JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
			Polygon polygon = new Polygon(Polygon.fromJsonArray(polygonArray));
			feature.setGeometry(new Geometry(polygon));
		}
		
		HashMap<String, Object> propertyMap = new HashMap<String, Object>();
		JSONObject properties = json.getJSONObject("properties");
		Iterator<String> propertyIterator = properties.sortedKeys();
		while (propertyIterator.hasNext()) {
			String key = (String) propertyIterator.next();
			propertyMap.put(key, properties.get(key));
		}
		feature.setProperties(propertyMap);
		return feature;
	}
	
	public static Feature fromJsonString(String jsonString) throws JSONException {
		return fromJson(new JSONObject(jsonString));
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("geometry", this.getGeometryJSON());
		json.put("type", "Feature");
		json.put("id", this.getSimpleGeoId());
		json.put("properties", this.getPropertyJSON());
		return json;
	}
	
	public String toJsonString() throws JSONException {
		return this.toJson().toString();
	}
	
	private JSONObject getGeometryJSON() throws JSONException {
		JSONObject json = new JSONObject();
		Geometry geometry = this.getGeometry();
		if (geometry.getPoint() != null) {
			json.put("type", "Point");
			json.put("coordinates", geometry.getPoint().toJSONArray());
		} else {
			json.put("type", "Polygon");
			json.put("coordinates", geometry.getPolygon().toJSONArray());
		}
		return json;
	}
	
	private JSONObject getPropertyJSON() throws JSONException {
		JSONObject json = new JSONObject();
		HashMap<String, Object> properties = this.getProperties();
		Set<String> keys = properties.keySet();
		for (String key : keys) {
			json.put(key, properties.get(key));
		}
		return json;
	}
	
}
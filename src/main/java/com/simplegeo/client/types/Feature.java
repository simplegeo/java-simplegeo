package com.simplegeo.client.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feature {

	protected String simpleGeoId;
	protected Geometry geometry;
	protected String type;
	protected HashMap<String, Object> properties;
	
	public Feature() {
		
	}
	
	public Feature(Geometry geometry, String type, HashMap<String, Object> properties) {
		this.geometry = geometry;
		this.type = type;
		this.properties = properties;
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
	
	public static Feature fromJSON(JSONObject json) throws JSONException {
		Feature feature = new Feature();
		feature.setSimpleGeoId(json.optString("id"));
		feature.setType("Feature");
		JSONObject jsonGeometry = json.getJSONObject("geometry");
		if ("Point".equals(jsonGeometry.getString("type"))) {
			JSONArray coordinates = jsonGeometry.getJSONArray("coordinates");
			Point point = new Point(coordinates.getDouble(1), coordinates.getDouble(0));
			feature.setGeometry(new Geometry(point));
		} else if ("Polygon".equals(jsonGeometry.getString("type"))) {
			JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
			Polygon polygon = Polygon.fromJSONArray(polygonArray);
			feature.setGeometry(new Geometry(polygon));
		} else {
			JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
			MultiPolygon multiPolygon = MultiPolygon.fromJSONArray(polygonArray);
			feature.setGeometry(new Geometry(multiPolygon));
		}
		
		HashMap<String, Object> propertyMap = new HashMap<String, Object>();
		JSONObject properties = json.getJSONObject("properties");
		Iterator<String> propertyIterator = properties.keys();
		while (propertyIterator.hasNext()) {
			String key = (String) propertyIterator.next();
			propertyMap.put(key, properties.get(key));
		}
		feature.setProperties(propertyMap);
		return feature;
	}
	
	public static Feature fromJSONString(String jsonString) throws JSONException {
		return fromJSON(new JSONObject(jsonString));
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("geometry", this.getGeometryJSON());
		json.put("type", "Feature");
		if (this.getSimpleGeoId() != null) { json.put("id", this.getSimpleGeoId()); }
		json.put("properties", this.getPropertyJSON());
		return json;
	}
	
	public String toJSONString() throws JSONException {
		return this.toJSON().toString();
	}
	
	private JSONObject getGeometryJSON() throws JSONException {
		JSONObject json = new JSONObject();
		Geometry geometry = this.getGeometry();
		if (geometry.getPoint() != null) {
			json.put("type", "Point");
			json.put("coordinates", geometry.getPoint().toJSONArray());
		} else if (geometry.getPolygon() != null){
			json.put("type", "Polygon");
			json.put("coordinates", geometry.getPolygon().toJSONArray());
		} else {
			json.put("type", "MultiPolygon");
			json.put("coordinates", geometry.getMultiPolygon().toJSONArray());
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
package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Collection of {@link com.simplegeo.client.types.Geometry} objects
 * 
 * @author Pranil Kanderi
 *
 */
public class GeometryCollection {

	private ArrayList<Geometry> geometries;
	private String cursor;
	
	public GeometryCollection() {
		
	}
	
	public GeometryCollection(ArrayList<Geometry> geometries) {
		this.setGeometries(geometries);
	}

	/**
	 * @param geometries the geometries to set
	 */
	public void setGeometries(ArrayList<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * @return the geometries
	 */
	public ArrayList<Geometry> getGeometries() {
		return geometries;
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
	
	public static GeometryCollection fromJSONString(String response) throws JSONException {
		JSONObject jsonObject = new JSONObject(response);
		
		JSONArray jsonArray = jsonObject.getJSONArray("geometries");
		
		GeometryCollection geometryCollection = new GeometryCollection();
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			
			Geometry geometry = new Geometry();
			JSONObject jsonGeometry = jsonArray.getJSONObject(i);
			
			if ("Point".equals(jsonGeometry.getString("type"))) {
				JSONArray coordinates = jsonGeometry.getJSONArray("coordinates");
				Point point = new Point(coordinates.getDouble(1), coordinates.getDouble(0));
				geometry = new Geometry(point);
			} else if ("Polygon".equals(jsonGeometry.getString("type"))) {
				JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
				Polygon polygon = Polygon.fromJSONArray(polygonArray);
				geometry = new Geometry(polygon);
			} else {
				JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
				MultiPolygon multiPolygon = MultiPolygon.fromJSONArray(polygonArray);
				geometry = new Geometry(multiPolygon);
			}
			
			geometry.setCreated(jsonGeometry.getLong("created"));			
			geometries.add(geometry);
		}
		geometryCollection.setCursor(new JSONObject(response).optString("cursor"));
		geometryCollection.setGeometries(geometries);
		
		return geometryCollection;
	}

}

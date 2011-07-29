package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class MultiPolygon {
	
	private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	
	public MultiPolygon() {
		
	}
	
	public MultiPolygon(ArrayList<Polygon> polygons) {
		this.polygons = polygons;
	}

	public ArrayList<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(ArrayList<Polygon> polygons) {
		this.polygons = polygons;
	}
	
	public static MultiPolygon fromJSONArray(JSONArray jsonArray) throws JSONException {
		ArrayList<Polygon> polygonList = new ArrayList<Polygon>();
		int numOfPolygons = jsonArray.length();
		for (int i=0; i<numOfPolygons; i++) {
			polygonList.add(Polygon.fromJSONArray(jsonArray.getJSONArray(i)));
		}
		return new MultiPolygon(polygonList);
	}
	
	public JSONArray toJSONArray() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Polygon> polygons = this.getPolygons();
		for (Polygon polygon : polygons) {
			jsonArray.put(polygon.toJSONArray());
		}
		return jsonArray;
	}

}
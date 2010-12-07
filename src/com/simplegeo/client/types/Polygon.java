package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Polygon {

	private ArrayList<ArrayList<Point>> rings;
	
	public Polygon() {
		
	}
	
	public Polygon(ArrayList<ArrayList<Point>> rings) {
		this.rings = rings;
	}

	public ArrayList<ArrayList<Point>> getRings() {
		return rings;
	}

	public void setRings(ArrayList<ArrayList<Point>> rings) {
		this.rings = rings;
	}
	
	public static ArrayList<ArrayList<Point>> fromJsonArray(JSONArray polygonArray) throws JSONException {
		ArrayList<ArrayList<Point>> ringList = new ArrayList<ArrayList<Point>>();
		int numOfRings = polygonArray.length();
		for (int i=0; i<numOfRings; i++) {
			JSONArray ring = polygonArray.getJSONArray(i);
			int numOfCoords = ring.length();
			ArrayList<Point> pointList = new ArrayList<Point>();
			for (int j=0; j<numOfCoords; j++) {
				JSONArray coords = ring.getJSONArray(j);
				pointList.add(new Point(coords.getDouble(0), coords.getDouble(1)));
			}
			ringList.add(pointList);
		}
		return ringList;
	}
	
	public JSONArray toJSONArray() throws JSONException {
		JSONArray polygonArray = new JSONArray();
		JSONArray rings = new JSONArray();
		ArrayList<ArrayList<Point>> ringList = this.getRings();
		for (ArrayList<Point> ring : ringList) {
			int numOfPoints = ring.size();
			for (int i=0; i<numOfPoints; i++) {
				Point point = ring.get(i);
				rings.put(point.toJSONArray());
			}
		}
		polygonArray.put(rings);
		return polygonArray;
	}
	
}
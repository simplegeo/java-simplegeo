package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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
	
	public static Polygon fromJSONArray(JSONArray polygonArray) throws JSONException {
		ArrayList<ArrayList<Point>> ringList = new ArrayList<ArrayList<Point>>();
		int numOfRings = polygonArray.length();
		for (int i=0; i<numOfRings; i++) {
			JSONArray ring = polygonArray.getJSONArray(i);
			int numOfCoords = ring.length();
			ArrayList<Point> pointList = new ArrayList<Point>();
			for (int j=0; j<numOfCoords; j++) {
				JSONArray coords = ring.getJSONArray(j);
				pointList.add(new Point(coords.getDouble(1), coords.getDouble(0)));
			}
			ringList.add(pointList);
		}
		return new Polygon(ringList);
	}
	
	public JSONArray toJSONArray() throws JSONException {
		JSONArray rings = new JSONArray();
		ArrayList<ArrayList<Point>> ringList = this.getRings();
		for (ArrayList<Point> ring : ringList) {
			JSONArray r = new JSONArray();
			int numOfPoints = ring.size();
			for (int i=0; i<numOfPoints; i++) {
				Point point = ring.get(i);
				r.put(point.toJSONArray());
			}
			rings.put(r);
		}
		return rings;
	}
	
}
package com.simplegeo.client.types;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;

public class MultiPolygonTest extends TestCase {

	public void testToJSONArray() {
		try {
			MultiPolygon multiPolygon = new MultiPolygon();
			multiPolygon.getPolygons().add(this.generatePolygon());
			multiPolygon.getPolygons().add(this.generatePolygon());
			JSONArray jsonArray = multiPolygon.toJSONArray();
			TestCase.assertEquals(2, jsonArray.length());
			TestCase.assertEquals(5, jsonArray.getJSONArray(0).length());
			TestCase.assertEquals(5, jsonArray.getJSONArray(1).length());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testFromJSONArray() {
		try {
			JSONArray jsonArray = new JSONArray("[[[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]], [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],[[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]]");
			MultiPolygon multiPolygon = MultiPolygon.fromJSONArray(jsonArray);
			TestCase.assertEquals(2, multiPolygon.getPolygons().size());
			TestCase.assertEquals(1, multiPolygon.getPolygons().get(0).getRings().size());
			TestCase.assertEquals(2, multiPolygon.getPolygons().get(1).getRings().size());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	private Polygon generatePolygon() {
		ArrayList<ArrayList<Point>> rings = new ArrayList<ArrayList<Point>>();
		for (int i=0; i<5; i++) {
			rings.add(this.generateRing());
		}
		return new Polygon(rings);
	}
	
	private ArrayList<Point> generateRing() {
		ArrayList<Point> ring = new ArrayList<Point>();
		for (int i=0; i<5; i++) {
			ring.add(new Point(37.0, -110.0));
		}
		return ring;
	}
	
}

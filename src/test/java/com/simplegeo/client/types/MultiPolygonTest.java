package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class MultiPolygonTest {

	@Test
	public void testToJSONArray() {
		try {
			MultiPolygon multiPolygon = new MultiPolygon();
			multiPolygon.getPolygons().add(this.generatePolygon());
			multiPolygon.getPolygons().add(this.generatePolygon());
			JSONArray jsonArray = multiPolygon.toJSONArray();
			Assert.assertEquals(2, jsonArray.length());
			Assert.assertEquals(5, jsonArray.getJSONArray(0).length());
			Assert.assertEquals(5, jsonArray.getJSONArray(1).length());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFromJSONArray() {
		try {
			JSONArray jsonArray = new JSONArray("[[[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]], [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],[[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]]");
			MultiPolygon multiPolygon = MultiPolygon.fromJSONArray(jsonArray);
			Assert.assertEquals(2, multiPolygon.getPolygons().size());
			Assert.assertEquals(1, multiPolygon.getPolygons().get(0).getRings().size());
			Assert.assertEquals(2, multiPolygon.getPolygons().get(1).getRings().size());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
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

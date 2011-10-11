package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class PolygonTest {

	@Test
	public void testToJSONArray() {
		try {
			ArrayList<ArrayList<Point>> rings = new ArrayList<ArrayList<Point>>();
			rings.add(this.generateRing());
			Polygon polygon = new Polygon(rings);
			JSONArray jsonArray = polygon.toJSONArray();
			Assert.assertEquals(1, jsonArray.length());
			JSONArray ringArray = jsonArray.getJSONArray(0);
			Assert.assertEquals(5, ringArray.length());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testToJSONArrayMultipleRings() {
		try {
			ArrayList<ArrayList<Point>> rings = new ArrayList<ArrayList<Point>>();
			rings.add(this.generateRing());
			rings.add(this.generateRing());
			Polygon polygon = new Polygon(rings);
			JSONArray jsonArray = polygon.toJSONArray();
			Assert.assertEquals(2, jsonArray.length());
			JSONArray ringArray = jsonArray.getJSONArray(0);
			Assert.assertEquals(5, ringArray.length());
			ringArray = jsonArray.getJSONArray(1);
			Assert.assertEquals(5, ringArray.length());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFromJSONArray() {
		try {
			JSONArray jsonArray = new JSONArray("[[[-122.444406,37.759271],[-122.444291,37.759487],[-122.444127,37.759709],[-122.443135,37.760357],[-122.442843,37.760444],[-122.441282,37.76053],[-122.440682,37.76043],[-122.439282,37.76133],[-122.438082,37.76163],[-122.436982,37.76183],[-122.435882,37.76193],[-122.434982,37.762429],[-122.432982,37.764229],[-122.430982,37.765729],[-122.430682,37.765929],[-122.430382,37.766029],[-122.428882,37.767429],[-122.428482,37.766129],[-122.428382,37.764629],[-122.428328,37.763683],[-122.428277,37.762825],[-122.428182,37.762029],[-122.428082,37.761229],[-122.428082,37.76053],[-122.427882,37.75963],[-122.427882,37.75893],[-122.427782,37.75803],[-122.425582,37.75823],[-122.423082,37.75833],[-122.422344,37.758422],[-122.422282,37.75843],[-122.421382,37.75853],[-122.421282,37.75773],[-122.421282,37.75693],[-122.422186,37.756874],[-122.422991,37.756814],[-122.423615,37.756757],[-122.424182,37.75673],[-122.42479,37.756688],[-122.425482,37.75663],[-122.426682,37.75653],[-122.427782,37.75653],[-122.429882,37.75643],[-122.431582,37.75633],[-122.432102,37.756266],[-122.434386,37.756119],[-122.435374,37.756034],[-122.436516,37.755973],[-122.437597,37.755899],[-122.43752,37.75546],[-122.438682,37.75533],[-122.439682,37.75523],[-122.440282,37.75513],[-122.439882,37.75633],[-122.440219,37.756934],[-122.441403,37.75808],[-122.441961,37.758277],[-122.442982,37.75883],[-122.443482,37.75903],[-122.444406,37.759271]]]");
			Polygon polygon = Polygon.fromJSONArray(jsonArray);
			Assert.assertEquals(1, polygon.getRings().size());
			ArrayList<Point> points = polygon.getRings().get(0);
			Assert.assertEquals(60, points.size());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testFromJSONArrayMultipleRings() {
		try {
			JSONArray jsonArray = new JSONArray("[[[-122.444406,37.759271],[-122.444291,37.759487],[-122.444127,37.759709],[-122.443135,37.760357],[-122.442843,37.760444],[-122.441282,37.76053],[-122.440682,37.76043],[-122.439282,37.76133],[-122.438082,37.76163],[-122.436982,37.76183],[-122.435882,37.76193],[-122.434982,37.762429],[-122.432982,37.764229],[-122.430982,37.765729],[-122.430682,37.765929],[-122.430382,37.766029],[-122.428882,37.767429],[-122.428482,37.766129],[-122.428382,37.764629],[-122.428328,37.763683],[-122.428277,37.762825],[-122.428182,37.762029],[-122.428082,37.761229],[-122.428082,37.76053],[-122.427882,37.75963],[-122.427882,37.75893],[-122.427782,37.75803],[-122.425582,37.75823],[-122.423082,37.75833],[-122.422344,37.758422],[-122.422282,37.75843],[-122.421382,37.75853],[-122.421282,37.75773],[-122.421282,37.75693],[-122.422186,37.756874],[-122.422991,37.756814],[-122.423615,37.756757]],[[-122.424182,37.75673],[-122.42479,37.756688],[-122.425482,37.75663],[-122.426682,37.75653],[-122.427782,37.75653],[-122.429882,37.75643],[-122.431582,37.75633],[-122.432102,37.756266],[-122.434386,37.756119],[-122.435374,37.756034],[-122.436516,37.755973],[-122.437597,37.755899],[-122.43752,37.75546],[-122.438682,37.75533],[-122.439682,37.75523],[-122.440282,37.75513],[-122.439882,37.75633],[-122.440219,37.756934],[-122.441403,37.75808],[-122.441961,37.758277],[-122.442982,37.75883],[-122.443482,37.75903],[-122.444406,37.759271]]]");
			Polygon polygon = Polygon.fromJSONArray(jsonArray);
			Assert.assertEquals(2, polygon.getRings().size());
			ArrayList<Point> points = polygon.getRings().get(0);
			Assert.assertEquals(37, points.size());
			ArrayList<Point> points2 = polygon.getRings().get(1);
			Assert.assertEquals(23, points2.size());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	private ArrayList<Point> generateRing() {
		ArrayList<Point> ring = new ArrayList<Point>();
		for (int i=0; i<5; i++) {
			ring.add(new Point(37.0, -110.0));
		}
		return ring;
	}
	
}

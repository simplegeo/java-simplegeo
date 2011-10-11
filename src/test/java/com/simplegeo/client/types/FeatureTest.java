package com.simplegeo.client.types;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.simplegeo.client.test.TestEnvironment;

public class FeatureTest {
	
	@Test
	public void testFeatureConversionPoint() {
		try {
			String jsonString = TestEnvironment.getJsonPointString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			Assert.assertNotNull(actual.get("id"));
			Assert.assertNotNull(actual.get("properties"));
			Assert.assertNotNull(actual.get("type"));
			Assert.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureConversionPolygon() {
		try {
			String jsonString = TestEnvironment.getJsonPolygonString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			Assert.assertNotNull(actual.get("id"));
			Assert.assertNotNull(actual.get("properties"));
			Assert.assertNotNull(actual.get("type"));
			Assert.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFeatureConversionMultiPolygon() {
		try {
			String jsonString = TestEnvironment.getJsonMultipolygonString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			Assert.assertNotNull(actual.get("id"));
			Assert.assertNotNull(actual.get("properties"));
			Assert.assertNotNull(actual.get("type"));
			Assert.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
}

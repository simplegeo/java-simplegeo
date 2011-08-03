package com.simplegeo.client.types;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.test.TestEnvironment;

public class FeatureTest extends TestCase {
	
	public void testFeatureConversionPoint() {
		try {
			String jsonString = TestEnvironment.getJsonPointString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			TestCase.assertNotNull(actual.get("id"));
			TestCase.assertNotNull(actual.get("properties"));
			TestCase.assertNotNull(actual.get("type"));
			TestCase.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testFeatureConversionPolygon() {
		try {
			String jsonString = TestEnvironment.getJsonPolygonString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			TestCase.assertNotNull(actual.get("id"));
			TestCase.assertNotNull(actual.get("properties"));
			TestCase.assertNotNull(actual.get("type"));
			TestCase.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testFeatureConversionMultiPolygon() {
		try {
			String jsonString = TestEnvironment.getJsonMultipolygonString();
			Feature feature = Feature.fromJSONString(jsonString);
			String featureString = feature.toJSONString();
			JSONObject actual = new JSONObject(featureString);
			TestCase.assertNotNull(actual.get("id"));
			TestCase.assertNotNull(actual.get("properties"));
			TestCase.assertNotNull(actual.get("type"));
			TestCase.assertNotNull(actual.get("geometry"));
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
}

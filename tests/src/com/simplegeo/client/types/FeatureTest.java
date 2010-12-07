package com.simplegeo.client.types;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.test.TestEnvironment;

public class FeatureTest extends TestCase {
	
	public void testFeatureConversionPoint() {
		try {
			String jsonString = TestEnvironment.getJsonPointString();
			Feature feature = Feature.fromJsonString(jsonString);
			String featureString = feature.toJsonString();
			String expected = new JSONObject(jsonString).toString();
			String actual = new JSONObject(featureString).toString();
			this.assertEquals(expected, actual);
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testFeatureConversionPolygon() {
		try {
			String jsonString = TestEnvironment.getJsonPolygonString();
			Feature feature = Feature.fromJsonString(jsonString);
			String featureString = feature.toJsonString();
			String expected = new JSONObject(jsonString).toString();
			String actual = new JSONObject(featureString).toString();
			this.assertEquals(expected, actual);
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
}

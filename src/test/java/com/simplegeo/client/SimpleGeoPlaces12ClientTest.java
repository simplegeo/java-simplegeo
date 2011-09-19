package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

public class SimpleGeoPlaces12ClientTest {

	protected static SimpleGeoPlaces12Client client;
	
	@BeforeClass
	public static void setUp() throws Exception {
		setupClient();
	}
	
	private static void setupClient() throws Exception {
		client = new SimpleGeoPlaces12Client();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}

	@Test
	public void testGetFeaturePointSync() {
		try {
			String jsonString = client.getFeature("SG_12NOI6r1xICCpP4bMdyHsq_37.778644_-122.389380@1303263314");
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertEquals("SG_12NOI6r1xICCpP4bMdyHsq_37.778644_-122.389380@1303263314", json.get("id"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	@Test
	public void testGetFactualFeaturePointSync() {
		try {
			String jsonString = client.getFeature("b69900d4-4490-4b56-9a1d-c00a86b8c322");
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertEquals("b69900d4-4490-4b56-9a1d-c00a86b8c322", json.get("id"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	@Test
	public void testGetPlacePolygonSync() {
		try {
			String jsonString = client.getFeature("SG_0Bw22I6fWoxnZ4GDc8YlXd");
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertEquals("SG_0Bw22I6fWoxnZ4GDc8YlXd", json.get("id"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	@Test
	public void testSearchSync() {
		double lat = 37.772381;
		double lon = -122.405827;
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("category", new String[] {"Restaurants"});
			params.put("radius", new String[] {"100"});
			String jsonString = client.search(lat, lon, params);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json);
			
			FeatureCollection collection = FeatureCollection.fromJSON(json);
			
			TestCase.assertNotNull(collection);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSearchNonGeoSync() {
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("limit", new String[] { "10" });
			String jsonString = client.search("salad dressing", params);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json);
			
			FeatureCollection collection = FeatureCollection.fromJSON(json);
			
			TestCase.assertNotNull(collection);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	@Test
	public void testSearchByAddressSync() {
		String address = "1535 Pearl St, Boulder, CO";
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("radius", new String[] {"100"});
			String jsonString = client.searchByAddress(address, params);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json);
			
			FeatureCollection collection = FeatureCollection.fromJSON(json);
			
			TestCase.assertNotNull(collection);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
}

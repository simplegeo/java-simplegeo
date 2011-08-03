package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Feature;

public class SimpleGeoPlacesClientTest extends TestCase {

	protected SimpleGeoPlacesClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = new SimpleGeoPlacesClient();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}

	public void testGetCategoriesSync() {
		try {
			String jsonString = client.getCategories();
			JSONArray json = new JSONArray(jsonString);
			
			TestCase.assertTrue(json.length() > 0);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

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

	public void testAddUpdateDeletePlaceSync() {
		try {
			JSONObject origJson = new JSONObject(TestEnvironment.getJsonPointStringNoId());
			Feature feature = Feature.fromJSON(origJson);
			String jsonString = client.addPlace(feature);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("id"));
			String newPlaceHandle = json.getString("id");
			
			feature = Feature.fromJSON(origJson);
			feature.setSimpleGeoId(newPlaceHandle);
			jsonString = client.updatePlace(feature);
			
			TestCase.assertNotNull(json.get("id"));
			TestCase.assertEquals(newPlaceHandle, json.getString("id"));
			
			jsonString = client.deletePlace(newPlaceHandle);
			json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("token"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testSearchSync() {
		double lat = 37.759737;
		double lon = -122.433203;
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("category", new String[] {"Restaurants"});
			params.put("radius", new String[] {"25"});
			String jsonString = client.search(lat, lon, params);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testSearchByAddressSync() {
		String address = "1535 Pearl St, Boulder, CO";
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("radius", new String[] {"25"});
			String jsonString = client.searchByAddress(address, params);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
}
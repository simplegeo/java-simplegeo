package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.test.TestEnvironment;

public class SimpleGeoContextClientTest extends TestCase {
	
	protected SimpleGeoContextClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = new SimpleGeoContextClient();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetContextSync() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			String jsonString = client.getContext(lat, lon, null);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
		} catch (IOException e) {	
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByMyIPSync() {
		try {
			String jsonString = client.getContextByIP("", null);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByIPSync() {
		try {
			String jsonString = client.getContextByIP("92.156.43.27", null);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByAddressSync() {
		try {
			String jsonString = client.getContextByAddress("41 Decatur St, San Francisco, CA", null);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
		
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetFilteredContext() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			queryParams.put("filter", new String[] {"weather", "features"});
			queryParams.put("features__category", new String[] {"Neighborhood"});
			String jsonString = client.getContext(lat, lon, queryParams);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("features"));
			
			TestCase.assertNull(json.opt("demographics"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetDemographics() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			queryParams.put("filter", new String[] {"demographics.acs"});
			queryParams.put("demographics.acs__table", new String[] {"B08012"});
			String jsonString = client.getContext(lat, lon, queryParams);
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertNotNull(json.get("demographics"));
			
			TestCase.assertNull(json.opt("weather"));
			TestCase.assertNull(json.opt("features"));
		} catch (IOException e) {	
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
}

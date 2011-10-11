package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simplegeo.client.test.TestEnvironment;

public class SimpleGeoContextClientTest {
	
	protected static SimpleGeoContextClient client;
	
	@BeforeClass
	public static void setUp() throws Exception {
		setupClient();
	}
	
	private static void setupClient() throws Exception {
		client = new SimpleGeoContextClient();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	@Test
	public void testGetContextSync() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			String jsonString = client.getContext(lat, lon, null);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("features"));
			Assert.assertNotNull(json.get("weather"));
			Assert.assertNotNull(json.get("demographics"));
		} catch (IOException e) {	
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetContextByMyIPSync() {
		try {
			String jsonString = client.getContextByIP("", null);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("query"));
			Assert.assertNotNull(json.get("features"));
			Assert.assertNotNull(json.get("weather"));
			Assert.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetContextByIPSync() {
		try {
			String jsonString = client.getContextByIP("92.156.43.27", null);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("query"));
			Assert.assertNotNull(json.get("features"));
			Assert.assertNotNull(json.get("weather"));
			Assert.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetContextByAddressSync() {
		try {
			String jsonString = client.getContextByAddress("41 Decatur St, San Francisco, CA", null);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("query"));
			Assert.assertNotNull(json.get("features"));
			Assert.assertNotNull(json.get("weather"));
			Assert.assertNotNull(json.get("demographics"));
		
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetFilteredContext() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			queryParams.put("filter", new String[] {"weather", "features"});
			queryParams.put("features__category", new String[] {"Neighborhood"});
			String jsonString = client.getContext(lat, lon, queryParams);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("weather"));
			Assert.assertNotNull(json.get("features"));
			
			Assert.assertNull(json.opt("demographics"));
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetDemographics() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			queryParams.put("filter", new String[] {"demographics.acs"});
			queryParams.put("demographics.acs__table", new String[] {"B08012"});
			String jsonString = client.getContext(lat, lon, queryParams);
			JSONObject json = new JSONObject(jsonString);
			
			Assert.assertNotNull(json.get("demographics"));
			
			Assert.assertNull(json.opt("weather"));
			Assert.assertNull(json.opt("features"));
		} catch (IOException e) {	
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
}

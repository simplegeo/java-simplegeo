package com.simplegeo.client;

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.GeometryCollection;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.LayerCollection;
import com.simplegeo.client.types.Record;

public class SimpleGeoStorageClientTest {

	protected static SimpleGeoStorageClient client;
	
	@BeforeClass
	public static void setUp() throws Exception {
		setupClient();
	}
	
	private static void setupClient() throws Exception {
		client = new SimpleGeoStorageClient();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	@Before
	public void mustHavePaidAccount() {
		assumeTrue(TestEnvironment.isPaidAccount());
	}

	@Test
	public void testAddOrUpdateRecordSync() {
		double lon = -122.937467;
		double lat = 47.046962;
		String layer = "casey.testing.layer";
		String testPropertyKey = "name";
		String testPropertyValue = "Testing Storage";
		try {
			Record record = new Record("simplegeo-boulder", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record.setProperties(properties);
			client.addOrUpdateRecord(record);
			
			Assert.assertEquals(layer, record.getLayer());
			Assert.assertEquals(lat, record.getGeometry().getPoint().getLat(), 0d);
			Assert.assertEquals(lon, record.getGeometry().getPoint().getLon(), 0d);
			Assert.assertEquals(testPropertyValue, record.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddOrUpdateRecordsSync() {
		double lon = -122.937467;
		double lat = 47.046962;
		String layer = "casey.testing.layer";
		String testPropertyKey = "name";
		String testPropertyValue = "Testing Storage";
		try {
			Record record1 = new Record("testRecordId1", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record1.setProperties(properties);
			
			Record record2 = new Record("testRecordId2", layer, "Feature", lon, lat);
			record2.setProperties(properties);
			
			ArrayList<Record> records = new ArrayList<Record>();
			records.add(record1);
			records.add(record2);
			
			client.addOrUpdateRecords(records, layer);
			
			Assert.assertEquals(layer, record1.getLayer());
			Assert.assertEquals(lat, record1.getGeometry().getPoint().getLat(), 0d);
			Assert.assertEquals(lon, record1.getGeometry().getPoint().getLon(), 0d);
			Assert.assertEquals(testPropertyValue, record1.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetRecordSync() {
		try {
			String jsonString = client.getRecord("casey.testing.layer", "simplegeo-boulder");
			Record record = Record.fromJSONString(jsonString);
			
			Assert.assertEquals("simplegeo-boulder", record.getRecordId());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}

	@Test
	public void testGetHistorySync() {
		try {
			String jsonString = client.getHistory("casey.testing.layer", "simplegeo-boulder", null);
			GeometryCollection geoColl = GeometryCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(geoColl.getGeometries());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}
	
	@Test
	public void testSearchSync() {
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("limit", new String[] {"10"});
			String jsonString = client.search(37.761809d, -122.422832d, "casey.testing.layer", params);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}
	
	@Test
	public void testSearchByAddressSync() {
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("limit", new String[] {"10"});
			String jsonString = client.searchByAddress("41 decatur st, san francisco, ca", "casey.testing.layer", params);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}
	
	@Test
	public void testSearchByIPSync() {
		try {
			String jsonString = client.searchByIP("173.164.219.53", "casey.testing.layer", null);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}

	@Test
	public void testSearchByMyIPSync() {
		try {
			String jsonString = client.searchByMyIP("casey.testing.layer", null);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			Assert.fail(e.getMessage());			
		} catch (JSONException e) {
			Assert.fail(e.getMessage());			
		} 
	}

	@Test
	public void testCreateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("java.client.testing.layer", "Testing Layer", "This layer is for testing only", false, urls);
			String jsonString = client.createLayer(layer);
			JSONObject json = new JSONObject(jsonString);

			Assert.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("java.client.testing.layer", "Testing Layer", "This layer is for testing only", false, urls);
			String jsonString = client.updateLayer(layer);
			JSONObject json = new JSONObject(jsonString);

			Assert.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetLayerSync() {
		try {			
			String jsonString = client.getLayer("casey.testing.layer");
			Layer layer = Layer.fromJSONString(jsonString);

			Assert.assertEquals("casey.testing.layer", layer.getName());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetLayersSync() {
		try {			
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			String jsonString = client.getLayers(queryParams);
			LayerCollection layers = LayerCollection.fromJSONString(jsonString);
			
			Assert.assertNotNull(layers.getLayers());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			Assert.fail(e.getMessage());
		}
	}
}
/**
 * Copyright (c) 2010-2011, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.simplegeo.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.GeometryCollection;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.LayerCollection;
import com.simplegeo.client.types.Record;

public class SimpleGeoStorageClientTest extends TestCase {

	protected SimpleGeoStorageClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = new SimpleGeoStorageClient();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
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
			
			TestCase.assertEquals(layer, record.getLayer());
			TestCase.assertEquals(lat, record.getGeometry().getPoint().getLat());
			TestCase.assertEquals(lon, record.getGeometry().getPoint().getLon());
			TestCase.assertEquals(testPropertyValue, record.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
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
			
			TestCase.assertEquals(layer, record1.getLayer());
			TestCase.assertEquals(lat, record1.getGeometry().getPoint().getLat());
			TestCase.assertEquals(lon, record1.getGeometry().getPoint().getLon());
			TestCase.assertEquals(testPropertyValue, record1.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testGetRecordSync() {
		try {
			String jsonString = client.getRecord("casey.testing.layer", "simplegeo-boulder");
			Record record = Record.fromJSONString(jsonString);
			
			TestCase.assertEquals("simplegeo-boulder", record.getRecordId());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testGetHistorySync() {
		try {
			String jsonString = client.getHistory("casey.testing.layer", "simplegeo-boulder", null);
			GeometryCollection geoColl = GeometryCollection.fromJSONString(jsonString);
			
			TestCase.assertNotNull(geoColl.getGeometries());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testDeleteRecordSync() {
		try {
			String jsonString = client.deleteRecord("casey.testing.layer", "simplegeo-boulder");
			JSONObject json = new JSONObject(jsonString);
			
			TestCase.assertEquals("Accepted", json.get("status"));
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testSearchSync() {
		try {
			HashMap<String, String[]> params = new HashMap<String, String[]>();
			params.put("limit", new String[] {"10"});
			String jsonString = client.search(37.761809d, -122.422832d, "casey.testing.layer", params);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			TestCase.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testSearchByIPSync() {
		try {
			String jsonString = client.searchByIP("173.164.219.53", "casey.testing.layer", null);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			TestCase.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testSearchByMyIPSync() {
		try {
			String jsonString = client.searchByMyIP("casey.testing.layer", null);
			FeatureCollection featureCollection = FeatureCollection.fromJSONString(jsonString);
			
			TestCase.assertNotNull(featureCollection.getFeatures());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testCreateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("java.client.testing.layer", "Testing Layer", "This layer is for testing only", false, urls);
			String jsonString = client.createLayer(layer);
			JSONObject json = new JSONObject(jsonString);

			TestCase.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testUpdateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("java.client.testing.layer", "Testing Layer", "This layer is for testing only", false, urls);
			String jsonString = client.updateLayer(layer);
			JSONObject json = new JSONObject(jsonString);

			TestCase.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testDeleteLayerSync() {
		try {			
			String jsonString = client.deleteLayer("java.client.testing.layer");
			JSONObject json = new JSONObject(jsonString);

			TestCase.assertEquals("Deleted", json.get("status"));

		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetLayerSync() {
		try {			
			String jsonString = client.getLayer("casey.testing.layer");
			Layer layer = Layer.fromJSONString(jsonString);

			TestCase.assertEquals("casey.testing.layer", layer.getName());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetLayersSync() {
		try {			
			HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
			String jsonString = client.getLayers(queryParams);
			LayerCollection layers = LayerCollection.fromJSONString(jsonString);
			
			TestCase.assertNotNull(layers.getLayers());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
}
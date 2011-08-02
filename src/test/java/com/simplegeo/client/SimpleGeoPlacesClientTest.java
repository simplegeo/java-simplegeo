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
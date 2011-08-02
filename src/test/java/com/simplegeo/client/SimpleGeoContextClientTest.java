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

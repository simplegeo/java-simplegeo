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
		client = SimpleGeoContextClient.getInstance(TestEnvironment.getBaseUrl(), 
						TestEnvironment.getPort(), TestEnvironment.getApiVersion());
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetContext() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			HashMap<String, Object> responseMap = (HashMap<String, Object>) client.getContext(lat, lon);
			
			this.assertNotNull(responseMap.get("features"));
			this.assertNotNull(responseMap.get("weather"));
			this.assertNotNull(responseMap.get("demographics"));
		} catch (IOException e) {	
			this.fail(e.getMessage());
		}
	}
	
	public void testGetContextByMyIP() {
		try {
			HashMap<String, Object> responseMap = (HashMap<String, Object>) client.getContextByIP("");
			
			this.assertNotNull(responseMap.get("query"));
			this.assertNotNull(responseMap.get("features"));
			this.assertNotNull(responseMap.get("weather"));
			this.assertNotNull(responseMap.get("demographics"));
			
			Object latitude = ((JSONObject)responseMap.get("query")).get("latitude");
			Object longitude = ((JSONObject)responseMap.get("query")).get("longitude");
			
			this.assertEquals(37.778381,latitude);
			this.assertEquals(-122.389388,longitude);
			
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetContextByIP() {
		try {
			HashMap<String, Object> responseMap = (HashMap<String, Object>) client.getContextByIP("92.156.43.27");
			
			this.assertNotNull(responseMap.get("query"));
			this.assertNotNull(responseMap.get("features"));
			this.assertNotNull(responseMap.get("weather"));
			this.assertNotNull(responseMap.get("demographics"));
			
			Object latitude = ((JSONObject)responseMap.get("query")).get("latitude");
			Object longitude = ((JSONObject)responseMap.get("query")).get("longitude");
			
			this.assertEquals(42.39020,latitude);
			this.assertEquals(-71.11470,longitude);
			
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetContextByAddress() {
		try {
			HashMap<String, Object> responseMap = (HashMap<String, Object>) client.getContextByAddress("41 Decatur St, San Francisco, CA");
			
			this.assertNotNull(responseMap.get("query"));
			this.assertNotNull(responseMap.get("features"));
			this.assertNotNull(responseMap.get("weather"));
			this.assertNotNull(responseMap.get("demographics"));
			
			Object latitude = ((JSONObject)responseMap.get("query")).get("latitude");
			Object longitude = ((JSONObject)responseMap.get("query")).get("longitude");
			
			this.assertEquals(40.01753,latitude);
			this.assertEquals(-105.27741,longitude);
			
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
}

/**
 * Copyright (c) 2009-2010, SimpleGeo
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
package com.simplegeo.client.model;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.ISimpleGeoClient;
import com.simplegeo.client.SimpleGeoClient;
import com.simplegeo.client.query.LatLonNearbyQuery;
import com.simplegeo.client.query.NearbyQuery;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.utilities.ModelHelper;


/**
 * @author Derek Smith
 *
 */
public class LayerTest extends TestCase {

	private Layer testingLayer;
	
	public void setUp() throws Exception {
		
		SimpleGeoClient.getInstance().getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
		testingLayer = new Layer(TestEnvironment.getLayer());
		
	}

	public void tearDown() {
		
		ISimpleGeoClient client = SimpleGeoClient.getInstance();
		if (client.supportsFutureTasks())
			client.setFutureTask(false);
		
		List<IRecord> records = testingLayer.getRecords();
		
		try {
			
			for(IRecord record : records)
				client.delete(record);
			
		} catch (ClientProtocolException e) {
			assertTrue(e.getMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);	
		}
	}

	public void testLayerRetrieval() throws Exception {
		
		double latitude = 37.0;
		double longitude = 27.0;
		NearbyQuery query = new LatLonNearbyQuery(latitude, longitude, 1.0, testingLayer.getName());
		for(int i = 0; i < 10; i++) {
			DefaultRecord record = ModelHelper.getRandomDefaultRecord();
			record.setLatitude(latitude);
			record.setLongitude(longitude);
			testingLayer.add(record);
		}
		
		try {			
			testingLayer.update();
			ModelHelper.waitForWrite();
			GeoJSONRecord geojson = (GeoJSONRecord)testingLayer.nearby(query);
			assertNotNull(geojson);
			assertTrue(geojson.getFeatures().length() >= 10);
			
		} catch (ClientProtocolException e) {
			assertTrue(e.getMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
		}
	}
}

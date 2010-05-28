/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.SimpleGeoClient;
import com.simplegeo.client.service.query.LatLonNearbyQuery;
import com.simplegeo.client.service.query.NearbyQuery;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.test.ModelHelperTest;


/**
 * @author Derek Smith
 *
 */
public class LayerTest extends ModelHelperTest {

	private Layer testingLayer;
	
	public void setUp() throws Exception {
		
		SimpleGeoClient.getInstance().getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
		testingLayer = new Layer(TestEnvironment.getLayer());
		
	}

	public void tearDown() {
		
		SimpleGeoClient locationService = SimpleGeoClient.getInstance();
		locationService.futureTask = false;
		
		List<IRecord> records = testingLayer.getRecords();
		
		try {
			
			for(IRecord record : records)
				locationService.delete(record);
			
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
			DefaultRecord record = getRandomDefaultRecord();
			record.setLatitude(latitude);
			record.setLongitude(longitude);
			testingLayer.add(record);
		}
		
		try {
			
			testingLayer.update();
			ModelHelperTest.waitForWrite();
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

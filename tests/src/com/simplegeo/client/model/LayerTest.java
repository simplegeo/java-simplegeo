/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.TestEnvironment;
import com.simplegeo.client.service.LocationService;
import com.simplegeo.client.test.ModelHelperTest;


/**
 * @author dsmith
 *
 */
public class LayerTest extends ModelHelperTest {

	private Layer testingLayer;
	
	public void setUp() {
		
		LocationService.getInstance().getHttpClient().setToken(TestEnvironment.ACCESS_KEY, TestEnvironment.SECRET_KEY);
		testingLayer = new Layer(TestEnvironment.TESTING_LAYER);
		
	}

	public void tearDown() {
		
		LocationService locationService = LocationService.getInstance();
		locationService.futureTask = false;
		
		List<IRecord> records = testingLayer.getRecords();
		
		try {
			
			for(IRecord record : records)
				locationService.delete(record);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testLayerRetrieval() {
		
		
		testingLayer.add(getRandomDefaultRecordList(50));
		try {
			
			testingLayer.update();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}

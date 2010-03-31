/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import junit.framework.*;
//import android.test.AndroidTestCase;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;

/**
 * @author Derek Smith
 *
 */
public class ModelHelperTest extends TestCase {
	
	private static final String TAG = ModelHelperTest.class.getName();
	
public void testTestsAreAlive() {
	    assertTrue("Tests exist!", true);
	}
	
	public DefaultRecord getRandomDefaultRecord() throws Exception {
		
		DefaultRecord record = new DefaultRecord(getRandomRecordId(), TestEnvironment.getLayer(), "object");
		record.setLatitude(getRandomLatitude());
		record.setLongitude(getRandomLongitude());
		
		return record;
	}
	
	public static void waitForWrite() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(TAG+"unable to sleep for 5 seconds");
		}
	}
	
	public List<IRecord> getRandomDefaultRecordList(int length) throws Exception {
		List<IRecord> list = new ArrayList<IRecord>(length);
		
		for(int i = 0; i < length; i++) 
			list.add(getRandomDefaultRecord());
		
		return list;
	}
	
	public GeoJSONRecord getRandomGeoJSONRecord() throws Exception {
		
		GeoJSONRecord record = new GeoJSONRecord(getRandomRecordId(), TestEnvironment.getLayer(), "object");
		record.setLatitude(getRandomLatitude());
		record.setLongitude(getRandomLongitude());
		
		return record;
	}
	
	public GeoJSONRecord getRandomGeoJSONRecordList(int length) throws Exception {
		
		GeoJSONRecord bigGeoJSONRecord = new GeoJSONRecord("FeatureCollection");
		
		JSONArray list = new JSONArray();
		for(int i = 0; i < length; i++) 
			list.put(getRandomGeoJSONRecord());
		
		try {
			bigGeoJSONRecord.putOpt("features", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bigGeoJSONRecord;
	}
	
	public boolean equals(IRecord recordOne, IRecord recordTwo) {

		boolean areEqual = true;
		areEqual &= roundDouble(recordOne.getLatitude()) == roundDouble(recordTwo.getLatitude());
		areEqual &= roundDouble(recordOne.getLongitude()) == roundDouble(recordTwo.getLongitude());
		areEqual &= recordOne.getLayer().equals(recordTwo.getLayer());
		areEqual &= recordOne.getObjectType().equals(recordTwo.getObjectType());
		areEqual &= recordOne.getCreated() == recordTwo.getCreated();
		
		return areEqual;
	}
	
	public boolean equals(List<IRecord> recordsOne, List<IRecord> recordsTwo) {
		
		int length = recordsOne.size();
		boolean areEqual = length == recordsTwo.size();
		for(int i = 0; i < length; i++)
			areEqual &= equals(recordsOne.get(i), recordsTwo.get(i));
		
		return areEqual;
	}
	
	public boolean equals(List<IRecord> records, GeoJSONObject geoJSONObject) {
		
		int recordSize = records.size();
		try {
			
			if(geoJSONObject.getType().equals("FeatureCollection")) {
			
				JSONArray features = geoJSONObject.getJSONArray("features");
				if(features != null) {
					
					int featuresSize = features.length();
					if(featuresSize == recordSize) {
						
						for(int index = 0; index < featuresSize; index++) {
							
							IRecord record = records.get(index);
							GeoJSONRecord jsonObject = (GeoJSONRecord) features.get(index);
							if(!equals(record, jsonObject))
								return false;
						}
						
						return true;
					}
					
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private String getRandomRecordId() {
		return String.format("testing-%d", (int)(Math.random() * 10000.0));
	}
	
	private double getRandomLatitude() {
		return Math.random() * 90.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}
	
	private double getRandomLongitude() {
		return Math.random() * 180.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}
	
	private double roundDouble(double d) {
		int c = 10;
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
	}
}

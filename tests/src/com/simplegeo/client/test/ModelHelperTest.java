/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.simplegeo.android.sdk.model.DefaultRecord;
import com.simplegeo.android.sdk.model.GeoJSONRecord;
import com.simplegeo.android.sdk.model.IRecord;

/**
 * @author dsmith
 *
 */
public class ModelHelperTest extends AndroidTestCase {
	
	public static final String TESTING_LAYER =  "com.simplegeo.testing";
	
	public static final String ACCESS_KEY = "my_key";
	public static final String SECRET_KEY = "my_secret";

	public DefaultRecord getRandomDefaultRecord() {
		DefaultRecord record = new DefaultRecord(getRandomRecordId(), TESTING_LAYER, "object");
		record.setLatitude(getRandomLatitude());
		record.setLongitude(getRandomLongitude());
		
		return record;
	}
	
	public List<IRecord> getRandomDefaultRecordList(int length) {
		ArrayList<DefaultRecord> list = new ArrayList<DefaultRecord>(length);
		
		for(int i = 0; i < length; i++) 
			list.add(getRandomDefaultRecord());
		
		return list;
	}
	
	public GeoJSONRecord getRandomGeoJSONRecord() {
		
		GeoJSONRecord record = new GeoJSONRecord(getRandomRecordId(), TESTING_LAYER, "object");
		record.setLatitude(getRandomLatitude());
		record.setLongitude(getRandomLongitude());
		
		return record;
	}
	
	public List<IRecord> getRandomGeoJSONRecordList(int length) {
		ArrayList<IRecord> list = new ArrayList<GeoJSONRecord>(length);

		for(int i = 0; i < length; i++) 
			list.add(getRandomGeoJSONRecord());
		
		return list;
	}
	
	public boolean equals(IRecord recordOne, IRecord recordTwo) {

		boolean areEqual = true;
		areEqual &= recordOne.getLatitude() == recordTwo.getLatitude();
		areEqual &= recordOne.getLongitude() == recordTwo.getLongitude();
		areEqual &= recordOne.getLayer().equals(recordTwo.getLayer());
		areEqual &= recordOne.getType().equals(recordTwo.getType());
		areEqual &= recordOne.getExpiration() == recordTwo.getExpiration();
		areEqual &= recordOne.getCreated() == recordTwo.getCreated();
		areEqual &= recordOne.getProperties().equals(recordTwo.getProperties());
		
		return areEqual;
	}
	
	public boolean equals(List<IRecord> recordsOne, List<IRecord> recordsTwo) {
		
		int length = recordsOne.size();
		boolean areEqual = length == recordsTwo.size();
		for(int i = 0; i < length; i++)
			areEqual &= equals(recordsOne.get(i), recordsTwo.get(i));
		
		return areEqual;
	}

	private String getRandomRecordId() {
		return String.format("testing-%i", (int)(Math.random() * 10000.0));
	}
	
	private double getRandomLatitude() {
		return Math.random() * 90.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}
	
	private double getRandomLongitude() {
		return Math.random() * 180.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}	
}

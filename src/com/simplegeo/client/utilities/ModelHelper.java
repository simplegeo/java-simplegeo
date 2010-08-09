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
package com.simplegeo.client.utilities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import junit.framework.*;

import com.simplegeo.client.geojson.GeoJSONObject;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;

/**
 * A simple class that can create random records. This is mainly used for testing
 * purposes.
 * 
 * @author Derek Smith
 */
public class ModelHelper extends TestCase {
	
	private static final String TAG = ModelHelper.class.getName();
		
	public static DefaultRecord getRandomDefaultRecord() {

		return getRandomDefaultRecord("random_layer");
	}

	public static DefaultRecord getRandomDefaultRecord(String layer) {
		
		DefaultRecord record = new DefaultRecord(getRandomRecordId(), layer, "object");
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
	
	public static List<IRecord> getRandomDefaultRecordList(int length) throws Exception {
		
		return getRandomDefaultRecordList("random_layer", length);
	}
	
	public static List<IRecord> getRandomDefaultRecordList(String layer, int length) throws Exception {
		
		List<IRecord> list = new ArrayList<IRecord>(length);
		for(int i = 0; i < length; i++) 
			list.add(ModelHelper.getRandomDefaultRecord(layer));
		
		return list;
	}
	
	public static GeoJSONRecord getRandomGeoJSONRecord() throws Exception {
		
		return getRandomGeoJSONRecord("random_layer");
	}
	
	public static GeoJSONRecord getRandomGeoJSONRecord(String layer) throws Exception {
		
		GeoJSONRecord record = new GeoJSONRecord(getRandomRecordId(), layer, "object");
		record.setLatitude(getRandomLatitude());
		record.setLongitude(getRandomLongitude());
		
		return record;
	}
	
	public static GeoJSONRecord getRandomGeoJSONRecordList(int length) throws Exception {
				
		return getRandomGeoJSONRecordList("random_layer", length);
	}
	
	public static GeoJSONRecord getRandomGeoJSONRecordList(String layer, int length) throws Exception {
		
		GeoJSONRecord bigGeoJSONRecord = new GeoJSONRecord("FeatureCollection");
		
		JSONArray list = new JSONArray();
		for(int i = 0; i < length; i++) 
			list.put(getRandomGeoJSONRecord(layer));
		
		try {
			bigGeoJSONRecord.putOpt("features", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bigGeoJSONRecord;
	}
		
	public static boolean equals(IRecord recordOne, IRecord recordTwo) {

		boolean areEqual = true;
		areEqual &= roundDouble(recordOne.getLatitude()) == roundDouble(recordTwo.getLatitude());
		areEqual &= roundDouble(recordOne.getLongitude()) == roundDouble(recordTwo.getLongitude());
		areEqual &= recordOne.getLayer().equals(recordTwo.getLayer());
		areEqual &= recordOne.getObjectType().equals(recordTwo.getObjectType());
		areEqual &= recordOne.getCreated() == recordTwo.getCreated();
		
		return areEqual;
	}
	
	public static boolean equals(List<IRecord> recordsOne, List<IRecord> recordsTwo) {
		
		int length = recordsOne.size();
		boolean areEqual = length == recordsTwo.size();
		for(int i = 0; i < length; i++)
			areEqual &= equals(recordsOne.get(i), recordsTwo.get(i));
		
		return areEqual;
	}
	
	public static boolean equals(List<IRecord> records, GeoJSONObject geoJSONObject) {
		
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

	public static String getRandomRecordId() {
		return String.format("testing-%d", (int)(Math.random() * 1000000.0));
	}
	
	public static double getRandomLatitude() {
		return Math.random() * 90.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}
	
	public static double getRandomLongitude() {
		return Math.random() * 180.0 * (((int)Math.random() * 2) == 0 ? (-1.0) : (1.0));
	}
	
	public static double roundDouble(double d) {
		int c = 10;
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
	}
}

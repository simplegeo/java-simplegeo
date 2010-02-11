/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.encoder;


import org.json.JSONException;

import android.test.suitebuilder.annotation.MediumTest;

import com.simplegeo.android.sdk.model.DefaultRecord;
import com.simplegeo.android.sdk.model.GeoJSONRecord;
import com.simplegeo.android.sdk.test.ModelHelperTest;

/**
 * @author dsmith
 *
 */
public class GeoJSONEncoderTest extends ModelHelperTest {

	@MediumTest
	public void testRecordToGeoJSON() {
		
		DefaultRecord record = getRandomDefaultRecord();
		
		try {
			record.getProperties().put("name", "derek");
		} catch (JSONException e) {
			;
		}
		
		GeoJSONRecord jsonRecord = (GeoJSONRecord)GeoJSONEncoder.getGeoJSONObject(record);
		assertNotNull(jsonRecord);
		assertTrue(equals(record, jsonRecord));
		
		jsonRecord = (GeoJSONRecord)GeoJSONEncoder.getGeoJSONObject(null);
		assertNull(jsonRecord);
	}
	
	@MediumTest
	public void testGeoJSONToRecord() {

		
		GeoJSONRecord jsonRecord = getRandomGeoJSONRecord();
		
		try {
			jsonRecord.getProperties().put("name", "derek");
		} catch (JSONException e) {
			;
		}
		
		DefaultRecord record = GeoJSONEncoder.getRecord(jsonRecord);
		assertNotNull(record);
		assertTrue(equals(record, jsonRecord));
		
		record = GeoJSONEncoder.getRecord(null);
		assertNull(record);
		
	}

}

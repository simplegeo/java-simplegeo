/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.encoder;


import java.util.List;

import org.json.JSONException;

import android.test.suitebuilder.annotation.MediumTest;

import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.test.ModelHelperTest;

/**
 * @author dsmith
 *
 */
public class GeoJSONEncoderTest extends ModelHelperTest {

	@MediumTest
	public void testRecordToGeoJSON() throws JSONException {
		
		DefaultRecord record = getRandomDefaultRecord();
		record.getProperties().put("name", "derek");
		
		GeoJSONRecord jsonRecord = GeoJSONEncoder.getGeoJSONRecord(record);
		assertNotNull(jsonRecord);
		assertTrue(equals(record, jsonRecord));
		
		jsonRecord = (GeoJSONRecord)GeoJSONEncoder.getGeoJSONRecord((DefaultRecord)null);
		assertNull(jsonRecord);
	}
	
	@MediumTest
	public void testGeoJSONToRecord() {

		GeoJSONRecord jsonRecord = getRandomGeoJSONRecord();
		jsonRecord.setObjectProperty("name", "derek");
				
		DefaultRecord record = GeoJSONEncoder.getRecord(jsonRecord);
		assertNotNull(record);
		assertTrue(equals(record, jsonRecord));
		
		record = GeoJSONEncoder.getRecord(null);
		assertNull(record);
		
	}
	
	@MediumTest
	public void testMultiGeoJSONToRecords() {
		
		GeoJSONRecord bigGeoJSONRecord = getRandomGeoJSONRecordList(10);
		
		List<DefaultRecord> defaultRecords = GeoJSONEncoder.getRecords(bigGeoJSONRecord);
		
		
	}
	
	@MediumTest
	public void testMultiRecordsToGeoJSON() {
		
		
	}

}

/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.encoder;


import java.util.List;

import org.json.JSONException;

import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.test.ModelHelperTest;

/**
 * @author dsmith
 *
 */
public class GeoJSONEncoderTest extends ModelHelperTest {

	public void testRecordToGeoJSON() throws JSONException {
		
		DefaultRecord record = getRandomDefaultRecord();
		record.getProperties().put("name", "derek");
		
		GeoJSONRecord jsonRecord = GeoJSONEncoder.getGeoJSONRecord(record);
		assertNotNull(String.format("GeoJSON record %s should not be null", jsonRecord.toString()), jsonRecord);
		assertTrue(String.format("Record %s does not equal %s", record, jsonRecord), equals(record, jsonRecord));

		jsonRecord = (GeoJSONRecord)GeoJSONEncoder.getGeoJSONRecord((DefaultRecord)null);
		assertNull(String.format("GeoJSON record %s should be null", jsonRecord), jsonRecord);
	}
	
	public void testGeoJSONToRecord() {

		GeoJSONRecord jsonRecord = getRandomGeoJSONRecord();
		jsonRecord.setObjectProperty("name", "derek");
				
		DefaultRecord record = GeoJSONEncoder.getRecord(jsonRecord);
		assertNotNull(record);
		assertTrue(equals(record, jsonRecord));
		
		record = GeoJSONEncoder.getRecord(null);
		assertNull(record);
		
	}
	
	public void testMultiGeoJSONToRecords() {
		
		GeoJSONRecord bigGeoJSONRecord = getRandomGeoJSONRecordList(10);
		
		List<DefaultRecord> defaultRecords = GeoJSONEncoder.getRecords(bigGeoJSONRecord);
		
		
	}
	
	public void testMultiRecordsToGeoJSON() {
		
		
	}

}

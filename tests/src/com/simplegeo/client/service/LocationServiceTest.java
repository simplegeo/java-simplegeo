/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.geohash.GeoHash;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.Envelope;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.service.LocationService;
import com.simplegeo.client.service.LocationService.Handler;
import com.simplegeo.client.service.query.GeohashNearbyQuery;
import com.simplegeo.client.service.query.HistoryQuery;
import com.simplegeo.client.service.query.LatLonNearbyQuery;
import com.simplegeo.client.test.ModelHelperTest;

public class LocationServiceTest extends ModelHelperTest {
	
	private DefaultRecord defaultRecord;
	private GeoJSONRecord feature;
	private List<IRecord> defaultRecordList;
	private GeoJSONObject featureCollection;
	
	public void setUp() throws Exception {
		
		LocationService.getInstance().getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
		
		defaultRecord = getRandomDefaultRecord();
		defaultRecord.setObjectProperty("name", "derek");
		
		feature = getRandomGeoJSONRecord();
		feature.setObjectProperty("mickey", "mouse");
		
		defaultRecordList = getRandomDefaultRecordList(10);
		featureCollection = getRandomGeoJSONRecordList(10);
		
	}
	
	public void tearDown() {
		
		LocationService locationService = LocationService.getInstance();
		locationService.futureTask = false;
		defaultRecordList.add(defaultRecord);
		
		List<IRecord> records = new ArrayList<IRecord>();
		records.addAll(defaultRecordList);
		records.addAll(GeoJSONEncoder.getRecords(featureCollection));
		records.add(GeoJSONEncoder.getRecord(feature));
		
		try {
			
			for(IRecord record : records)
				locationService.delete(record);
			
		} catch (Exception e) {
			;
		}
	}
	
	public void testRetrieveAndUpdateRecord() {
		
		LocationService locationService = LocationService.getInstance();
		
		try {
			
			// null means that the object was successful
			Object nothing = locationService.update(defaultRecord);
			assertNull("Should return a null value", nothing);
			ModelHelperTest.waitForWrite();
			
			nothing = locationService.update((GeoJSONObject)feature);
			assertNull("Should return a null value", nothing);
			ModelHelperTest.waitForWrite();
			
			IRecord retrievedRecord = (DefaultRecord)locationService.retrieve(defaultRecord);
			assertTrue(DefaultRecord.class.isInstance(retrievedRecord));
			assertTrue(equals(retrievedRecord, defaultRecord));
			
			retrievedRecord = (GeoJSONRecord)locationService.retrieve(feature);
			assertTrue("Should be an instance of GeoJSONRecord", GeoJSONRecord.class.isInstance(retrievedRecord));
			assertTrue("The two records should be equal", equals(retrievedRecord, feature));
			
			nothing = locationService.update(defaultRecordList);
			assertNull("Should return a null value", nothing);
			nothing = locationService.update(featureCollection);
			assertNull("Should return a null value", nothing);
						
		} catch (ClientProtocolException e) {
			assertTrue(e.getLocalizedMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getLocalizedMessage(), false);
		}
		
		defaultRecord.setRecordId("not-here-102939484");
		
		try {
			locationService.retrieve(defaultRecord);
			assertTrue(false);
		} catch (APIException e) {
			assertEquals(e.statusCode, SimpleGeoHandler.NO_SUCH);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	public void testNearby() throws Exception {
		
		LocationService locationService = LocationService.getInstance();
		
		DefaultRecord record = (DefaultRecord)defaultRecordList.get(0);
		record.setLatitude(10.0);
		record.setLongitude(10.0);
		locationService.update(record);
		
		record = (DefaultRecord)defaultRecordList.get(1);
		record.setLatitude(10.0);
		record.setLongitude(10.0);
		locationService.update(record);
		
		record = (DefaultRecord)defaultRecordList.get(2);
		record.setLatitude(10.0);
		record.setLongitude(10.0);
		locationService.update(record);
		
		try {
			
			locationService.update(defaultRecordList);
			ModelHelperTest.waitForWrite();
			
			locationService.update((IRecord)featureCollection);
			ModelHelperTest.waitForWrite();
			
		} catch (ClientProtocolException e) {
			assertTrue(e.getLocalizedMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getLocalizedMessage(), false);			
		}
		
		
		List<String> types = new ArrayList<String>();
		types.add("object");
		
		GeoHash geoHash = GeoHash.withBitPrecision(10.0, 10.0, 4);
		
		try {
			
			GeohashNearbyQuery geoHashQuery = new GeohashNearbyQuery(geoHash, TestEnvironment.getLayer(), types, 2, null);
			List<IRecord> nearbyRecords = (List<IRecord>)locationService.nearby(geoHashQuery, Handler.RECORD);
			assertNotNull(nearbyRecords);
			assertTrue(List.class.isInstance(nearbyRecords));
			assertTrue(nearbyRecords.size() == 2);
			
			LatLonNearbyQuery latLonQuery = new LatLonNearbyQuery(10.0, 10.0, 10.0, TestEnvironment.getLayer(), types, 2, null);
			GeoJSONObject nearbyJSONObjects = (GeoJSONObject)locationService.nearby(latLonQuery, Handler.GEOJSON);
			assertNotNull(nearbyJSONObjects);
			assertTrue(GeoJSONObject.class.isInstance(nearbyJSONObjects));
			int featureLength = nearbyJSONObjects.getFeatures().length();
			assertTrue(featureLength == 2);
			
			String cursor = (String)nearbyJSONObjects.get("next_cursor");
			assertNotNull(cursor);
			
			geoHashQuery.setCursor(cursor);
			geoHashQuery.setLimit(100);
			nearbyRecords = (List<IRecord>)locationService.nearby(geoHashQuery, Handler.RECORD);
			assertFalse(nearbyRecords.size() == featureLength);
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);			
		}
		
	}
	
	public void testDeleteRecord() {
		
		LocationService locationService = LocationService.getInstance();
		
		try {
			
			Object nothing = locationService.update(defaultRecord);
			assertNull("A null value should be returned", nothing);
			LocationServiceTest.waitForWrite();
				
			IRecord r = (IRecord)locationService.retrieve(defaultRecord);
			assertNotNull("The record should be retrievable", r);
			assertTrue("The records should be equal", equals(defaultRecord, r));
				
			locationService.delete(defaultRecord);
			
			nothing = locationService.update((GeoJSONObject)feature);
			assertNull("A null value should be returned", nothing);
			LocationServiceTest.waitForWrite();
			
			r = (IRecord)locationService.retrieve(feature);
			assertNotNull("The record should be retrievable", r);
			assertTrue("The records should be equal", equals(feature, r));
			
			locationService.delete(feature);
			

		} catch (ClientProtocolException e) {
			assertTrue(e.getMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
		}
		
	}
	
	
	
	public void testReverseGeocode() {

		LocationService locationService = LocationService.getInstance();
		
		try {
			
			GeoJSONObject jsonObject = (GeoJSONObject)locationService.reverseGeocode(40.01729499086, -105.2775999994);
			assertTrue(jsonObject.getProperties().length() > 8);
			assertTrue(jsonObject.getProperties().get("country").equals("US"));
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (JSONException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}
	} 
	
	public void testDensity() {

		LocationService locationService = LocationService.getInstance();
		
		try {
			Object o = locationService.density(Calendar.WEDNESDAY, 12, 40.01729499086, -105.2775999994);
			GeoJSONObject jsonObject = (GeoJSONObject)o;
			
			assertTrue(jsonObject.getGeometry().getJSONArray("coordinates").length()>3);
			
			assertTrue(jsonObject.getProperties().getInt("worldwide_rank") != -983);
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (JSONException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}
	}
	
	public void testContains() {
		
		LocationService locationService = LocationService.getInstance();
		
		try {
			double lat = 40.017294990861913;
			double lon = -105.27759999949176;
			
			JSONArray jsonArray = (JSONArray)locationService.contains(lat, lon);
			assertNotNull(jsonArray);
			assertTrue(jsonArray.length() == 9);
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}

	}
	
	public void testOverlaps() {
		
		LocationService locationService = LocationService.getInstance();
		
		try {
			
			Envelope envelope = new Envelope(40.0, -90.0, 50.0, -80.0);
			JSONArray jsonArray = (JSONArray)locationService.overlaps(envelope, 2, null);
			assertNotNull(jsonArray);
			assertTrue(jsonArray.length() == 2);
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}

	}
	
	public void testBoundary() {
		LocationService locationService = LocationService.getInstance();
		try {
			
			String featureId = "Province:Bauchi:s1zj73";
		    String name = "Bauchi";
		    
		    GeoJSONObject geoJSON = (GeoJSONObject)locationService.boundaries(featureId);
		    assertTrue(geoJSON.isFeature());
		    JSONObject properties = geoJSON.getProperties();
		    assertTrue(properties.getString("id").equals(featureId));
		    assertTrue(properties.getString("name").equals(name));
		    
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (JSONException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}

	}
	
	public void testHistory() throws Exception{
		LocationService locationService = LocationService.getInstance();

		int lat = 0;
		String recordId = defaultRecordList.get(0).getRecordId();

		try {
			
			for(IRecord record : defaultRecordList) {
				lat++;
				DefaultRecord defaultRecord = (DefaultRecord)record;
				defaultRecord.setRecordId(recordId);
				defaultRecord.setCreated(defaultRecord.getCreated()+lat*100);
				defaultRecord.setLatitude(lat);
				locationService.update(defaultRecord);
			}
			
			ModelHelperTest.waitForWrite();
			
		} catch (ClientProtocolException e) {
			assertTrue(e.getLocalizedMessage(), false);
		} catch (IOException e) {
			assertTrue(e.getLocalizedMessage(), false);			
		}
		
		try {
			
			HistoryQuery query = new HistoryQuery(recordId, TestEnvironment.getLayer(), 2);
			GeoJSONObject jsonObject = (GeoJSONObject)locationService.history(query);
			assertNotNull(jsonObject);
			assertTrue(GeoJSONObject.class.isInstance(jsonObject));
			assertTrue(jsonObject.isGeometryCollection());
			
			JSONArray geometries = jsonObject.getGeometries();
			int length = geometries.length();
			assertEquals(length, 2);

			String cursor = (String)jsonObject.get("next_cursor");
			assertTrue(cursor != null);
			query.setCursor(cursor);
			
			jsonObject = (GeoJSONObject)locationService.history(query);
			assertNotNull(jsonObject);
			geometries = jsonObject.getGeometries();
			assertEquals(length, 2);

		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);			
		}
		
	}
		
	public void testFutureRetrieval() {
		
		LocationService locationService = LocationService.getInstance();
		locationService.futureTask = true;
		
		try {
			
			FutureTask<Object> updateTaskOne = (FutureTask<Object>)locationService.update(defaultRecord);
			assertTrue(FutureTask.class.isInstance(updateTaskOne));
			FutureTask<Object> updateTaskTwo = (FutureTask<Object>)locationService.update((GeoJSONObject)feature);
			assertTrue(FutureTask.class.isInstance(updateTaskTwo));
			
			while(!updateTaskOne.isDone() && !updateTaskTwo.isDone());
			
			IRecord returnedRecord = (IRecord)updateTaskOne.get();
			assertNull(returnedRecord);
		
			LocationServiceTest.waitForWrite();
			
			FutureTask<Object> retrieveTaskOne = (FutureTask<Object>)locationService.retrieve(defaultRecord);
			assertTrue(FutureTask.class.isInstance(retrieveTaskOne));
			FutureTask<Object> retrieveTaskTwo = (FutureTask<Object>)locationService.retrieve(feature);
			assertTrue(FutureTask.class.isInstance(retrieveTaskTwo));
			
			while(!retrieveTaskOne.isDone() && !retrieveTaskTwo.isDone());
			
			returnedRecord = ((List<IRecord>)retrieveTaskOne.get()).get(0);
			assertNotNull(returnedRecord);
			assertTrue(DefaultRecord.class.isInstance(returnedRecord));
			assertTrue(equals(returnedRecord, defaultRecord));
			
			returnedRecord = (IRecord)retrieveTaskTwo.get();
			assertNotNull(returnedRecord);
			assertTrue(GeoJSONRecord.class.isInstance(returnedRecord));
			assertTrue(equals(returnedRecord, feature));
			
		} catch (ClientProtocolException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (IOException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (InterruptedException e) {
			assertFalse(e.getLocalizedMessage(), true);
		} catch (ExecutionException e) {
			assertFalse(e.getLocalizedMessage(), true);
		}
	}
}

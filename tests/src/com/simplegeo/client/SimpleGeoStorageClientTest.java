/**
 * Copyright (c) 2010-2011, SimpleGeo
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

package com.simplegeo.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import junit.framework.TestCase;

import org.json.JSONException;

import com.simplegeo.client.callbacks.FeatureCallback;
import com.simplegeo.client.callbacks.FeatureCollectionCallback;
import com.simplegeo.client.callbacks.GeometryCollectionCallback;
import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.CategoryCollection;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.GeometryCollection;
import com.simplegeo.client.types.Point;
import com.simplegeo.client.types.Record;

public class SimpleGeoStorageClientTest extends TestCase {

	protected SimpleGeoStorageClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoStorageClient.getInstance(TestEnvironment.getBaseUrl(), 
						TestEnvironment.getPort(), "0.1");
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testAddOrUpdateRecordSync() {
		double lon = -122.937467;
		double lat = 47.046962;
		String layer = "mojodna.test";
		String testPropertyKey = "name";
		String testPropertyValue = "Testing Storage";
		try {
			Record record = new Record("testRecordId", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record.setProperties(properties);
			client.addOrUpdateRecord(record);
			
			TestCase.assertEquals(layer, record.getLayer());
			TestCase.assertEquals(lat, record.getGeometry().getPoint().getLat());
			TestCase.assertEquals(lon, record.getGeometry().getPoint().getLon());
			TestCase.assertEquals(testPropertyValue, record.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testAddOrUpdateRecordAsync() {
		
		final CyclicBarrier barrier = new CyclicBarrier(2);
		final double lon = -122.937467;
		final double lat = 47.046962;
		final String layer = "mojodna.test";
		final String testPropertyKey = "name";
		final String testPropertyValue = "Testing Storage";
		try {
			final Record record = new Record("testRecordId", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record.setProperties(properties);
			client.addOrUpdateRecord(record, new SimpleGeoCallback<HashMap<String, Object>>() {
				
				@Override
				public void onSuccess(HashMap<String, Object> hasmap) {
					TestCase.assertEquals(layer, record.getLayer());
					TestCase.assertEquals(lat, record.getGeometry().getPoint().getLat());
					TestCase.assertEquals(lon, record.getGeometry().getPoint().getLon());
					TestCase.assertEquals(testPropertyValue, record.getProperties().get(testPropertyKey));
					barrierAwait(barrier);
				}
				
				@Override
				public void onError(String errorMessage) {
					TestCase.fail(errorMessage);
				}
			});
			barrierAwait(barrier);
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testAddOrUpdateRecordsSync() {
		double lon = -122.937467;
		double lat = 47.046962;
		String layer = "mojodna.test";
		String testPropertyKey = "name";
		String testPropertyValue = "Testing Storage";
		try {
			Record record1 = new Record("testRecordId1", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record1.setProperties(properties);
			
			Record record2 = new Record("testRecordId2", layer, "Feature", lon, lat);
			record2.setProperties(properties);
			
			ArrayList<Record> records = new ArrayList<Record>();
			records.add(record1);
			records.add(record2);
			
			client.addOrUpdateRecords(records, layer);
			
			TestCase.assertEquals(layer, record1.getLayer());
			TestCase.assertEquals(lat, record1.getGeometry().getPoint().getLat());
			TestCase.assertEquals(lon, record1.getGeometry().getPoint().getLon());
			TestCase.assertEquals(testPropertyValue, record1.getProperties().get(testPropertyKey));
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testAddOrUpdateRecordsAsync() {
		
		final CyclicBarrier barrier = new CyclicBarrier(2);
		final double lon = -122.937467;
		final double lat = 47.046962;
		final String layer = "mojodna.test";
		final String testPropertyKey = "name";
		final String testPropertyValue = "Testing Storage";
		try {
			final Record record1 = new Record("testRecordId1", layer, "Feature", lon, lat);
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put(testPropertyKey, testPropertyValue);
			record1.setProperties(properties);
			
			Record record2 = new Record("testRecordId2", layer, "Feature", lon, lat);
			record2.setProperties(properties);
			
			final ArrayList<Record> records = new ArrayList<Record>();
			records.add(record1);
			records.add(record2);
			
			client.addOrUpdateRecords(records, layer, new SimpleGeoCallback<HashMap<String, Object>>() {
				
				@Override
				public void onSuccess(HashMap<String, Object> hashmap) {
					TestCase.assertEquals(layer, record1.getLayer());
					TestCase.assertEquals(lat, record1.getGeometry().getPoint().getLat());
					TestCase.assertEquals(lon, record1.getGeometry().getPoint().getLon());
					TestCase.assertEquals(testPropertyValue, record1.getProperties().get(testPropertyKey));
					barrierAwait(barrier);
				}
				
				@Override
				public void onError(String errorMessage) {					
					TestCase.fail(errorMessage);
				}
			});
			barrierAwait(barrier);
			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testGetRecordSync() {
		try {
			Record record = client.getRecord("mojodna.test", "simplegeo-boulder");
			
			TestCase.assertNotNull(record);
			TestCase.assertEquals("mojodna.test", record.getLayer());
			TestCase.assertEquals("simplegeo-boulder", record.getRecordId());
			TestCase.assertEquals(-105.27728d, record.getGeometry().getPoint().getLon());
			TestCase.assertEquals(40.01685d, record.getGeometry().getPoint().getLat());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testGetRecordAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
				client.getRecord("mojodna.test", "simplegeo-boulder", new FeatureCallback() {
				
					@Override
					public void onSuccess(Feature feature) {
						Record record = (Record)feature;
						TestCase.assertNotNull(record);
						TestCase.assertEquals("mojodna.test", record.getLayer());
						TestCase.assertEquals("simplegeo-boulder", record.getRecordId());
						TestCase.assertEquals(-105.27728d, record.getGeometry().getPoint().getLon());
						TestCase.assertEquals(40.01685d, record.getGeometry().getPoint().getLat());
						barrierAwait(barrier);
					}
					
					@Override
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testDeleteRecordSync() {
		try {
			
			client.deleteRecord("mojodna.test", "simplegeo-boulder");
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testDeleteRecordAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
				client.deleteRecord("mojodna.test", "simplegeo-boulder", new SimpleGeoCallback<HashMap<String, Object>>() {
				
					@Override
					public void onSuccess(HashMap<String, Object> hashmap) {
						barrierAwait(barrier);
					}
					
					@Override
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testGetHistorySync() {
		try {
			
			GeometryCollection geometryCollection = client.getHistory("mojodna.test", "simplegeo-boulder", 0, null);
			TestCase.assertNotNull(geometryCollection);
			TestCase.assertNotNull(geometryCollection.getGeometries());
			TestCase.assertEquals(2, geometryCollection.getGeometries().size());
			TestCase.assertEquals(1213397406, geometryCollection.getGeometries().get(0).getCreated());
			TestCase.assertEquals(1213396201, geometryCollection.getGeometries().get(1).getCreated());
			TestCase.assertEquals(-122.422917d, geometryCollection.getGeometries().get(0).getPoint().getLon());
			TestCase.assertEquals(37.761835d, geometryCollection.getGeometries().get(0).getPoint().getLat());
			TestCase.assertEquals(-122.422832d, geometryCollection.getGeometries().get(1).getPoint().getLon());
			TestCase.assertEquals(37.761809d, geometryCollection.getGeometries().get(1).getPoint().getLat());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testGetHistoryAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.getHistory("mojodna.test", "simplegeo-boulder", 0, null, new GeometryCollectionCallback() {
				
					@Override
					public void onSuccess(GeometryCollection geometryCollection) {
						TestCase.assertNotNull(geometryCollection);
						TestCase.assertNotNull(geometryCollection.getGeometries());
						TestCase.assertEquals(2, geometryCollection.getGeometries().size());
						TestCase.assertEquals(1213397406, geometryCollection.getGeometries().get(0).getCreated());
						TestCase.assertEquals(1213396201, geometryCollection.getGeometries().get(1).getCreated());
						TestCase.assertEquals(-122.422917d, geometryCollection.getGeometries().get(0).getPoint().getLon());
						TestCase.assertEquals(37.761835d, geometryCollection.getGeometries().get(0).getPoint().getLat());
						TestCase.assertEquals(-122.422832d, geometryCollection.getGeometries().get(1).getPoint().getLon());
						TestCase.assertEquals(37.761809d, geometryCollection.getGeometries().get(1).getPoint().getLat());
						barrierAwait(barrier);
					}
					
					@Override
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testSearchSync() {
		try {
			FeatureCollection featureCollection = client.search(37.761809d, -122.422832d, "mojodna.test", 10, 0, null);
			TestCase.assertNotNull(featureCollection);
			TestCase.assertNotNull(featureCollection.getFeatures());
			TestCase.assertEquals(2, featureCollection.getFeatures().size());
			TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLon());
			TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLat());
			TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLon());
			TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLat());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testSearchAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.search(37.761809d, -122.422832d, "mojodna.test", 10, 0, null, new FeatureCollectionCallback() {
				
					@Override
					public void onSuccess(FeatureCollection featureCollection) {
						TestCase.assertNotNull(featureCollection);
						TestCase.assertNotNull(featureCollection.getFeatures());
						TestCase.assertEquals(2, featureCollection.getFeatures().size());
						TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLon());
						TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLat());
						TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLon());
						TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLat());
						barrierAwait(barrier);
					}
					
					@Override
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testSearchByIPSync() {
		try {
			FeatureCollection featureCollection = client.searchByIP("127.0.0.1", "mojodna.test", 0, null);
			TestCase.assertNotNull(featureCollection);
			TestCase.assertNotNull(featureCollection.getFeatures());
			TestCase.assertEquals(2, featureCollection.getFeatures().size());
			TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLon());
			TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLat());
			TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLon());
			TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLat());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testSearchByIPAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.searchByIP("127.0.0.1", "mojodna.test", 0, null, new FeatureCollectionCallback() {
				
					@Override
					public void onSuccess(FeatureCollection featureCollection) {
						TestCase.assertNotNull(featureCollection);
						TestCase.assertNotNull(featureCollection.getFeatures());
						TestCase.assertEquals(2, featureCollection.getFeatures().size());
						TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLon());
						TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(0).getGeometry().getPoint().getLat());
						TestCase.assertEquals(-105.27728d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLon());
						TestCase.assertEquals(40.01685d, featureCollection.getFeatures().get(1).getGeometry().getPoint().getLat());
						barrierAwait(barrier);
					}
					
					@Override
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	final private void barrierAwait(CyclicBarrier barrier) {
		try {
			barrier.await();
		} catch (InterruptedException e) {
			TestCase.fail(e.getMessage());
		} catch (BrokenBarrierException e) {
			TestCase.fail(e.getMessage());
		}
	}

}
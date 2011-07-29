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
import org.json.JSONObject;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.Record;

public class SimpleGeoStorageClientTest extends TestCase {

	protected SimpleGeoStorageClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoStorageClient.getInstance();
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
			client.addOrUpdateRecord(record, new SimpleGeoCallback() {
				
				public void onSuccess(JSONObject json) {
					TestCase.assertEquals(layer, record.getLayer());
					TestCase.assertEquals(lat, record.getGeometry().getPoint().getLat());
					TestCase.assertEquals(lon, record.getGeometry().getPoint().getLon());
					TestCase.assertEquals(testPropertyValue, record.getProperties().get(testPropertyKey));
					barrierAwait(barrier);
				}
				
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
			
			client.addOrUpdateRecords(records, layer, new SimpleGeoCallback() {
				
				public void onSuccess(JSONObject json) {
					TestCase.assertEquals(layer, record1.getLayer());
					TestCase.assertEquals(lat, record1.getGeometry().getPoint().getLat());
					TestCase.assertEquals(lon, record1.getGeometry().getPoint().getLon());
					TestCase.assertEquals(testPropertyValue, record1.getProperties().get(testPropertyKey));
					barrierAwait(barrier);
				}
				
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
			JSONObject json = client.getRecord("mojodna.test", "simplegeo-boulder");
			
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testGetRecordAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
				client.getRecord("mojodna.test", "simplegeo-boulder", new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}
					
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
			
			JSONObject json = client.deleteRecord("mojodna.test", "simplegeo-boulder");
			TestCase.assertEquals("deleted", json.get("status"));

		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testDeleteRecordAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
				client.deleteRecord("mojodna.test", "simplegeo-boulder", new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						try {
							TestCase.assertEquals("deleted", json.get("status"));
						} catch (JSONException e) {
							TestCase.fail(e.getMessage());
						}
						barrierAwait(barrier);
					}
					
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
			JSONObject json = client.getHistory("mojodna.test", "simplegeo-boulder", null);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testGetHistoryAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.getHistory("mojodna.test", "simplegeo-boulder", 0, null, new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}
					
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
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("limit", "10");
			JSONObject json = client.search(37.761809d, -122.422832d, "mojodna.test", params);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testSearchAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("limit", "10");
			client.search(37.761809d, -122.422832d, "mojodna.test", params, new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}
					
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
			JSONObject json = client.searchByIP("127.0.0.1", "mojodna.test", null);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testSearchByIPAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.searchByIP("127.0.0.1", "mojodna.test", null, new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}
					
					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}
	
	public void testCreateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("testLayer", "Testing Layer", "This layer is for testing only", false, urls);
			JSONObject json = client.createLayer(layer);

			TestCase.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testCreateLayerAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("testLayer", "Testing Layer", "This layer is for testing only", false, urls);
			client.createLayer(layer, new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						try {
							TestCase.assertEquals("OK", json.get("status"));
						} catch (JSONException e) {
							TestCase.fail(e.getMessage());
						}
						barrierAwait(barrier);
					}

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

	public void testUpdateLayerSync() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("testLayer", "Testing Layer", "This layer is for testing only", false, urls);
			JSONObject json = client.updateLayer(layer);

			TestCase.assertEquals("OK", json.get("status"));

		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testUpdateLayerAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://example.com/callback/simplegeo");

			Layer layer = new Layer("testLayer", "Testing Layer", "This layer is for testing only", false, urls);
			client.updateLayer(layer, new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						try {
							TestCase.assertEquals("OK", json.get("status"));
						} catch (JSONException e) {
							TestCase.fail(e.getMessage());
						}
						barrierAwait(barrier);
					}

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

	public void testDeleteLayerSync() {
		try {			
			JSONObject json = client.deleteLayer("testLayer");

			TestCase.assertEquals("Deleted", json.get("status"));

		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testDeleteLayerAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.deleteLayer("testLayer", new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						try {
							TestCase.assertEquals("Deleted", json.get("status"));
						} catch (JSONException e) {
							TestCase.fail(e.getMessage());
						}
						barrierAwait(barrier);
					}

					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testGetLayerSync() {
		try {			
			JSONObject json = client.getLayer("mojodna.test");

		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testGetLayerAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.getLayer("mojodna.test", new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}

					public void onError(String errorMessage) {
						TestCase.fail(errorMessage);			
					}
				});
				barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());			
		} 
	}

	public void testGetLayersSync() {
		try {			
			JSONObject json = client.getLayers();

		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}

	public void testGetLayersAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.getLayers(new SimpleGeoCallback() {
				
					public void onSuccess(JSONObject json) {
						barrierAwait(barrier);
					}

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
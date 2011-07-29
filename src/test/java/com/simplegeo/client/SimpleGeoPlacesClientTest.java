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
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Feature;

public class SimpleGeoPlacesClientTest extends TestCase {

	protected SimpleGeoPlacesClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}

	public void testGetCategoriesSync() {
		try {
			JSONObject json = client.getCategories();
			// TODO Actually do some testing here
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetCategoriesAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);

		try {
			client.getCategories(new SimpleGeoCallback() {
				
				public void onSuccess(JSONObject json) {
					// TODO do some testing
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
	
	public void testGetPlacePointSync() {
		try {
			JSONObject json = client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePointAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					// TODO Testing
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

	public void testGetPlacePointLocale() {
		try {
			JSONObject json = client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} finally {
			Locale.setDefault(Locale.US);
		}
	}

	
	public void testGetPlacePointFailureAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getPlace("SG_garbage", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					TestCase.fail("Succeeded for some reason");
				}
				public void onError(String errorMessage) {
					TestCase.assertEquals("Not Found", errorMessage);
					barrierAwait(barrier);
				}
			});
			barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePolygonSync() {
		try {
			JSONObject json = client.getPlace("SG_0Bw22I6fWoxnZ4GDc8YlXd");
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePolygonAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getPlace("SG_0Bw22I6fWoxnZ4GDc8YlXd", new SimpleGeoCallback() {
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
	
	public void testGetPlacePolygonFailureAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getPlace("SG_garbage", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					TestCase.fail("Succeeded for some reason");
				}
				public void onError(String errorMessage) {
					TestCase.assertEquals("Not Found", errorMessage);
					barrierAwait(barrier);
				}
			});
			barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testAddPlaceSync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			JSONObject json = client.addPlace(feature);
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testAddPlaceAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			client.addPlace(feature, new SimpleGeoCallback() {
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
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testAddPlaceFailureAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointBadString());
			client.addPlace(feature, new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					TestCase.fail("Succeeded for some reason.");
				}
				public void onError(String errorMessage) {
					TestCase.assertEquals("Internal Server Error", errorMessage);
					barrierAwait(barrier);
				}
			});
			barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testUpdatePlaceSync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			JSONObject json = client.updatePlace(feature);
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testUpdatePlaceAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			client.updatePlace(feature, new SimpleGeoCallback() {
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
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testUpdatePlaceFailureAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointBadString());
			client.updatePlace(feature, new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					TestCase.fail("Succeeded for some reason");
				}
				public void onError(String errorMessage) {
					TestCase.assertEquals("Internal Server Error", errorMessage);
					barrierAwait(barrier);
				}
			});
			barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testDeletePlaceSync() {
		try {
			JSONObject json = client.deletePlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testDeletePlaceAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.deletePlace("SG_4CsrE4oNy1gl8hCLdwu0F0", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					// TODO Testing
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
	
	public void testDeletePlaceFailureAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.deletePlace("SG_garbage", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					TestCase.fail("Succeeded for some reason");
				}
				public void onError(String errorMessage) {
					TestCase.assertEquals("Not Found", errorMessage);
					barrierAwait(barrier);
				}
			});
			barrierAwait(barrier);
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testSearchSync() {
		double lat = 37.759737;
		double lon = -122.433203;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("category", "Restaurants");
			params.put("radius", "25");
			JSONObject json = client.search(lat, lon, params);
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testSearchAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		final double lat = 37.759737;
		final double lon = -122.433203;
		
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("category", "Restaurants");
			params.put("radius", "25");
			client.search(lat, lon, params, new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					// TODO Testing
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
	
	public void testSearchByAddressSync() {
		String address = "1535 Pearl St, Boulder, CO";
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("radius", "25");
			JSONObject json = client.searchByAddress(address, params);
			// TODO Testing
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testSearchByAddressAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		final String address = "1535 Pearl St, Boulder, CO";
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("radius", "25");
			client.searchByAddress(address, params, new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					// TODO Testing
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
	
	final private boolean equalExceptTimestamp(String expected, String actual) {
		String actualMinusTimestamp = actual.substring(0, actual.lastIndexOf("@"));
		return expected.equals(actualMinusTimestamp);
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
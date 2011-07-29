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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.test.TestEnvironment;

public class SimpleGeoContextClientTest extends TestCase {
	
	protected SimpleGeoContextClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoContextClient.getInstance();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetContextSync() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			JSONObject json = client.getContext(lat, lon);
			
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
		} catch (IOException e) {	
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			client.getContext(lat, lon, new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					try {
						TestCase.assertNotNull(json.get("features"));
						TestCase.assertNotNull(json.get("weather"));
						TestCase.assertNotNull(json.get("demographics"));
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
	
	public void testGetContextByMyIPSync() {
		try {
			JSONObject json = client.getContextByIP("");
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByMyIPAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getContextByIP("", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					try {
						TestCase.assertNotNull(json.get("query"));
						TestCase.assertNotNull(json.get("features"));
						TestCase.assertNotNull(json.get("weather"));
						TestCase.assertNotNull(json.get("demographics"));
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
	
	public void testGetContextByIPSync() {
		try {
			JSONObject json = client.getContextByIP("92.156.43.27");
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
			
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByIPAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getContextByIP("92.156.43.27", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					try {
						TestCase.assertNotNull(json.get("query"));
						TestCase.assertNotNull(json.get("features"));
						TestCase.assertNotNull(json.get("weather"));
						TestCase.assertNotNull(json.get("demographics"));
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
	
	public void testGetContextByAddressSync() {
		try {
			JSONObject json = client.getContextByAddress("41 Decatur St, San Francisco, CA");
			
			TestCase.assertNotNull(json.get("query"));
			TestCase.assertNotNull(json.get("features"));
			TestCase.assertNotNull(json.get("weather"));
			TestCase.assertNotNull(json.get("demographics"));
		
		} catch (IOException e) {
			TestCase.fail(e.getMessage());
		} catch (JSONException e) {
			TestCase.fail(e.getMessage());
		}
	}
	
	public void testGetContextByAddressAsync() {
		final CyclicBarrier barrier = new CyclicBarrier(2);
		try {
			client.getContextByAddress("41 Decatur St, San Francisco, CA", new SimpleGeoCallback() {
				public void onSuccess(JSONObject json) {
					try {
						TestCase.assertNotNull(json.get("query"));
						TestCase.assertNotNull(json.get("features"));
						TestCase.assertNotNull(json.get("weather"));
						TestCase.assertNotNull(json.get("demographics"));
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

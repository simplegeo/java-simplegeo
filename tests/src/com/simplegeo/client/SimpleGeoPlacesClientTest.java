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

import junit.framework.TestCase;

import org.json.JSONException;

import com.simplegeo.client.callbacks.SimpleGeoFeatureCallback;
import com.simplegeo.client.callbacks.SimpleGeoFeatureCollectionCallback;
import com.simplegeo.client.callbacks.SimpleGeoMapCallback;
import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.Point;

public class SimpleGeoPlacesClientTest extends TestCase {

	protected SimpleGeoPlacesClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoPlacesClient.getInstance(TestEnvironment.getBaseUrl(), 
						TestEnvironment.getPort(), TestEnvironment.getApiVersion());
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetPlacePointSync() {
		double lon = -122.937467;
		double lat = 47.046962;
		try {
			Feature feature = client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			
			this.assertEquals("Feature", feature.getType());
			this.assertEquals("SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830", feature.getSimpleGeoId());
			this.assertNotNull(feature.getGeometry().getPoint());
			
			this.assertEquals(lat, feature.getGeometry().getPoint().getLat());
			this.assertEquals(lon, feature.getGeometry().getPoint().getLon());
			this.assertNull(feature.getGeometry().getPolygon());
			this.assertEquals(10, feature.getProperties().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePointAsync() {
		final double lon = -122.937467;
		final double lat = 47.046962;
		try {
			client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0", new SimpleGeoFeatureCallback() {
				public void onSuccess(Feature feature) {
					TestCase.assertEquals("Feature", feature.getType());
					TestCase.assertEquals("SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830", feature.getSimpleGeoId());
					TestCase.assertNotNull(feature.getGeometry().getPoint());
					
					TestCase.assertEquals(lat, feature.getGeometry().getPoint().getLat());
					TestCase.assertEquals(lon, feature.getGeometry().getPoint().getLon());
					TestCase.assertNull(feature.getGeometry().getPolygon());
					TestCase.assertEquals(10, feature.getProperties().size());
				}
				public void onError(String errorMessage) {
					// shouldn't be hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePolygonSync() {
		try {
			Feature feature = client.getPlace("SG_0Bw22I6fWoxnZ4GDc8YlXd");
			
			this.assertEquals("Feature", feature.getType());
			this.assertEquals("SG_0Bw22I6fWoxnZ4GDc8YlXd_37.759737_-122.433203", feature.getSimpleGeoId());
			this.assertNull(feature.getGeometry().getPoint());
			
			this.assertNotNull(feature.getGeometry().getPolygon());
			this.assertEquals(1, feature.getGeometry().getPolygon().getRings().size());
			this.assertEquals(60, feature.getGeometry().getPolygon().getRings().get(0).size());
			this.assertEquals(4, feature.getProperties().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePolygonAsync() {
		try {
			client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0", new SimpleGeoFeatureCallback() {
				public void onSuccess(Feature feature) {
					TestCase.assertEquals("Feature", feature.getType());
					TestCase.assertEquals("SG_0Bw22I6fWoxnZ4GDc8YlXd_37.759737_-122.433203", feature.getSimpleGeoId());
					TestCase.assertNull(feature.getGeometry().getPoint());
					
					TestCase.assertNotNull(feature.getGeometry().getPolygon());
					TestCase.assertEquals(1, feature.getGeometry().getPolygon().getRings().size());
					TestCase.assertEquals(60, feature.getGeometry().getPolygon().getRings().get(0).size());
					TestCase.assertEquals(4, feature.getProperties().size());
				}
				public void onError(String errorMessage) {
					// shouldn't be hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testAddPlaceSync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			HashMap<String, Object> responseMap = client.addPlace(feature);
			
			this.assertTrue(this.equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("id").toString()));
			this.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", responseMap.get("token").toString());
			this.assertTrue(this.equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("uri").toString()));
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testAddPlaceAsync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			client.addPlace(feature, new SimpleGeoMapCallback() {
				public void onSuccess(HashMap<String, Object> map) {
					TestCase.assertTrue(equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
							map.get("id").toString()));
					TestCase.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", map.get("token").toString());
					TestCase.assertTrue(equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
							map.get("uri").toString()));
				}
				public void onError(String errorMessage) {
					// shouldn't get hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	private final boolean equalExceptTimestamp(String expected, String actual) {
		String actualMinusTimestamp = actual.substring(0, actual.lastIndexOf("@"));
		return expected.equals(actualMinusTimestamp);
	}
	
	public void testUpdatePlaceSync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			HashMap<String, Object> responseMap = client.updatePlace(feature);
			
			this.assertTrue(this.equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("id").toString()));
			this.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", responseMap.get("token").toString());
			this.assertTrue(this.equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("uri").toString()));
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testUpdatePlaceAsync() {
		try {
			Feature feature = Feature.fromJSONString(TestEnvironment.getJsonPointString());
			client.updatePlace(feature, new SimpleGeoMapCallback() {
				public void onSuccess(HashMap<String, Object> map) {
					TestCase.assertTrue(equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
							map.get("id").toString()));
					TestCase.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", map.get("token").toString());
					TestCase.assertTrue(equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
							map.get("uri").toString()));
				}
				public void onError(String errorMessage) {
					// shouldn't get hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testDeletePlaceSync() {
		try {
			HashMap<String, Object> responseMap = client.deletePlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			
			this.assertEquals("8fa0d1c4fc2911dfa39058b035fcf1e5", responseMap.get("token").toString());
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testDeletePlaceAsync() {
		try {
			client.deletePlace("SG_4CsrE4oNy1gl8hCLdwu0F0", new SimpleGeoMapCallback() {
				public void onSuccess(HashMap<String, Object> map) {
					TestCase.assertEquals("8fa0d1c4fc2911dfa39058b035fcf1e5", map.get("token").toString());
				}
				public void onError(String errorMessage) {
					// shouldn't be hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testSearchSync() {
		double lat = 37.759737;
		double lon = -122.433203;
		try {
			FeatureCollection features = client.search(new Point(lat, lon), "", "Restaurants", 25);
			
			this.assertEquals(1, features.getFeatures().size());
			this.assertEquals(features.getFeatures().get(0).getType(), "Feature");
			this.assertEquals(features.getFeatures().get(0).getSimpleGeoId(), "SG_2RgyhpOhiTIVnpe3pN7y45_40.018959_-105.275107@1291798821");
			this.assertNotNull(features.getFeatures().get(0).getProperties());
			this.assertNotNull(features.getFeatures().get(0).getGeometry().getPoint());
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testSearchAsync() {
		final double lat = 37.759737;
		final double lon = -122.433203;
		
		try {
			client.search(new Point(lat, lon), "", "Restaurants", 25, new SimpleGeoFeatureCollectionCallback() {
				public void onSuccess(FeatureCollection features) {
					TestCase.assertEquals(1, features.getFeatures().size());
					TestCase.assertEquals(features.getFeatures().get(0).getType(), "Feature");
					TestCase.assertEquals(features.getFeatures().get(0).getSimpleGeoId(), "SG_2RgyhpOhiTIVnpe3pN7y45_40.018959_-105.275107@1291798821");
					TestCase.assertNotNull(features.getFeatures().get(0).getProperties());
					TestCase.assertNotNull(features.getFeatures().get(0).getGeometry().getPoint());
				}
				public void onError(String errorMessage) {
					// shouldn't get hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testSearchByAddressSync() {
		String address = "1535 Pearl St, Boulder, CO";
		try {
			FeatureCollection features = client.searchByAddress(address, "", "", 25);

			this.assertEquals(1, features.getFeatures().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testSearchByAddressAsync() {
		final String address = "1535 Pearl St, Boulder, CO";
		try {
			client.searchByAddress(address, "", "", 25, new SimpleGeoFeatureCollectionCallback() {
				public void onSuccess(FeatureCollection features) {
					TestCase.assertEquals(1, features.getFeatures().size());
				}
				public void onError(String errorMessage) {
					// shouldn't get hit
				}
			});
		} catch (IOException e) {
			this.fail(e.getMessage());
		}
	}

}
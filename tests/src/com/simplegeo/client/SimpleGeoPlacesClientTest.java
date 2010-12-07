package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import junit.framework.TestCase;

import org.json.JSONException;

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
	
	public void testGetPlacePoint() {
		double lat = -122.937467;
		double lon = 47.046962;
		try {
			FutureTask<Object> future = (FutureTask<Object>) client.getPlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			Feature feature = (Feature) future.get();
			this.assertEquals("Feature", feature.getType());
			this.assertEquals("SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830", feature.getSimpleGeoId());
			this.assertNotNull(feature.getGeometry().getPoint());
			
			this.assertEquals(lat, feature.getGeometry().getPoint().getLat());
			this.assertEquals(lon, feature.getGeometry().getPoint().getLon());
			this.assertNull(feature.getGeometry().getPolygon());
			this.assertEquals(10, feature.getProperties().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testGetPlacePolygon() {
		try {
			FutureTask<Object> future = (FutureTask<Object>) client.getPlace("SG_0Bw22I6fWoxnZ4GDc8YlXd");
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			Feature feature = (Feature) future.get();
			this.assertEquals("Feature", feature.getType());
			this.assertEquals("SG_0Bw22I6fWoxnZ4GDc8YlXd_37.759737_-122.433203", feature.getSimpleGeoId());
			this.assertNull(feature.getGeometry().getPoint());
			
			this.assertNotNull(feature.getGeometry().getPolygon());
			this.assertEquals(1, feature.getGeometry().getPolygon().getRings().size());
			this.assertEquals(60, feature.getGeometry().getPolygon().getRings().get(0).size());
			this.assertEquals(6, feature.getProperties().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testAddPlace() {
		try {
			Feature feature = Feature.fromJsonString(TestEnvironment.getJsonPointString());
			FutureTask<Object> future = (FutureTask<Object>) client.addPlace(feature);
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			HashMap<String, Object> responseMap = (HashMap<String, Object>) future.get();
			this.assertTrue(this.equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("id").toString()));
			this.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", responseMap.get("token").toString());
			this.assertTrue(this.equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("uri").toString()));
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
	
	private boolean equalExceptTimestamp(String expected, String actual) {
		String actualMinusTimestamp = actual.substring(0, actual.lastIndexOf("@"));
		return expected.equals(actualMinusTimestamp);
	}
	
	public void testUpdatePlace() {
		try {
			Feature feature = Feature.fromJsonString(TestEnvironment.getJsonPointString());
			FutureTask<Object> future = (FutureTask<Object>) client.updatePlace(feature);
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			HashMap<String, Object> responseMap = (HashMap<String, Object>) future.get();
			this.assertTrue(this.equalExceptTimestamp("SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("id").toString()));
			this.assertEquals("596499b4fc2a11dfa39058b035fcf1e5", responseMap.get("token").toString());
			this.assertTrue(this.equalExceptTimestamp("/1.0/features/SG_2cf49b19bfbbe6b737e43699b106fb4e2ade9b51_47.046962_-122.937467", 
					responseMap.get("uri").toString()));
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (JSONException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testDeletePlace() {
		try {
			FutureTask<Object> future = (FutureTask<Object>) client.deletePlace("SG_4CsrE4oNy1gl8hCLdwu0F0");
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			HashMap<String, Object> responseMap = (HashMap<String, Object>) future.get();
			this.assertEquals("8fa0d1c4fc2911dfa39058b035fcf1e5", responseMap.get("token").toString());
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
	
	public void testSearch() {
		double lat = 37.759737;
		double lon = -122.433203;
		try {
			FutureTask<Object> future = (FutureTask<Object>)  client.search(new Point(lat, lon), "", "Restaurants");
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			FeatureCollection features = (FeatureCollection) future.get();
			System.out.println(features.getFeatures().get(0).getSimpleGeoId());
			this.assertEquals(7, features.getFeatures().size());
		} catch (IOException e) {
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}

}
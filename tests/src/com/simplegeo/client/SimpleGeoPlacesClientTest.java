package com.simplegeo.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import junit.framework.TestCase;

import org.json.JSONException;

import com.simplegeo.client.test.TestEnvironment;
import com.simplegeo.client.types.Feature;
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
			Feature feature = Feature.fromJsonString(TestEnvironment.getJsonString());
			FutureTask<Object> future = (FutureTask<Object>) client.addPlace(feature);
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			System.out.println(future.get());
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
	
	public void testUpdatePlace() {
		try {
			Object response =  client.updatePlace(new Feature());
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		} catch (JSONException je) {
			this.fail(je.getMessage());
		}
	}
	
	public void testDeletePlace() {
		try {
			Object response = client.deletePlace("");
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		}
	}
	
	public void testSearch() {
		double lat = -122.937467;
		double lon = 47.046962;
		try {
			Object response =  client.search(new Point(lat, lon), "", "");
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		}
	}

}
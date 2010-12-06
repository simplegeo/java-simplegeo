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
	protected double lat = -122.937467;
	protected double lon = 47.046962;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoPlacesClient.getInstance(TestEnvironment.getBaseUrl(), 
						TestEnvironment.getPort(), TestEnvironment.getApiVersion());
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetPlace() {
		try {
			// how exactly do future tasks work?
			FutureTask<Object> future = (FutureTask<Object>) client.getPlace("");
			Feature feature = (Feature) future.get();
			this.failNotEquals("Types - Feature != " + feature.getType(), "Feature", feature.getType());
			this.failNotEquals("Ids - SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830 != " + feature.getSimpleGeoId(), 
					"SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830", feature.getSimpleGeoId());
			if (feature.getGeometry().getPoint() == null) {
				this.fail("Point - Point(-122.937467, 47.046962) != null");
			} else {
				this.failNotEquals("Lat - -122.937467 != " + feature.getGeometry().getPoint().getLat(), 
						lat, feature.getGeometry().getPoint().getLat());
				this.failNotEquals("Lat - 47.046962 != " + feature.getGeometry().getPoint().getLon(), 
						lon, feature.getGeometry().getPoint().getLon());
			}
			if (feature.getGeometry().getPolygon()!= null) {
				this.fail("Polygon - Should be null");
			}
			if (feature.getProperties() == null) {
				this.fail("Properties - Shouldn't be null");
			} else {
				this.failNotEquals("Properties Size - 7 != " + feature.getProperties().size(), 
						7, feature.getProperties().size());
			}
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		} catch (ExecutionException ee) {
			this.fail(ee.getMessage());
		} catch (InterruptedException ine) {
			this.fail(ine.getMessage());
		}
	}
	
	public void testAddPlace() {
		try {
			// how exactly do future tasks work?
			FutureTask<Object> future = (FutureTask<Object>) client.addPlace(new Feature());
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		} catch (JSONException je) {
			this.fail(je.getMessage());
		}
	}
	
	public void testUpdatePlace() {
		try {
			// how exactly do future tasks work?
			FutureTask<Object> future = (FutureTask<Object>) client.updatePlace(new Feature());
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		} catch (JSONException je) {
			this.fail(je.getMessage());
		}
	}
	
	public void testDeletePlace() {
		try {
			// how exactly do future tasks work?
			FutureTask<Object> future = (FutureTask<Object>) client.deletePlace("");
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		}
	}
	
	public void testSearch() {		
		try {
			FutureTask<Object> future = (FutureTask<Object>) client.search(new Point(lat, lon), "", "");
		} catch (IOException ie) {
			this.fail(ie.getMessage());
		}
	}

}
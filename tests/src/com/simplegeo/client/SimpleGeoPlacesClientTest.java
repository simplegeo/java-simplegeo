package com.simplegeo.client;

import java.io.IOException;
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
	
	public void testGetPlace() {
		try {
			// how exactly do future tasks work?
			FutureTask future = (FutureTask) client.getPlace("");
		} catch (IOException e) {
			e.printStackTrace();
			this.fail();
		}
	}
	
	public void testAddPlace() {
		try {
			// how exactly do future tasks work?
			FutureTask future = (FutureTask) client.addPlace(new Feature());
		} catch (IOException ie) {
			ie.printStackTrace();
			this.fail();
		} catch (JSONException je) {
			je.printStackTrace();
			this.fail();
		}
	}
	
	public void testUpdatePlace() {
		try {
			// how exactly do future tasks work?
			FutureTask future = (FutureTask) client.updatePlace(new Feature());
		} catch (IOException ie) {
			ie.printStackTrace();
			this.fail();
		} catch (JSONException je) {
			je.printStackTrace();
			this.fail();
		}
	}
	
	public void testDeletePlace() {
		try {
			// how exactly do future tasks work?
			FutureTask future = (FutureTask) client.deletePlace("");
		} catch (IOException ie) {
			ie.printStackTrace();
			this.fail();
		}
	}
	
	public void testSearch() {		
		try {
			FutureTask future = (FutureTask) client.search(new Point(), "", "");
		} catch (IOException ie) {
			ie.printStackTrace();
			this.fail();
		}
	}

}
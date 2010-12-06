package com.simplegeo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import junit.framework.TestCase;

import com.simplegeo.client.test.TestEnvironment;

public class SimpleGeoContextClientTest extends TestCase {
	
	protected SimpleGeoContextClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoContextClient.getInstance(TestEnvironment.getBaseUrl(), 
						TestEnvironment.getPort(), TestEnvironment.getApiVersion());
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
	
	public void testGetContext() {
		double lat = 37.803259;
		double lon = -122.440033;
		try {
			FutureTask<Object> future = (FutureTask<Object>) client.getContext(lat, lon);
			while (!future.isDone()) {
				Thread.sleep(500);
			}
			HashMap<String, Object> responseMap = (HashMap<String, Object>) future.get();
			System.out.println(responseMap);
			this.assertNotNull(responseMap.get("features"));
			this.assertNotNull(responseMap.get("weather"));
			this.assertNotNull(responseMap.get("demographics"));
		} catch (IOException e) {	
			this.fail(e.getMessage());
		} catch (InterruptedException e) {
			this.fail(e.getMessage());
		} catch (ExecutionException e) {
			this.fail(e.getMessage());
		}
	}
}

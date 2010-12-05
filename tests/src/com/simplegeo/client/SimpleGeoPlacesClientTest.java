package com.simplegeo.client;

import com.simplegeo.client.test.TestEnvironment;

import junit.framework.TestCase;

public class SimpleGeoPlacesClientTest extends TestCase {

	protected ISimpleGeoClient client;
	
	public void setUp() throws Exception {
		this.setupClient();
	}
	
	private void setupClient() throws Exception {
		client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(TestEnvironment.getKey(), TestEnvironment.getSecret());
	}
}

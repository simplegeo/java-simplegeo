package com.simplegeo.client;

import java.io.IOException;

import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.http.IOAuthClient;

public class SimpleGeoContextClient extends AbstractSimpleGeoClient {

	@Override
	public IOAuthClient getHttpClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object executeGet(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object executePost(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected Object executePut(String uri, String jsonPayload,
			ISimpleGeoJSONHandler handler) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object executeDelete(String uri, ISimpleGeoJSONHandler handler)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}

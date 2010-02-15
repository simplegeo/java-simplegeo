/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;

/**
 * A handler used to create {@link com.simplegeo.client.model.DefaultRecord}s
 * from a given payload.
 * 
 * @author Derek Smith
 */
public class RecordHandler extends GeoJSONHandler {
	
	private static final String TAG = RecordHandler.class.getName();

	/* (non-Javadoc)
	 * @see com.simplegeo.client.http.GeoJSONHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		GeoJSONObject geoJSONObject = (GeoJSONObject)super.handleResponse(response);
		
		List<DefaultRecord> defaultRecords = null;
		
		if(geoJSONObject != null)
			defaultRecords = GeoJSONEncoder.getRecords(geoJSONObject);
		
		return defaultRecords;
	}

}

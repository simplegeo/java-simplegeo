/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;



import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;


/**
 * @author dsmith
 *
 */
public class RecordHandler extends GeoJSONHandler {
	
	private static final String TAG = RecordHandler.class.getName();

	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		GeoJSONObject geoJSONObject = (GeoJSONObject)super.handleResponse(response);
		
		List<DefaultRecord> defaultRecords = null;
		
		if(geoJSONObject != null)
			defaultRecords = GeoJSONEncoder.getRecords(geoJSONObject);
		
		return defaultRecords;
	}

}

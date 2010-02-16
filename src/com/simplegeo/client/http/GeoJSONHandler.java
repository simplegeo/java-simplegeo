/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;

import java.io.EOFException;
import java.io.IOException;

//import android.util.Log;
import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;

/**
 * A response handler used for creating {@link com.simplegeo.client.model.GeoJSONObject}s
 * from a given payload.
 * 
 * @author Derek Smith
 */
public class GeoJSONHandler extends SimpleGeoHandler {
	
	private static final String TAG = GeoJSONHandler.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONHandler.class);

	/* (non-Javadoc)
	 * @see com.simplegeo.client.http.SimpleGeoHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public Object handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		
		response = (HttpResponse)super.handleResponse(response);
				
		GeoJSONObject topObject = null;
        try {
        	
        	// We need to do a little massaging of the data here
        	String jsonString = EntityUtils.toString(response.getEntity());
        	if(jsonString != null && jsonString.length() > 1) {
        		
        		topObject = new GeoJSONRecord("Feature", jsonString);
        		
        		String geoJSONType = topObject.getType();
        		if(geoJSONType.equals("Feature")) {
        			
        			topObject.put("layer", getLayerForGeoJSONRecord(topObject));
        			
        		} else if(geoJSONType.equals("FeatureCollection")) {
        			
        			JSONArray features = topObject.getFeatures();
        			int length = features.length();
        			for(int index = 0; index < length; index++) {
        				
        				JSONObject record = (JSONObject)features.get(index);
        				record.put("layer", getLayerForGeoJSONRecord(record));
        				
        			}
        				
        			
        		}
        	}
        	
        } catch (EOFException e) {
        	
        	// Do nothing becuase the entity was empty
        	logger.debug(String.format("response was a success, but no content was returned (%s)", e.toString()));
        	
		} catch (JSONException e) {
			
			logger.debug(e.getMessage());
		}
        
		return topObject;
	}

	private String getLayerForGeoJSONRecord(JSONObject record) {
		
		String layer = "";
		JSONObject layerDict = (JSONObject)record.opt("layerLink");
		if(layerDict != null) {
			
			
			// THIS IS SOOOO BAD
			String hRef = (String)layerDict.opt("href");
			String[] substrings = hRef.split("/");
			int length = substrings.length;
			if(substrings.length > 0) {
				layer = substrings[length - 1];
				layer = layer.replaceAll(".json", "");
			}
			
		}
		
		return layer;
		
	}
}

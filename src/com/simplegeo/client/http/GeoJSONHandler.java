/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//import android.util.Log;
import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;



/**
 * @author Derek Smith
 *
 */
public class GeoJSONHandler extends SimpleGeoHandler {
	
	private static final String TAG = GeoJSONHandler.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONHandler.class);

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

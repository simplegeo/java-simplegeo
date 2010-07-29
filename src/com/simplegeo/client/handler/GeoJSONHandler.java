/**
 * Copyright (c) 2009-2010, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simplegeo.client.handler;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.geojson.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;

/**
 * A response handler used for creating {@link com.simplegeo.client.geojson.GeoJSONObject}s
 * from a given payload.
 * 
 * @author Derek Smith
 */
public class GeoJSONHandler implements SimpleGeoJSONHandlerIfc {
	
	private static Logger logger = Logger.getLogger(GeoJSONHandler.class.getName());

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

	
	public Object parseResponse(String response) {
		GeoJSONObject topObject = null;
        try {
        	
        	// We need to do a little massaging of the data here
        	String jsonString = response;
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
        	
        } catch (JSONException e) {
			
			logger.info(e.getMessage());
		}
        
		return topObject;
	}
}

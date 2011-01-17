/**
 * Copyright (c) 2010-2011, SimpleGeo
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

import org.json.JSONException;

import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

/**
 * A response handler used for creating {@link com.simplegeo.client.geojson.GeoJSONObject}s
 * from a given payload.
 * 
 * @author Derek Smith
 */
public class GeoJSONHandler implements SimpleGeoJSONHandler {
	
	private static Logger logger = Logger.getLogger(GeoJSONHandler.class.getName());
	
	public Object parseResponse(String response) {
		Object returnObject = new Object();
		if (response.contains("FeatureCollection")) {
			try {
				returnObject = FeatureCollection.fromJSONString(response);
			} catch (JSONException e){
				logger.info(e.getMessage());
			}
		} else {
			try {
				returnObject = Feature.fromJSONString(response);
			} catch (JSONException e){
				logger.info(e.getMessage());
			}
		}
		return returnObject;
	}
}

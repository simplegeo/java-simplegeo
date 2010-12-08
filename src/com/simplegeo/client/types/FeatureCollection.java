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

package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeatureCollection {

	private ArrayList<Feature> features;
	
	public FeatureCollection() {
		
	}
	
	public FeatureCollection(ArrayList<Feature> features) {
		this.features = features;
	}

	public ArrayList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}
	
	public static FeatureCollection fromJson(JSONObject json) throws JSONException {
		FeatureCollection featureCollection = new FeatureCollection();
		int numOfFeatures = json.getInt("total");
		ArrayList<Feature> features = new ArrayList<Feature>();
		JSONArray featuresArray = json.getJSONArray("features");
		for (int i=0; i<numOfFeatures; i++) {
			features.add(Feature.fromJson(featuresArray.getJSONObject(i)));
		}
		featureCollection.setFeatures(features);
		return featureCollection;
	}
	
	public static FeatureCollection fromJsonString(String jsonString) throws JSONException {
		return fromJson(new JSONObject(jsonString));
	}
	
	public JSONArray toJson() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Feature> features = this.getFeatures();
		for (Feature feature : features) {
			jsonArray.put(feature.toJson());
		}
		return jsonArray;
	}
	
	public String toJsonString() throws JSONException {
		return this.toJson().toString();
	}
	
}
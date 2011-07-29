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

/**
 * Class that a represents a Layer
 * 
 * @author Pranil Kanderi
 *
 */
public class Layer {
	
	private String name;
	private String title;
	private String description;
	private boolean isPublic;
	private long created;
	private long updated;
	private ArrayList<String> callbackURLs;
	
	/**
	 * Initializes a new Layer object with the name
	 * 
	 * @param name the name of the layer (e.g. com.mokriya.testing)
	 */
	public Layer(String name) {
		this(name, "", "", false, new ArrayList<String>());
	}
	
	/**
	 * Initializes a new Layer object with the specified
	 * 
	 * @param name the name of the layer (e.g. com.mokriya.testing)
	 * @param title the title of this layer (eg. Test Layer for Mokriya)
	 * @param description the description of this layer
	 * @param isPublic boolean, true if this layer is public, false otherwise
	 * @param callbackURLs ArrayList of callback url's
	 */
	public Layer(String name, String title, String description, boolean isPublic, ArrayList<String> callbackURLs) {
		this.name = name;
		this.title = title;
		this.description = description;
		this.isPublic = isPublic;
		this.callbackURLs = callbackURLs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public ArrayList<String> getCallbackURLs() {
		return callbackURLs;
	}

	public void setCallbackURLs(ArrayList<String> callbackURLs) {
		this.callbackURLs = callbackURLs;
	}	

	public long getCreated() {
		return created;
	}

	public long getUpdated() {
		return updated;
	}

	public static String toJSONString(Layer layer) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", layer.getName());
		jsonObject.put("title", layer.getTitle());
		jsonObject.put("description", layer.getDescription());
		jsonObject.put("public", layer.isPublic());
		
		JSONArray jsonArray = new JSONArray();
		for (String url : layer.callbackURLs) {
			jsonArray.put(url);
		}
		jsonObject.put("callback_urls", jsonArray);
		
		return jsonObject.toString();
	}

	public static Layer fromJSONString(String jsonString) throws JSONException {
		return fromJSONObject(new JSONObject(jsonString));
	}
	
	public static Layer fromJSONObject(JSONObject json) throws JSONException {
		Layer layer = new Layer(json.getString("name"));
		layer.setTitle(json.optString("title"));
		layer.setDescription(json.optString("description"));
		layer.setPublic(json.optBoolean("public"));
		layer.created = json.optLong("created");
		layer.updated = json.optLong("updated");
		
		JSONArray urls = json.optJSONArray("callback_urls");
		ArrayList<String> callbackURLs = new ArrayList<String>();
		if (urls != null) {
			for (int i=0; i < urls.length(); i++) {
				callbackURLs.add(urls.getString(i));
			}
		}
		
		layer.setCallbackURLs(callbackURLs);
		
		return layer;
	}
	
}

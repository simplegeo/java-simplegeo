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

package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Collection of {@link com.simplegeo.client.types.Layer} objects
 * 
 * @author Pranil Kanderi
 *
 */
public class LayerCollection {

	private ArrayList<Layer> layers;
	
	public LayerCollection() {
	}
	
	public LayerCollection(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

	public static LayerCollection fromJSONString(String response) throws JSONException {
		JSONObject jsonObject = new JSONObject(response);
		JSONArray jsonArray = jsonObject.getJSONArray("layers");
		
		LayerCollection layerCollection = new LayerCollection();
		ArrayList<Layer> layers = new ArrayList<Layer>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Layer layer = Layer.fromJSONObject(json);
			layers.add(layer);
		}
		layerCollection.setLayers(layers);
		
		return layerCollection;
	}
	
	
}

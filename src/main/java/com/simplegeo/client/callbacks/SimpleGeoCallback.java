package com.simplegeo.client.callbacks;

import org.json.JSONObject;

public interface SimpleGeoCallback {

	public void onSuccess(JSONObject json);
	
	public void onError(String errorMessage);
	
}

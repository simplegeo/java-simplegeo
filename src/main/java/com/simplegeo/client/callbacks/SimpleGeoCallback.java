package com.simplegeo.client.callbacks;

public interface SimpleGeoCallback {

	public void onSuccess(String jsonString);
	
	public void onError(String errorMessage);
	
}

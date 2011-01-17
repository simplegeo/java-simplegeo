package com.simplegeo.client.callbacks;

import java.util.HashMap;

public abstract class SimpleGeoMapCallback implements SimpleGeoCallback<HashMap<String, Object>> {
	
	public abstract void onSuccess(HashMap<String, Object> map);
	
	public abstract void onError(String errorMessage);

}

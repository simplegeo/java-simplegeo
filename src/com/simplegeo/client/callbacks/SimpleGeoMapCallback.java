package com.simplegeo.client.callbacks;

import java.util.HashMap;

public abstract class SimpleGeoMapCallback implements ISimpleGeoCallback<HashMap<String, Object>> {
	
	public abstract void onSuccess(HashMap<String, Object> map);
	
	public abstract void onError(String errorMessage);

}

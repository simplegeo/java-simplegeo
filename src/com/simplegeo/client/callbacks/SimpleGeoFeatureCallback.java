package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.Feature;

public abstract class SimpleGeoFeatureCallback implements SimpleGeoCallback<Feature> {
	
	public abstract void onSuccess(Feature feature);
	
	public abstract void onError(String errorMessage);

}

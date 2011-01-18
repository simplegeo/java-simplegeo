package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.FeatureCollection;

public abstract class FeatureCollectionCallback 
	implements SimpleGeoCallback<FeatureCollection> {

	public abstract void onSuccess(FeatureCollection featureCollection);
	
	public abstract void onError(String errorMessage);
	
}

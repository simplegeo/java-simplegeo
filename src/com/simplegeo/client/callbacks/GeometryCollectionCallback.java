package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.GeometryCollection;

public abstract class GeometryCollectionCallback 
	implements SimpleGeoCallback<GeometryCollection>{

	public abstract void onSuccess(GeometryCollection geometryCollection);

	public abstract void onError(String errorMessage);
	
}

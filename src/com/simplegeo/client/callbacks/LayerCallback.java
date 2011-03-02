package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.Layer;

public abstract class LayerCallback implements SimpleGeoCallback<Layer>{

	public abstract void onSuccess(Layer layer);

	public abstract void onError(String errorMessage);
	
}

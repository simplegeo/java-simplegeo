package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.LayerCollection;

public abstract class LayerCollectionCallback implements SimpleGeoCallback<LayerCollection>{

	public abstract void onSuccess(LayerCollection layerCollection);

	public abstract void onError(String errorMessage);
	
}

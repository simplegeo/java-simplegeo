package com.simplegeo.client.callbacks;

import java.util.logging.Logger;

import com.simplegeo.client.AbstractSimpleGeoClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

public class SimpleGeoCallback implements ISimpleGeoCallback {
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());

	@Override
	public void onSuccess(Object object) {
		if (Feature.class.isInstance(object)) {
			logger.info("feature");
		} else if (FeatureCollection.class.isInstance(object)) {
			logger.info("feature collection");
		} else {
			logger.info("map");
		}
	}

	@Override
	public void onError(String errorMessage) {
		logger.info("success");
	}

}
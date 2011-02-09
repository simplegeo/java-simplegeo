package com.simplegeo.client.callbacks;

import com.simplegeo.client.types.CategoryCollection;

public abstract class CategoryCollectionCallback 
	implements SimpleGeoCallback<CategoryCollection>{

	public abstract void onSuccess(CategoryCollection categoryCollection);

	public abstract void onError(String errorMessage);
	
}

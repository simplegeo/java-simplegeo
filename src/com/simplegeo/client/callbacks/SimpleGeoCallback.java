package com.simplegeo.client.callbacks;

public interface SimpleGeoCallback<E> {

	public void onSuccess(E e);
	
	public void onError(String errorMessage);
	
}

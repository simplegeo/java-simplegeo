package com.simplegeo.client.callbacks;

public interface ISimpleGeoCallback<E> {

	public void onSuccess(E e);
	
	public void onError(String errorMessage);
	
}

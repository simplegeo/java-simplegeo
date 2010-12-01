package com.simplegeo.client;

import com.simplegeo.client.http.IOAuthClient;

public interface ISimpleGeoClient {

	static public enum Handler { JSON, GEOJSON, SIMPLEGEO }
	
	/**
	 * @return the Http client used to execute all requests
	 */
	public IOAuthClient getHttpClient();

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#supportsFutureTasks()
	 */
	public abstract boolean supportsFutureTasks();

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#setFutureTask(boolean)
	 */
	public abstract void setFutureTask(boolean futureTask);

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#getFutureTask()
	 */
	public abstract boolean getFutureTask();

}
/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONObject;

/**
 * A simple interface that defines the proper required state
 * of a record in SimpleGeo.
 * 
 * @author Derek Smith
 */
public interface IRecord {

	/**
	 * @return the id associated with the record
	 */
	public String getRecordId();
	
	/**
	 * @return the layer where the record is held
	 */
	public String getLayer();
	
	/**
	 * @return the type associated with the record
	 * @see com.simplegeo.client.model.RecordType
	 */
	public String getObjectType();
	
	/**
	 * @return the latitude for the record
	 */
	public double getLatitude();
	
	/**
	 * @return the longitude for the record
	 */
	public double getLongitude();
	
	/**
	 * @return the time at which this record was created in milliseconds
	 */
	public long getCreated();
	
	/**
	 * @return the time at which this record will expire in milliseconds
	 */
	public long getExpiration();
	
	/**
	 * @return other values associated with this record
	 */
	public JSONObject getProperties();

}

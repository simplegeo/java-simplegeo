/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONObject;

/**
 * @author Derek Smith
 *
 */
public interface IRecord {

	public String getRecordId();
	
	public String getLayer();
	
	public String getObjectType();
	
	public double getLatitude();
	
	public double getLongitude();
	
	public long getCreated();
	
	public long getExpiration();
	
	public JSONObject getProperties();

}

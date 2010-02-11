/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * @author dsmith
 *
 */
public class DefaultRecord implements IRecord {
	
	private String recordId;
	private String layer;
	private String type;
	private long created;
	private long expiration;
	private double latitude;
	private double longitude;
	private JSONObject properties;

	public DefaultRecord(String recordId, String layer, String type, double longitude, double latitude) {
		
		this.recordId = recordId;
		this.layer = layer;
		this.type = type;
		this.longitude = longitude;
		this.latitude = latitude;
		setProperties(new JSONObject());
	
		this.created = System.currentTimeMillis();
		this.expiration = 0;
	}

	public DefaultRecord(String recordId, String layer, String type) {
		this(recordId, layer, type, 0.0, 0.0);
	}
	
	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getObjectType() {
		return type;
	}
	
	public void setObjectType(String type) {
		this.type = type;
	}
	
	public long getCreated() {
		return created;
	}
	
	public void setCreated(long created) {
		this.created = created;
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public JSONObject getProperties() {
		return this.properties;
	}
	
	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}
	
	public int getIntProperty(String key) {
		int value = 0;
		try {
			value = getProperties().getInt(key);
		} catch (JSONException e) {
			;
		}
		
		return value;
	}
	
	public void setIntProperty(String key, int value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public double getDoubleProperty(String key) {
		double value = 0.0;
		try {
			value = getProperties().getDouble(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	public void setDoubleProperty(String key, double value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}	
	}
	
	public long getLongProperty(String key) {
		long value = 0;
		try {
			value = getProperties().getLong(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	public void setLongProperty(String key, long value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}

	public Object getObjectProperty(String key) {
		Object value = null;
		try {
			value = getProperties().get(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	public void setObjectProperty(String key, Object value) {
		if(value == null)
			value = JSONObject.NULL;
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}

	@Override
	public String toString() {
		
		return String.format("<DefaultRecord id=%s, layer=%s, lat=%f, lon=%f, type=%s, created=%d>", 
				getRecordId(), getLayer(), getLatitude(), getLongitude(), getObjectType(), getCreated());
	}
	
}

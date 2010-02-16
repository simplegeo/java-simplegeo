/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The default implementation of a {@link com.simplegeo.client.model.IRecord}
 * 
 * @author Derek Smith
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

	/**
	 * Creates a new record.
	 * 
	 * @param recordId the id associated with the record
	 * @param layer the layer where the record is held
	 * @param type the type associated with the record 
	 * @param longitude
	 * @param latitude
	 * @see com.simplegeo.client.model.RecordType
	 */
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

	/**
	 * @param recordId the id associated with the record
	 * @param layer the layer where the record is held
	 * @param type the type associated with the record
	 * @see com.simplegeo.client.model.RecordType
	 */
	public DefaultRecord(String recordId, String layer, String type) {
		this(recordId, layer, type, 0.0, 0.0);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLayer()
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer where the record is held
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLatitude()
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLongitude()
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getRecordId()
	 */
	public String getRecordId() {
		return recordId;
	}
	
	/**
	 * @param recordId the id associated with the record
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getObjectType()
	 */
	public String getObjectType() {
		return type;
	}
	
	/**
	 * @param type the type associated with the record
	 */
	public void setObjectType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getCreated()
	 */
	public long getCreated() {
		return created;
	}
	
	/**
	 * @param created the time at which this record was created in milliseconds
	 */
	public void setCreated(long created) {
		this.created = created;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getExpiration()
	 */
	public long getExpiration() {
		return expiration;
	}
	
	/**
	 * @param expiration the time at which this record will expire in milliseconds
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getProperties()
	 */
	public JSONObject getProperties() {
		return this.properties;
	}
	
	/**
	 * @param properties other values associated with this record
	 */
	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}
	
	/**
	 * @param key the key to look-up in the {@link org.json.JSONObject} properties 
	 * @return the integer value
	 */
	public int getIntProperty(String key) {
		int value = 0;
		try {
			value = getProperties().getInt(key);
		} catch (JSONException e) {
			;
		}
		
		return value;
	}
	
	/**
	 * @param key the key to use
	 * @param value the integer value that will be assigned
	 * to the key in the properties {@link org.json.JSONObject}.
	 */
	public void setIntProperty(String key, int value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	/**
	 * @param key the key to look-up in the {@link org.json.JSONObject} properties 
	 * @return the double value
	 */
	public double getDoubleProperty(String key) {
		double value = 0.0;
		try {
			value = getProperties().getDouble(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	/**
	 * @param key the key to use
	 * @param value the double value that will be assigned
	 * to the key in the properties {@link org.json.JSONObject}.
	 */
	public void setDoubleProperty(String key, double value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}	
	}
	
	/**
	 * @param key the key to look-up in the {@link org.json.JSONObject} properties 
	 * @return the long value
	 */
	public long getLongProperty(String key) {
		long value = 0;
		try {
			value = getProperties().getLong(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	/**
	 * @param key the key to use
	 * @param value the long value that will be assigned
	 * to the key in the properties {@link org.json.JSONObject}.
	 */
	public void setLongProperty(String key, long value) {
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}

	/**
	 * @param key the key to look-up in the {@link org.json.JSONObject} properties 
	 * @return the Object value
	 */
	public Object getObjectProperty(String key) {
		Object value = null;
		try {
			value = getProperties().get(key);
		} catch (JSONException e) {
			;
		}	
		
		return value;
	}
	
	/**
	 * @param key the key to use
	 * @param value the Object value that will be assigned
	 * to the key in the properties {@link org.json.JSONObject}.
	 */
	public void setObjectProperty(String key, Object value) {
		if(value == null)
			value = JSONObject.NULL;
		try {
			getProperties().put(key, value);
		} catch (JSONException e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return String.format("<DefaultRecord id=%s, layer=%s, lat=%f, lon=%f, type=%s, created=%d>", 
				getRecordId(), getLayer(), getLatitude(), getLongitude(), getObjectType(), getCreated());
	}
	
}

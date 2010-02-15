/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import org.apache.log4j.Logger;

/**
 * @author Derek Smith

 */
public class GeoJSONRecord extends GeoJSONObject implements IRecord {
	
	private static final String TAG = GeoJSONRecord.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONRecord.class);
	
	/**
	 * @param recordId
	 * @param layer
	 * @param type
	 * @param created
	 * @param expiration
	 */
	public GeoJSONRecord(String recordId, String layer, String type, long created, long expiration) {
		
		setProperties(new JSONObject());
		
		setRecordId(recordId);
		setLayer(layer);
		setObjectType(type);
		setCreated(created);
		setExpiration(expiration);
		setLatitude(0.0);
		setLongitude(0.0);
		
		try {
			
			setType("Feature");
			
		} catch (JSONException e) {
			
			logger.debug("unable to locate type key");
			
		}
		
	}

	/**
	 * @param recordId
	 * @param layer
	 * @param type
	 */
	public GeoJSONRecord(String recordId, String layer, String type) {
		this(recordId, layer, type, System.currentTimeMillis(), 0);
	}
		
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getCreated()
	 */
	public long getCreated() {
		long created = 0;		
		try {
			created = super.getLong("created");
		} catch (JSONException e) {			
			logger.debug( "unable to locate created");
		}
		
		return created;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setCreated(long)
	 */
	public void setCreated(long created) {
		try {
			super.put("created", created);
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getExpiration()
	 */
	public long getExpiration() {
		long expires = 0;		
		try {
			expires = super.getLong("expiration");
		} catch (JSONException e) {			
			logger.debug( "unable to locate expiration");
		}
		
		return expires;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setExpiration(long)
	 */
	public void setExpiration(long expiration) {

		try {
			super.put("expiration", expiration);
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}
		
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLatitude()
	 */
	public double getLatitude() {
		double latitude = -1.0;		
		try {
			latitude = super.getLatitude();
		} catch (JSONException e) {			
			logger.debug( "unable to locate latitude");
		}
		
		return latitude;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.GeoJSONObject#setLatitude(double)
	 */
	public void setLatitude(double lat) {
		try {
			super.setLatitude(lat);
		} catch (JSONException e) {
			logger.debug( "unable to locate latitude");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLayer()
	 */
	public String getLayer() {
		String layer = null;		
		try {
			layer = super.getString("layer");
		} catch (JSONException e) {			
			logger.debug( "unable to locate layer");
		}
		
		return layer;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setLayer(String)
	 */
	public void setLayer(String layer) {
		try {
			super.put("layer", layer);
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}		
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getLongitude()
	 */
	public double getLongitude() {
		double longitude = -1.0;		
		try {
			longitude = super.getLongitude();
		} catch (JSONException e) {			
			logger.debug( "unable to locate longitude");
		}
		
		return longitude;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.GeoJSONObject#setLongitude(double)
	 */
	public void setLongitude(double lon) {
		try {
			super.setLongitude(lon);
		} catch (JSONException e) {
			logger.debug( "unable to locate longitude");
		}
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getProperties()
	 */
	public JSONObject getProperties() {
		JSONObject properties = null;		
		try {
			properties = super.getProperties();
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}
		
		return properties;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.GeoJSONObject#setProperties(org.json.JSONObject)
	 */
	public void setProperties(JSONObject properties) {
		
		try {
			super.setProperties(properties);
		} catch (JSONException e) {
			logger.debug( "unable to locate properties");
		}
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getRecordId()
	 */
	public String getRecordId() {
		String recordId = null;
		try {
			recordId = super.getString("id");
		} catch (JSONException e) {
			logger.debug( "unable to locate id");
		}
		
		return recordId;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setRecordId(String)
	 */
	public void setRecordId(String recordId) {
		try {
			super.put("id", recordId);
		} catch (JSONException e) {
			logger.debug( "unable to locate properties");
		}
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.model.IRecord#getObjectType()
	 */
	public String getObjectType() {
		
		String type = null;		
		try {
			type = super.getProperties().getString("type");
		} catch (JSONException e) {			
			logger.debug( "unable to locate type");
		}
		
		return type;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setObjectType(String)
	 */
	public void setObjectType(String type) {
		try {
			super.getProperties().put("type", type);
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}
	}

	/**
	 * @see com.simplegeo.client.model.DefaultRecord#getIntProperty(String)
	 */
	public int getIntProperty(String key) {
		int value = 0;
		try {
			value = super.getProperties().getInt(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
		
		return value;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setIntProperty(String, int)
	 */
	public void setIntProperty(String key, int value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#getDoubleProperty(String)
	 */
	public double getDoubleProperty(String key) {
		double value = 0.0;
		try {
			value = super.getProperties().getDouble(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setDoubleProperty(String, double)
	 */
	public void setDoubleProperty(String key, double value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#getLongProperty(String)
	 */
	public long getLongProperty(String key) {
		long value = 0;
		try {
			value = super.getProperties().getLong(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setLongProperty(String, long)
	 */
	public void setLongProperty(String key, long value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
	}

	/**
	 * @see com.simplegeo.client.model.DefaultRecord#getObjectProperty(String)
	 */
	public Object getObjectProperty(String key) {
		Object value = null;
		try {
			value = super.getProperties().get(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
	/**
	 * @see com.simplegeo.client.model.DefaultRecord#setObjectProperty(String, Object)
	 */
	public void setObjectProperty(String key, Object value) {
		if(value == null)
			value = JSONObject.NULL;
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
	}
		
}

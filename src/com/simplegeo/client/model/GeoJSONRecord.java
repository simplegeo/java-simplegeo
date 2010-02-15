/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import org.apache.log4j.Logger;

import com.simplegeo.client.encoder.GeoJSONEncoder;


/**
 * @author Derek Smith
 *
 */
public class GeoJSONRecord extends GeoJSONObject implements IRecord {
	
	private static final String TAG = GeoJSONRecord.class.getName();
	private static Logger logger = Logger.getLogger(GeoJSONRecord.class);
	
	public GeoJSONRecord(String recordId, String layer, String type, long created, long expiration) {
		super();
		
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

	public GeoJSONRecord(String recordId, String layer, String type) {
		this(recordId, layer, type, System.currentTimeMillis(), 0);
	}
	
	public GeoJSONRecord() {
		super();
	}
	
	public GeoJSONRecord(String type) {
		super(type);
	}

	public GeoJSONRecord(String type, String jsonString) throws JSONException {
		super(type, jsonString);
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
	
	public void setLatitude(double lat) {
		try {
			super.setLatitude(lat);
		} catch (JSONException e) {
			logger.debug( "unable to locate latitude");
		}
	}
	
	public String getLayer() {
		String layer = null;		
		try {
			layer = super.getString("layer");
		} catch (JSONException e) {			
			logger.debug( "unable to locate layer");
		}
		
		return layer;
	}
	
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
	
	public void setRecordId(String recordId) {
		try {
			super.put("id", recordId);
		} catch (JSONException e) {
			logger.debug( "unable to locate properties");
		}
	}

	public String getObjectType() {
		
		String type = null;		
		try {
			type = super.getProperties().getString("type");
		} catch (JSONException e) {			
			logger.debug( "unable to locate type");
		}
		
		return type;
	}
	
	public void setObjectType(String type) {
		try {
			super.getProperties().put("type", type);
		} catch (JSONException e) {			
			logger.debug( "unable to locate properties");
		}
	}

	public int getIntProperty(String key) {
		int value = 0;
		try {
			value = super.getProperties().getInt(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
		
		return value;
	}
	
	public void setIntProperty(String key, int value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
	}
	
	public double getDoubleProperty(String key) {
		double value = 0.0;
		try {
			value = super.getProperties().getDouble(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
	public void setDoubleProperty(String key, double value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
	}
	
	public long getLongProperty(String key) {
		long value = 0;
		try {
			value = super.getProperties().getLong(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
	public void setLongProperty(String key, long value) {
		try {
			super.getProperties().put(key, value);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}
	}

	public Object getObjectProperty(String key) {
		Object value = null;
		try {
			value = super.getProperties().get(key);
		} catch (JSONException e) {
			logger.debug( String.format("unable to locate %s", key));
		}	
		
		return value;
	}
	
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

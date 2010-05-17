/**
 * Copyright (c) 2009-2010, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
		this(recordId, layer, type, System.currentTimeMillis() / 1000, 0);
	}
		
	public GeoJSONRecord(String type, String jsonString) throws JSONException {
		super(type, jsonString);
	}

	public GeoJSONRecord(String string) {
		super(string);
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

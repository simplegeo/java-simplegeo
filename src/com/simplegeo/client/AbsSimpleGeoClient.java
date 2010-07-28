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

package com.simplegeo.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.geojson.GeoJSONObject;
import com.simplegeo.client.http.GeoJSONHandler;
import com.simplegeo.client.http.JSONHandler;
import com.simplegeo.client.http.RecordHandler;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.UnsupportedHandlerException;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.Envelope;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.query.HistoryQuery;
import com.simplegeo.client.query.NearbyQuery;

/**
 * Extracts as much common code as possible between the SimpleGeoClient and the SimpleGeoURLConnClient.
 * 
 * @author Derek Smith (refactored by Mark Fogle)
 */

public abstract class AbsSimpleGeoClient implements SimpleGeoClientIfc {
	
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
	
	protected static Logger logger = Logger.getLogger(SimpleGeoClient.class.getName());
	
	protected static final String mainURL = "http://api.simplegeo.com/0.1";
	
	protected static SimpleGeoClientIfc sharedLocationService = null;
	
	protected RecordHandler recordHandler = null;
	protected GeoJSONHandler geoJSONHandler = null;
	protected JSONHandler jsonHandler = null;
	protected SimpleGeoHandler simpleGeoHandler = null;
	
	/**
	 * Tells the service whether to make the Http call on the same thread.  Note: if the underlying
	 * client doesn't handle future tasks, this flag will be ignored.
	 */
	public boolean futureTask = false; 
		
	protected AbsSimpleGeoClient() {
		
		setHandler(Handler.JSON, new JSONHandler());
		setHandler(Handler.RECORD, new RecordHandler());
		setHandler(Handler.GEOJSON, new GeoJSONHandler());
		
	}

	/**
	 * Set the handler per type.
	 * 
	 * This is useful if subclasses or implementations of the model
	 * objects change and the response object needs to be built 
	 * differently.
	 * 
	 * @param type the Handler type to override
	 * @param handler the handler must be a subclass of SimpleGeoHandler to
	 * ensure that a valid response was recieved
	 */
	public void setHandler(Handler type, SimpleGeoHandler handler) {
		
		switch(type) {
			case GEOJSON:
				geoJSONHandler = (GeoJSONHandler)handler;
				break;
			case RECORD:
				recordHandler = (RecordHandler)handler;
				break;
			case JSON:
				jsonHandler = (JSONHandler)handler;
				break;
			default:
				break;
		}
	}
	
	/**
	 * @param type the type of handler to retrieved defined by 
	 * {@link com.simplegeo.client.Handler}
	 * @return the instance of {@link com.simplegeo.client.http.SimpleGeoHandler}
	 * that is associated with the type
	 */
	public SimpleGeoHandler getHandler(Handler type) {
		
		SimpleGeoHandler handler = null;
		switch(type) {
			
			case GEOJSON:
				handler = geoJSONHandler;
				break;
			case RECORD:
				handler = recordHandler;
				break;
			case JSON:
				handler = jsonHandler;
				break;
			default:
				break;
				
		}
		
		return handler;
	}
		
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#retrieve(com.simplegeo.client.model.IRecord)
	 */
	public Object retrieve(IRecord defaultRecord) throws IOException {
		
		List<IRecord> list = new ArrayList<IRecord>();
		list.add(defaultRecord);
		
		Object object = retrieve(list);
		
		//
		// If this is neither a list nor an IRecord, it must be a FutureTask, so return it
		// directly.
		//
		if (!List.class.isInstance(object) && !IRecord.class.isInstance(object))
			return object;
		else 
		    return getIRecord(object);
	}	
					
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#retrieve(java.util.List)
	 */
	public Object retrieve(List<IRecord> records) throws IOException {
		
		if(!records.isEmpty()) {
			
			IRecord firstRecord = records.get(0);
			return retrieve(getLayer(records), getRecordIds(records), getHandler(firstRecord));
		}
		
		return null;
	}
	
	/**
	 * Retrieve a information from SimpleGeo about all the record ids within
	 * the list that are contained within a layer.
	 * 
	 * @param layer the name of the layer to find the records in
	 * @param recordIds the ids of records to retrieve
	 * @param handler the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false
	 * then the return value will be the result of the response based on the handler used. Otherwise,
	 * the return value will be a {@link java.util.concurrent.FutureTask} (if the underlying client supports
	 * asynchronous requests).
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	private Object retrieve(String layer, String recordIds, SimpleGeoHandler handler) throws IOException {
		
		String uri = mainURL + String.format("/records/%s/%s.json", layer, recordIds);
		
		logger.info( String.format("retrieving %s from %s", layer, recordIds));
		
		return executeGet (uri, handler);
	}
		
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#update(com.simplegeo.client.model.IRecord)
	 */
	public Object update(IRecord record) throws IOException {		
		return update(getGeoJSONObject(record));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#update(java.util.List)
	 */
	public Object update(List<IRecord> records) throws IOException {		
		return update(getGeoJSONObject(records));
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#update(com.simplegeo.client.geojson.GeoJSONObject)
	 */
	public Object update(GeoJSONObject geoJSONObject) throws IOException {
		
		GeoJSONObject record = null;
		if(geoJSONObject.isFeature()) {
			
			record = new GeoJSONRecord("FeatureCollection");
			
			try {
				
				JSONArray features = new JSONArray();
				features.put(geoJSONObject);
				record.setFeatures(features);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} else {
			
			record = geoJSONObject;
			
		}
		
		return update(getLayer(record), getGeoJSONObject(record), getHandler(record));
		
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#update(java.lang.String, com.simplegeo.client.geojson.GeoJSONObject, com.simplegeo.client.http.SimpleGeoHandler)
	 */
	public Object update(String layer, GeoJSONObject geoJSONObject, SimpleGeoHandler handler) 
	throws IOException {
	
		String uri = mainURL + String.format("/records/%s.json", layer);
		
		String jsonString = geoJSONObject.toString();
		
		logger.info(String.format("updating %s", jsonString));
		
		return executePost (uri, jsonString, handler);
	}
	

	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#delete(com.simplegeo.client.model.IRecord)
	 */
	public Object delete(IRecord record) throws IOException {
		
		if(record != null)
			return delete(record.getLayer(), record.getRecordId(), getHandler(record));
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#delete(java.lang.String, java.lang.String, com.simplegeo.client.http.SimpleGeoHandler)
	 */
	public Object delete(String layer, String recordId, SimpleGeoHandler handler) 
		throws IOException {
		
		String uri = mainURL + String.format("/records/%s/%s.json", layer, recordId);
		logger.info(String.format("deleting %s at %s", layer, recordId));
		
		return executeDelete (uri, handler);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#history(com.simplegeo.client.query.HistoryQuery)
	 */
	public Object history(HistoryQuery query) throws IOException{
		return history(query, Handler.GEOJSON);
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#history(com.simplegeo.client.query.HistoryQuery, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object history(HistoryQuery query, Handler type) 
		throws IOException {
		
		if(type != Handler.GEOJSON)
			throw new UnsupportedHandlerException(400, "The contains endpoint can only return GeoJSON objects.");
		
		String uri = mainURL + query.getUri();
		
		return executeGet (buildUrl(uri, query.getParams()), getHandler(type));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#nearby(com.simplegeo.client.query.NearbyQuery)
	 */
	public Object nearby(NearbyQuery query) throws IOException {
		return nearby(query, Handler.GEOJSON);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#nearby(com.simplegeo.client.query.NearbyQuery, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object nearby(NearbyQuery query, Handler type) throws IOException {
		
		String uri = mainURL + query.getUri();
		
		return executeGet (buildUrl(uri, query.getParams()), getHandler(type));
	}
		
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#reverseGeocode(double, double)
	 */
	public Object reverseGeocode(double lat, double lon)
			throws IOException {
		
		String uri = mainURL + String.format("/nearby/address/%f,%f.json", lat, lon);
		
		return executeGet (uri, getHandler(Handler.GEOJSON));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#density(int, int, double, double)
	 */
	public Object density(int day, int hour, double lat, double lon)
	throws IOException {
		return density(day, hour, lat, lon, Handler.GEOJSON);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#density(int, int, double, double, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object density(int day, int hour, double lat, double lon, Handler type)
			throws IOException {
		// /density/{dayname}/{hour}/{lat},{lon}.json
		String dayname = "";
		switch (day)
		{
		case Calendar.SUNDAY:
			dayname = "sun";
			break;
		case Calendar.MONDAY:
			dayname = "mon";
			break;
		case Calendar.TUESDAY:
			dayname = "tue";
			break;
		case Calendar.WEDNESDAY:
			dayname = "wed";
			break;
		case Calendar.THURSDAY:
			dayname = "thu";
			break;
		case Calendar.FRIDAY:
			dayname = "fri";
			break;
		case Calendar.SATURDAY:
			dayname = "sat";
			break;
		}
		
		String uri = mainURL;
		if (hour >=0 && hour <= 23)
		{
			uri += String.format("/density/%s/%d/%f,%f.json", dayname, hour, lat, lon);
		}
		else
		{
			uri += String.format("/density/%s/%f,%f.json", dayname, lat, lon);
		}
		
		return executeGet (uri, getHandler(Handler.GEOJSON));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#contains(double, double)
	 */
	public Object contains(double lat, double lon) throws IOException {
		return contains(lat, lon, Handler.JSON);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#contains(double, double, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object contains(double lat, double lon, Handler type)
	throws IOException {
		if(type != Handler.JSON)
			throw new UnsupportedHandlerException(400, "The contains endpoint can only return JSON objects.");

		String uri = mainURL + String.format("/contains/%f,%f.json", lat, lon);
		
		return executeGet (uri, getHandler(type));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#boundaries(java.lang.String)
	 */
	public Object boundaries(String featureId)
	throws IOException {
		
		return boundaries(featureId, Handler.GEOJSON);
		
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#boundaries(java.lang.String, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object boundaries(String featureId, Handler type)
	throws IOException {
		
		String uri = mainURL + String.format("/boundary/%s.json", featureId);
		
		return executeGet (uri, getHandler(type));
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#overlaps(com.simplegeo.client.model.Envelope, int, java.lang.String)
	 */
	public Object overlaps(Envelope envelope, int limit, String featureType) 
	throws IOException {
		
		return overlaps(envelope, limit, featureType, Handler.JSON);
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.SimpleGeoClientIfc#overlaps(com.simplegeo.client.model.Envelope, int, java.lang.String, com.simplegeo.client.SimpleGeoClientIfc.Handler)
	 */
	public Object overlaps(Envelope envelope, int limit, String featureType, Handler type)
	throws IOException {
		
		if(type != Handler.JSON)
			throw new UnsupportedHandlerException(400, "The contains endpoint can only return JSON objects.");
		
		String uri = mainURL + String.format("/overlaps/%s.json", envelope.toString());
		
		Map<String, String> params = new HashMap<String, String>();
		
		if (limit > 0)
			params.put("limit", Integer.toString(limit));
		
		if (featureType != null) 			
			params.put("type", featureType);
		
		return executeGet (buildUrl(uri, params), getHandler(type));
	}
	
	private String buildUrl(String url, Map<String, ?> parameters) {
		
		if(parameters == null)
			return url;
		
		StringBuilder sb = new StringBuilder(url);
		boolean first = true;
		for (Entry<String, ?> entry : parameters.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(first ? "?" : "&");
				try {
					String key = URLEncoder.encode(entry.getKey(), DEFAULT_CONTENT_CHARSET);
					String value = URLEncoder.encode(entry.getValue().toString(), DEFAULT_CONTENT_CHARSET);
					sb.append(key).append("=").append(value);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				first = false;
			}
		}
		return sb.toString();
	}
		
	private GeoJSONObject getGeoJSONObject(Object object) {
		
		GeoJSONObject jsonObject = null;
		if(JSONObject.class.isInstance(object)) {
			try {
				jsonObject = (GeoJSONObject)object;
				jsonObject.flatten();
			} catch (JSONException e) {
				logger.info("unable to flatten GeoJSONObject");
			}
			
		} else {
			jsonObject = GeoJSONEncoder.getGeoJSONRecord((DefaultRecord)object);
		}
		
		return jsonObject;
	}
	
	private GeoJSONObject getGeoJSONObject(List<IRecord> records) {
		
		GeoJSONObject geoJSONObject = null;
		
		if(records != null && !records.isEmpty()) {
			
			try {
			
				geoJSONObject = new GeoJSONObject("FeatureCollection");
			
				JSONArray features = new JSONArray();
				for(IRecord record : records) {
				
					GeoJSONObject jsonObject = getGeoJSONObject(record); 
					if(jsonObject != null) {
						features.put(jsonObject);
					}
				}
			
				geoJSONObject.put("features", features);
				
			} catch (JSONException e) {
				
				logger.info(String.format("unable to create GeoJSONObject from %s", records));
				
			}
		}
		
		return geoJSONObject;
	}
	
	private String getLayer(List<? extends Object> records) {
		
		String layer = null;
		if(records != null && !records.isEmpty())
			layer = getLayer(records.get(0));
		
		return layer;
	}
	
	private String getLayer(Object record) {
	
		String layer = null;
		
		if(GeoJSONObject.class.isInstance(record))
		{
			try {
				
				GeoJSONObject geoJSONObject = (GeoJSONObject)record;
				if(geoJSONObject.isFeatureCollection()) {
						
					JSONArray features = geoJSONObject.getFeatures();
					if(features != null && features.length() > 0)
						layer = features.getJSONObject(0).getString("layer");
					
				} else if(geoJSONObject.isFeature()) {
					
					layer = geoJSONObject.getString("layer");
				}

			} catch (JSONException e) {
				
				logger.info(String.format("unable to locate layer for the %s", record));
			} 			
			
		} else if(IRecord.class.isInstance(record)) {
			
			IRecord r = (IRecord)record;
			layer = r.getLayer();
		}
			
		return layer;
	}
	
	private String getRecordIds(List<? extends Object> objects) {
	
		String recordIds = null;
		
		for(Object object : objects) {
			
			String recordId = getRecordId(object);
			
			if(recordIds == null)
				recordIds = recordId;
			else
				recordIds += "," + recordId;
			
		}
		
		return recordIds;
	}
	
	private String getRecordId(Object object) {
		
		String recordId = null;
		
		if(IRecord.class.isInstance(object))
			recordId = ((IRecord)object).getRecordId();
		else if(GeoJSONObject.class.isInstance(object)) {
			
			try {
				
				GeoJSONObject geoJSONObject = (GeoJSONObject)object;
				if(geoJSONObject.isFeature()) {
					
					recordId = geoJSONObject.getString("id");
					
				} else if(geoJSONObject.isFeatureCollection()) {
						
					JSONArray features = geoJSONObject.getFeatures();
					if(features != null && features.length() > 0) {
						
						int size = features.length();
						for(int index = 0; index < size; index++) {
							
							String id = features.getJSONObject(index).getString("id");
							if(recordId == null)
								recordId = id;
							else
								recordId += "," + recordId;
						}
						
					}
				}

			} catch (JSONException e) {
				
				logger.info(String.format("unable to locate layer for the %s", object));
			} 			
	
		}
		
		return recordId;
	}
	
	private IRecord getIRecord(Object object) {
		IRecord record = null;
		if(object != null) {
			
			if(List.class.isInstance(object)) {
				
				@SuppressWarnings("unchecked")
				List<IRecord> list = (List<IRecord>)object;
				if(!list.isEmpty())
					record = list.get(0);
				
			} else if(IRecord.class.isInstance(object)) {
				record = (IRecord)object;
			}
		}
		
		return record;
	}

	private SimpleGeoHandler getHandler(Object record) {
		
		SimpleGeoHandler handler = geoJSONHandler;
		if(DefaultRecord.class.isInstance(record))
			handler = recordHandler; 
		
		return handler;
	}
	
	protected void dealWithAuthorizationException(Exception e) throws APIException {
		
		e.printStackTrace();
		throw new APIException(SimpleGeoHandler.NOT_AUTHORIZED, e.getMessage());
		
	}
	
	protected abstract Object executeGet (String uri, SimpleGeoHandler handler) throws IOException;
	
	protected abstract Object executePost (String uri, String jsonPayload, SimpleGeoHandler handler) throws IOException;

	protected abstract Object executeDelete (String uri, SimpleGeoHandler handler) throws IOException;

}

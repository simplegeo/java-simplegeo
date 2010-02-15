/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.util.Log;
import org.apache.log4j.Logger;

import ch.hsr.geohash.GeoHash;

import com.simplegeo.client.concurrent.RequestThreadPoolExecutor;
import com.simplegeo.client.encoder.GeoJSONEncoder;
import com.simplegeo.client.http.GeoJSONHandler;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.RecordHandler;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.utilities.SimpleGeoUtilities;

/**
 * @author Derek Smith
 *
 */
public class LocationService {
	
	private static final String TAG = LocationService.class.getName();
	private static Logger logger = Logger.getLogger(LocationService.class);
	
	private static final String mainURL = "http://api.simplegeo.com/0.1";
	
	private static LocationService sharedLocationService = null;
	
	private OAuthHttpClient httpClient = null;
	private RequestThreadPoolExecutor threadExecutor = null;
	
	private RecordHandler recordHandler = null;
	private GeoJSONHandler geoJSONHandler = null;
	private SimpleGeoHandler simpleGeoHandler = null;
	
	public boolean futureTask = false; 
	
	static public enum Handler { GEOJSON, RECORD, SIMPLEGEO }
	
	static public LocationService getInstance() {
		
		if(sharedLocationService == null)
			sharedLocationService = new LocationService();
		
		return sharedLocationService;
	}
	
	private LocationService() {
		
		this.httpClient = new OAuthHttpClient();
		this.threadExecutor = new RequestThreadPoolExecutor(TAG);
		
		setHandler(Handler.RECORD, new RecordHandler());
		setHandler(Handler.GEOJSON, new GeoJSONHandler());
		
		this.simpleGeoHandler = new SimpleGeoHandler();
	}

	public void setHandler(Handler type, SimpleGeoHandler handler) {
		
		switch(type) {
			case GEOJSON:
				geoJSONHandler = (GeoJSONHandler)handler;
				break;
			case RECORD:
				recordHandler = (RecordHandler)handler;
				break;
			default:
				break;
		}
	}
	
	public SimpleGeoHandler getHandler(Handler type) {
		
		SimpleGeoHandler handler = null;
		switch(type) {
			
			case GEOJSON:
				handler = geoJSONHandler;
				break;
			case RECORD:
				handler = recordHandler;
				break;
			default:
				break;
				
		}
		
		return handler;
	}
		
	public Object retrieve(IRecord defaultRecord) throws ClientProtocolException, IOException {
		
		List<IRecord> list = new ArrayList<IRecord>();
		list.add(defaultRecord);
		
		Object object = retrieve(list);
		
		return futureTask ? object : getIRecord(retrieve(list));
	}	
					
	public Object retrieve(List<IRecord> records) throws ClientProtocolException, IOException {
		
		if(!records.isEmpty()) {
			
			IRecord firstRecord = records.get(0);
			return retrieve(getLayer(records), getRecordIds(records), getHandler(firstRecord));
		}
		
		return null;
	}
	
	private Object retrieve(String layer, String recordIds, SimpleGeoHandler handler) throws ClientProtocolException, IOException {
		
		String uri = mainURL + String.format("/records/%s/%s.json", layer, recordIds);
		
		logger.debug( String.format("retrieving %s from %s", layer, recordIds));
		
		return execute(new HttpGet(uri), handler);
	}
		
	public Object update(IRecord record) throws ClientProtocolException, IOException {		
		return update(getGeoJSONObject(record));
	}
	
	public Object update(List<IRecord> records) throws ClientProtocolException, IOException {		
		return update(getGeoJSONObject(records));
	}

	public Object update(GeoJSONObject geoJSONRecord) throws ClientProtocolException, IOException {
		
		GeoJSONObject record = null;
		if(geoJSONRecord.isFeature()) {
			
			record = new GeoJSONRecord("FeatureCollection");
			
			try {
				
				JSONArray features = new JSONArray();
				features.put(geoJSONRecord);
				record.setFeatures(features);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} else {
			
			record = geoJSONRecord;
			
		}
		
		return update(getLayer(record), getGeoJSONObject(record), getHandler(record));
		
	}
	
	public Object update(String layer, GeoJSONObject geoJSONObject, SimpleGeoHandler handler) 
	throws ClientProtocolException, IOException {
	
		String uri = mainURL + String.format("/records/%s.json", layer);
		HttpPost post = new HttpPost(uri);
		
		String jsonString = geoJSONObject.toString();
		post.setEntity(new ByteArrayEntity(jsonString.getBytes()));
		
		logger.debug( String.format("updating %s", jsonString));
		
		return execute(post, handler);
	}
	
	public Object delete(IRecord record) throws ClientProtocolException, IOException {
		
		if(record != null)
			return delete(record.getLayer(), record.getRecordId(), getHandler(record));
		
		return null;
	}
	
	private Object delete(String layer, String recordId, SimpleGeoHandler handler) 
		throws ClientProtocolException, IOException {
		
		String uri = mainURL + String.format("/records/%s/%s.json", layer, recordId);
		logger.debug( String.format("deleting %s at %s", layer, recordId));
		return execute(new HttpDelete(uri), handler);
	}
	
	public Object nearby(GeoHash geoHash, List<String> layers, List<String> types, int limit) 
						throws ClientProtocolException, IOException {
		return nearby(geoHash, layers, types, limit, Handler.GEOJSON);
	}

	
	public Object nearby(GeoHash geoHash, List<String> layers, List<String> types, int limit, Handler type) 
						throws ClientProtocolException, IOException {
		
		String uri = mainURL + String.format("/nearby/%s.json", geoHash.toBase32());
		HttpGet request = new HttpGet(uri);
		
		BasicHttpParams params = new BasicHttpParams();
		params.setIntParameter("limit", limit);
		params.setParameter("types", SimpleGeoUtilities.commaSeparatedString(types));
		params.setParameter("layers", SimpleGeoUtilities.commaSeparatedString(layers));
		
		request.setParams(params);
		return execute(request, getHandler(type));
	}
	
	public Object nearby(double lat, double lon, double radius, List<String> layers, List<String> types, int limit) 
		throws ClientProtocolException, IOException {
		return nearby(lat, lon, radius, layers, types, limit, Handler.GEOJSON);
	}

	
	public Object nearby(double lat, double lon, double radius, List<String> layers, List<String> types, int limit, Handler type) 
						throws ClientProtocolException, IOException {

		String uri = mainURL + String.format("/nearby/%f,%f.json", lat, lon);
		HttpGet request = new HttpGet(uri);
		
		BasicHttpParams params = new BasicHttpParams();
		params.setIntParameter("limit", limit);
		params.setDoubleParameter("radius", radius);
		params.setParameter("types", SimpleGeoUtilities.commaSeparatedString(types));
		params.setParameter("layers", SimpleGeoUtilities.commaSeparatedString(layers));
		
		request.setParams(params);
		return execute(request, getHandler(type));

	}
	
	public Object reverseGeocode(double lat, double lon)
			throws ClientProtocolException, IOException {
		
		String uri = mainURL + String.format("/nearby/address/%f,%f.json", lat, lon);
		HttpGet request = new HttpGet(uri);
		
		return execute(request, getHandler(Handler.GEOJSON));
	}
	
	public OAuthHttpClient getHttpClient() {
		return this.httpClient;
	}
		
	private Object execute(HttpUriRequest request, SimpleGeoHandler handler)
									throws ClientProtocolException, IOException {
		
		logger.debug( String.format("sending %s", request.toString()));
		
		if(futureTask) {
			
			final HttpUriRequest finalRequest = request;
			final SimpleGeoHandler finalHandler = handler;
			
			FutureTask<Object> future = 
				new FutureTask<Object>(new Callable<Object>() {
					public Object call() throws ClientProtocolException, IOException {
						Object object = null;
						try {
							object = httpClient.executeOAuthRequest(finalRequest, finalHandler);
						} catch (OAuthMessageSignerException e) {
							dealWithAuthorizationException(e);
						} catch (OAuthExpectationFailedException e) {
							dealWithAuthorizationException(e);
						} catch (OAuthCommunicationException e) {
							dealWithAuthorizationException(e);
						}
						
						return object;
					}
				});
			
			threadExecutor.execute(future);
			return future;
			
		} else {
			
			Object object = null;
			try {
				object = httpClient.executeOAuthRequest(request, handler);
			} catch (OAuthMessageSignerException e) {
				dealWithAuthorizationException(e);
			} catch (OAuthExpectationFailedException e) {
				dealWithAuthorizationException(e);
			} catch (OAuthCommunicationException e) {
				dealWithAuthorizationException(e);
			};
			
			return object;
		}
		
	}
	
	private GeoJSONObject getGeoJSONObject(Object object) {
		
		GeoJSONObject jsonObject = null;
		if(JSONObject.class.isInstance(object)) {
			try {
				jsonObject = (GeoJSONObject)object;
				jsonObject.flatten();
			} catch (JSONException e) {
				logger.debug( "unable to flatten GeoJSONObject");
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
				
				logger.debug( String.format("unable to create GeoJSONObject from %s", records));
				
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
				
				logger.debug( String.format("unable to locate layer for the %s", record));
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
				
				logger.debug( String.format("unable to locate layer for the %s", object));
			} 			

			
		}
		
		return recordId;
	}
	
	private IRecord getIRecord(Object object) {
		IRecord record = null;
		if(object != null) {
			
			if(List.class.isInstance(object)) {
				
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
	
	private void dealWithAuthorizationException(Exception e) throws APIException {
		
		e.printStackTrace();
		throw new APIException(SimpleGeoHandler.NOT_AUTHORIZED, e.getMessage());
		
	}

}

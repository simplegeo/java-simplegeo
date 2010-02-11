/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import ch.hsr.geohash.GeoHash;

import com.simplegeo.android.sdk.service.LocationService;

/**
 * @author dsmith
 *
 */
public class Layer {
	
	private String name = null;
	private ArrayList<IRecord> records = null;
	
	public Layer(String name) {
		this.name = name;
		this.records = new ArrayList<IRecord>();
	}
	
	public ArrayList<IRecord> getRecords() {
		return this.records;
	}
	
	public void add(IRecord record) {
		
		// Attempt to update the layer name.
		if(DefaultRecord.class.isInstance(record))
			((DefaultRecord)record).setLayer(name);
		else if(GeoJSONRecord.class.isInstance(record)) {
		
			try {
				((GeoJSONRecord)record).getProperties().put("layer", name);
			} catch (JSONException e) {
				;
			}
		}
		
		records.add(record);
	}
	
	public void add(List<IRecord> records) {
		for(IRecord record : records)
			add(record);
	}
	
	public void remove(IRecord record) {
		records.remove(record);
	}
	
	public void remove(List<IRecord> records) {
		for(IRecord record : records)
			remove(record);
	}
	
	public IRecord getRecord(int index) {
		return records.get(index);
	}
	
	public IRecord removeRecord(int index) {
		return records.remove(index);
	}
	
	public int size() {
		return records.size();
	}
	
	public void update() throws ClientProtocolException, IOException {
		LocationService.getInstance().update(records);
	}
	
	public void retrieve() throws ClientProtocolException, IOException {
		LocationService.getInstance().retrieve(records);
	}
		
	public List<IRecord> nearby(GeoHash geoHash, List<String> types, int limit)
				throws ClientProtocolException, IOException {
		
		ArrayList<String> layers = new ArrayList<String>();
		layers.add(name);
		List<IRecord> nearby = (List<IRecord>)LocationService.getInstance().nearby(geoHash, layers, types, limit);
		return nearby;		
	}
	
	public List<IRecord> nearby(double lat, double lon, double radius, List<String> types, int limit) 
				throws ClientProtocolException, IOException {
		
		ArrayList<String> layers = new ArrayList<String>();
		layers.add(name);
		List<IRecord> nearby = (List<IRecord>)LocationService.getInstance().nearby(lat, lon, radius, layers, types, limit);
		return nearby;
		
	}
	
	public List<IRecord> nearby(int lat, int lon, double radius, List<String> types, int limit) 
		throws ClientProtocolException, IOException {
		return nearby(1000000 * lat, 1000000 * lon, radius, types, limit);
	}

}

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import ch.hsr.geohash.GeoHash;

import com.simplegeo.client.service.LocationService;

/**
 * A Layer object has the capability of retrieving and updating multiple records. 
 * The records must already be registered with the internal array of the object
 * in order for any requests to be successful.  
 * 
 * @author Derek Smith
 */
public class Layer {
	
	private String name = null;
	private ArrayList<IRecord> records = null;
	
	/**
	 * Initializes a new Layer object with the name.
	 * 
	 * @param name the name of the layer (e.g. com.derek.testing)
	 */
	public Layer(String name) {
		this.name = name;
		this.records = new ArrayList<IRecord>();
	}
	
	/**
	 * @return all records that have been registered with the object
	 */
	public ArrayList<IRecord> getRecords() {
		return this.records;
	}
	
	/**
	 * Adds the record to the interal list and attempts to update the
	 * layer value.
	 * 
	 * @param record the newly registered record 
	 */
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
	
	/**
	 * Registers a list of records with the layer object
	 * 
	 * @param records the list of records 
	 */
	public void add(List<IRecord> records) {
		
		for(IRecord record : records)
			add(record);
		
	}
	
	/**
	 * @param record the record to remove (unregister) from the Layer
	 * object
	 */
	public void remove(IRecord record) {
		records.remove(record);
	}
	
	/**
	 * @param records the list of records to remove (unregister) from the
	 * Layer object
	 */
	public void remove(List<IRecord> records) {
		for(IRecord record : records)
			remove(record);
	}
	
	/**
	 * @param index the index of the record
	 * @return the record at the index
	 */
	public IRecord getRecord(int index) {
		return records.get(index);
	}
	
	/**
	 * @param index the index of the record to remove
	 * @return the removed record
	 */
	public IRecord removeRecord(int index) {
		return records.remove(index);
	}
	
	/**
	 * @return the amount of records registerd with the Layer object
	 */
	public int size() {
		return records.size();
	}
	
	/**
	 * Updates all records that are registered with the Layer object.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void update() throws ClientProtocolException, IOException {
		LocationService.getInstance().update(records);
	}
	
	/**
	 * Retrieves all records that are registered witht the Layer object.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void retrieve() throws ClientProtocolException, IOException {
		LocationService.getInstance().retrieve(records);
	}
		
	/**
	 * Returns a list of nearby records for a geohash.
	 * 
	 * @param geoHash the bounding box to search in
	 * @param types the types of object to retrieve @see com.simplegeo.client.model.RecordTypes
	 * @param limit the maximum value of records to retrieve
	 * @return a list of nearby records 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<IRecord> nearby(GeoHash geoHash, List<String> types, int limit)
				throws ClientProtocolException, IOException {
		List<IRecord> nearby = (List<IRecord>)LocationService.getInstance().nearby(geoHash, name, types, limit);
		return nearby;		
	}
	
	/**
	 * Returns a list of nearby records for a give lat,long coordinate.
	 * 
	 * @param lat the latitude to use
	 * @param lon the longitude to use
	 * @param radius the radius to search in
	 * @param types the types of objects to retrieve @see com.simplegeo.client.model.RecordTypes
	 * @param limit the maximum value of records to retrieve
	 * @return a list of nearby records
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<IRecord> nearby(double lat, double lon, double radius, List<String> types, int limit) 
				throws ClientProtocolException, IOException {
		List<IRecord> nearby = (List<IRecord>)LocationService.getInstance().nearby(lat, lon, radius, name, types, limit);
		return nearby;
		
	}
	
}

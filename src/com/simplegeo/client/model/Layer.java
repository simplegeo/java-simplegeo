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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.json.JSONException;

import com.simplegeo.client.SimpleGeoClient;
import com.simplegeo.client.query.NearbyQuery;

/**
 * A Layer object has the capability of retrieving and updating multiple records. 
 * The records must already be registered with the internal array of the object
 * in order for any requests to be successful.  
 * 
 * @author Derek Smith
 */
public class Layer {
	
	private String name = null;
	private List<IRecord> records = null;
	@SuppressWarnings("unused")
	private List<String> responseIds = null;
	
	/**
	 * Initializes a new Layer object with the name.
	 * 
	 * @param name the name of the layer (e.g. com.derek.testing)
	 */
	public Layer(String name) {
		this.name = name;
		this.records = new ArrayList<IRecord>();
		this.responseIds = new ArrayList<String>();
	}
	
	/**
	 * @return all records that have been registered with the object
	 */
	public List<IRecord> getRecords() {
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
	 * @throws IOException
	 */
	public void update() throws IOException {
		SimpleGeoClient.getInstance().update(records);
	}
	
	/**
	 * Retrieves all records that are registered witht the Layer object.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void retrieve() throws IOException {
		GeoJSONRecord updatedRecords = waitForRecords(SimpleGeoClient.getInstance().retrieve(records));

		try {
			if(updatedRecords != null && updatedRecords.getFeatures() != null)
				this.records = (List<IRecord>)updatedRecords.getFeatures();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Returns a list of nearby records for a geohash.
	 * 
	 * @param query the nearby query
	 * @return  
	 * @throws IOException
	 */
	public GeoJSONRecord nearby(NearbyQuery query)
				throws IOException {
		GeoJSONRecord nearby = null;
		nearby = (GeoJSONRecord)SimpleGeoClient.getInstance().nearby(query);
		return waitForRecords(nearby);		
	}
		
	/**
	 * Returns the name of this layer.
	 * 
	 * @return the name of the layer
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * The SimpleGeoClient has the ability to return either FutureTasks
	 * or Lists of records. We need to have a way to handle both return
	 * objects.
	 */
	private GeoJSONRecord waitForRecords(Object returnObject) {
		GeoJSONRecord records = null;
		if(returnObject instanceof GeoJSONRecord)
			records = (GeoJSONRecord)returnObject;
		else if(returnObject instanceof FutureTask<?>)
			try {
				records = (GeoJSONRecord)((FutureTask<?>)returnObject).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		
		return records;
	}
}

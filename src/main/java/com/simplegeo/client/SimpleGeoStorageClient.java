/**
 * Copyright (c) 2010-2011, SimpleGeo
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.Record;

/*
 * Class for interacting with the SimpleGeo Storage API.
 * 
 * @author Pranil Kanderi
 */
public class SimpleGeoStorageClient extends AbstractSimpleGeoClient {
	
	protected static SimpleGeoStorageClient storageClient;
	
	/**
	 * Method that ensures we only have one instance of the {@link com.simplegeo.client.SimpleGeoStorageClient} instantiated.
	 * 
	 * @return SimpleGeoStorageClient
	 */
	public static SimpleGeoStorageClient getInstance() {
		if (storageClient == null) { storageClient = new SimpleGeoStorageClient(); }
		return storageClient;		
	}

	/**
	 * {@link com.simplegeo.client.SimpleGeoStorageClient} constructor
	 */
	private SimpleGeoStorageClient() {
		super();
		
		endpoints.put("singleRecord", "0.1/records/%s/%s.json");
		endpoints.put("multipleRecords", "0.1/records/%s.json");
		endpoints.put("history", "0.1/records/%s/%s/history.json");
		endpoints.put("searchByLatLon", "0.1/records/%s/nearby/%f,%f.json");
		endpoints.put("searchByIP", "0.1/records/%s/nearby/%s.json");
		endpoints.put("layer", "0.1/layers/%s.json");
		endpoints.put("allLayers", "0.1/layers.json");
	}
	
	/**
	 * Synchronously add a new record to the storage database
	 * 
	 * @param record {@link com.simplegeo.client.types.Record} representing a new Record to be added to storage.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecord(Record record) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(record.getLayer(), "UTF-8"), URLEncoder.encode(record.getRecordId(), "UTF-8")), HttpRequestMethod.PUT, null, record.toJSONString());
	}
	
	/**
	 * Asynchronously add a new record to the storage database
	 * 
	 * @param record {@link com.simplegeo.client.types.Record} representing a new Record to be added to storage.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecord(Record record, SimpleGeoCallback callback) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(record.getLayer(), "UTF-8"), URLEncoder.encode(record.getRecordId(), "UTF-8")), HttpRequestMethod.PUT, null, record.toJSONString(), callback);
	}
	
	
	/**
	 * Synchronously adds multiple records to the storage database
	 * 
	 * @param records ArrayList of {@link com.simplegeo.client.types.Record}s to be added to storage.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecords(ArrayList<Record> records, String layer) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("multipleRecords"), URLEncoder.encode(layer, "UTF-8")), HttpRequestMethod.POST, null, Record.toJSONString(records));
	}

	/**
	 * Asynchronously adds multiple records to the storage database
	 * 
	 * @param records ArrayList of {@link com.simplegeo.client.types.Record}s to be added to storage.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecords(ArrayList<Record> records, String layer, SimpleGeoCallback callback) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("multipleRecords"), URLEncoder.encode(layer, "UTF-8")), HttpRequestMethod.POST, null, Record.toJSONString(records), callback);
	}

	/**
	 * Synchronously get the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @return {@link com.simplegeo.client.types.Record} {@link com.simplegeo.client.types.Record} with the specified recordId and layer
	 * @throws IOException
	 */
	public JSONObject getRecord(String layer, String recordId) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.GET, null, "");
	}

	/**
	 * Asynchronously get the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getRecord(String layer, String recordId, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
	}
	
	/**
	 * Synchronously delete the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @throws IOException
	 */
	public JSONObject deleteRecord(String layer, String recordId) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.DELETE, null, "");
	}

	/**
	 * Asynchronously delete the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deleteRecord(String layer, String recordId, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.DELETE, null, "", callback);
	}
	
	/**
	 * Synchronously get the history of the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as limit and cursor.
	 * @return JSONObject representing the history of record with the specified recordId and layer.
	 * @throws IOException
	 */
	public JSONObject getHistory(String layer, String recordId, HashMap<String, String> extraParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("history"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.GET, extraParams, "");
	}
	
	/**
	 * Asynchronously get the history of the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getHistory(String layer, String recordId,  int limit, String cursor, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("history"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
	}

	
	/**
	 * Synchronously search for nearby records in the specified layer.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param layer String name of the layer
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public JSONObject search(double lat, double lon, String layer, HashMap<String, String> extraParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon), HttpRequestMethod.GET, extraParams, "");
	}
	
	/**
	 * Asynchronously search for nearby records in the specified layer.
	 * 
	 * @param lat Double latitude
	 * @param lon double longitude
	 * @param layer String name of the layer
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(double lat, double lon, String layer, HashMap<String, String> extraParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon), HttpRequestMethod.GET, extraParams, "", callback);
	}

	/**
	 * Synchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public JSONObject searchByIP(String ip, String layer, HashMap<String, String> extraParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, extraParams, "");
	}
	
	/**
	 * Asynchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param extraParams HashMap<String, String> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(String ip, String layer, HashMap<String, String> extraParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, extraParams, "", callback);
	}
	
	/**
	 * Synchronously create a new layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be created
	 * @return JSONObject containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject createLayer(Layer layer) throws IOException, JSONException {
		return this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layer.getName(), "UTF-8")), HttpRequestMethod.PUT, null, Layer.toJSONString(layer));
	}

	/**
	 * Asynchronously create a new layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be created
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException 
	 */
	public void createLayer(Layer layer, SimpleGeoCallback callback) throws IOException, JSONException {
		this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layer.getName(), "UTF-8")), HttpRequestMethod.PUT, null, Layer.toJSONString(layer), callback);
	}

	/**
	 * Synchronously update a layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be updated
	 * @return JSONObject containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject updateLayer(Layer layer) throws IOException, JSONException {
		return createLayer(layer);
	}

	/**
	 * Asynchronously update a layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be updated
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException 
	 */
	public void updateLayer(Layer layer, SimpleGeoCallback callback) throws IOException, JSONException {
		createLayer(layer, callback);
	}

	/**
	 * Synchronously delete a layer.
	 * 
	 * @param layerName String name of the layer to be deleted
	 * @return JSONObject containing the "status"
	 * @throws IOException
	 */
	public JSONObject deleteLayer(String layerName) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8")), HttpRequestMethod.DELETE, null, "");
	}

	/**
	 * Asynchronously delete a layer.
	 * 
	 * @param layerName String name of the layer to be deleted
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deleteLayer(String layerName, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8")), HttpRequestMethod.DELETE, null, "", callback);
	}

	/**
	 * Synchronously retrieve a layer.
	 * 
	 * @param layerName String name of the layer to be retrieved
	 * @return {@link com.simplegeo.client.types.Layer}
	 * @throws IOException
	 */
	public JSONObject getLayer(String layerName) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8")), HttpRequestMethod.GET, null, "");
	}

	/**
	 * Asynchronously retrieve a layer.
	 * 
	 * @param layerName String name of the layer to be retrieved
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getLayer(String layerName, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8")), HttpRequestMethod.GET, null, "", callback);
	}

	/**
	 * Synchronously retrieve all layers.
	 * 
	 * @return {@link com.simplegeo.client.types.LayerCollection}
	 * @throws IOException
	 */
	public JSONObject getLayers() throws IOException {
		return this.execute(this.getEndpoint("allLayers"), HttpRequestMethod.GET, null, "");
	}

	/**
	 * Asynchronously retrieve a layer.
	 * 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getLayers(SimpleGeoCallback callback) throws IOException {
		this.execute(this.getEndpoint("allLayers"), HttpRequestMethod.GET, null, "", callback);
	}
	
	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}
}

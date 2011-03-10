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

import org.json.JSONException;

import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.GeoJSONRecordHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.handler.JSONLayerHandler;
import com.simplegeo.client.http.OAuthClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;
import com.simplegeo.client.types.GeometryCollection;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.LayerCollection;
import com.simplegeo.client.types.Record;

/*
 * Class for interacting with the SimpleGeo Storage API.
 * 
 * @author Pranil Kanderi
 */
public class SimpleGeoStorageClient extends AbstractSimpleGeoClient {
	
	protected static SimpleGeoStorageClient storageClient = null;
	
	/**
	 * Method that ensures we only have one instance of the {@link com.simplegeo.client.SimpleGeoStorageClient} instantiated.  Also allows
	 * server connection variables to be overridden.
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 * @return SimpleGeoStorageClient
	 */
	public static SimpleGeoStorageClient getInstance(String baseUrl, String port, String apiVersion) {
		if(storageClient == null)
			storageClient = new SimpleGeoStorageClient(baseUrl, port, apiVersion);

		return (SimpleGeoStorageClient) storageClient;		
	}
	
	/**
	 * Default method for retrieving a {@link com.simplegeo.client.SimpleGeoStorageClient}.
	 * 
	 * @return SimpleGeoStorageClient
	 */
	public static SimpleGeoStorageClient getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, STORAGE_DEFAULT_VERSION);
	}
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoStorageClient} constructor
	 * 
	 * @param baseUrl String api.simplegeo.com is default, but can be overridden.
	 * @param port String 80 is default, but can be overridden.
	 * @param apiVersion String 1.0 is default, but can be overridden.
	 */
	private SimpleGeoStorageClient(String baseUrl, String port, String apiVersion) {
		super(baseUrl, port, apiVersion);
		
		endpoints.put("singleRecord", "records/%s/%s.json");
		endpoints.put("multipleRecords", "records/%s.json");
		endpoints.put("history", "records/%s/%s/history.json?limit=%s&cursor=%s");
		endpoints.put("searchByLatLon", "records/%s/nearby/%f,%f.json?limit=%s&cursor=%s&radius=%s");
		endpoints.put("searchByIP", "records/%s/nearby/%s.json?limit=%s&cursor=%s");
		endpoints.put("layer", "layers/%s.json");
		endpoints.put("allLayers", "layers.json");
	}
	
	/**
	 * Synchronously add a new record to the storage database
	 * 
	 * @param record {@link com.simplegeo.client.types.Record} representing a new Record to be added to storage.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecord(Record record) throws IOException, JSONException {
		String jsonString = record.toJSONString();
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(record.getLayer(), "UTF-8"), URLEncoder.encode(record.getRecordId(), "UTF-8"));
		this.execute(uri, HttpRequestMethod.PUT, jsonString, new SimpleGeoHandler(new JSONHandler()));
	}
	
	/**
	 * Asynchronously add a new record to the storage database
	 * 
	 * @param record {@link com.simplegeo.client.types.Record} representing a new Record to be added to storage.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecord(Record record, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		String jsonString = record.toJSONString();
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(record.getLayer(), "UTF-8"), URLEncoder.encode(record.getRecordId(), "UTF-8"));
		this.execute(uri, HttpRequestMethod.PUT, jsonString, new SimpleGeoHandler(new JSONHandler()), callback);
	}
	
	
	/**
	 * Synchronously adds multiple records to the storage database
	 * 
	 * @param records ArrayList of {@link com.simplegeo.client.types.Record}s to be added to storage.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecords(ArrayList<Record> records, String layer) throws IOException, JSONException {
		String jsonString = Record.toJSONString(records);
		String uri = String.format(this.getEndpoint("multipleRecords"), URLEncoder.encode(layer, "UTF-8"));
		this.execute(uri, HttpRequestMethod.POST, jsonString, new SimpleGeoHandler(new JSONHandler()));
	}

	/**
	 * Asynchronously adds multiple records to the storage database
	 * 
	 * @param records ArrayList of {@link com.simplegeo.client.types.Record}s to be added to storage.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addOrUpdateRecords(ArrayList<Record> records, String layer, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		String jsonString = Record.toJSONString(records);
		String uri = String.format(this.getEndpoint("multipleRecords"), URLEncoder.encode(layer, "UTF-8"));
		this.execute(uri, HttpRequestMethod.POST, jsonString, new SimpleGeoHandler(new JSONHandler()), callback);
	}

	/**
	 * Synchronously get the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @return {@link com.simplegeo.client.types.Record} {@link com.simplegeo.client.types.Record} with the specified recordId and layer
	 * @throws IOException
	 */
	public Record getRecord(String layer, String recordId) throws IOException {
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"));
		return (Record) this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONRecordHandler()));
	}

	/**
	 * Asynchronously get the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getRecord(String layer, String recordId, SimpleGeoCallback<Feature> callback) throws IOException {
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"));
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONRecordHandler()), callback);
	}
	
	/**
	 * Synchronously delete the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @throws IOException
	 */
	public HashMap<String, Object> deleteRecord(String layer, String recordId) throws IOException {
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"));
		return (HashMap<String, Object>)this.execute(uri, HttpRequestMethod.DELETE, "", new SimpleGeoHandler(new JSONHandler()));
	}

	/**
	 * Asynchronously delete the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deleteRecord(String layer, String recordId, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException {
		String uri = String.format(this.getEndpoint("singleRecord"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"));
		this.execute(uri, HttpRequestMethod.DELETE, "", new SimpleGeoHandler(new JSONHandler()), callback);
	}
	
	/**
	 * Synchronously get the history of the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param limit The maximum number of records to return. Default: 10
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @return {@link com.simplegeo.client.types.GeometryCollection} {@link com.simplegeo.client.types.GeometryCollection} representing the history of 
	 * record with the specified recordId and layer
	 * @throws IOException
	 */
	public GeometryCollection getHistory(String layer, String recordId, int limit, String cursor) throws IOException {
		String uri = String.format(this.getEndpoint("history"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"), (limit > 0 ? limit : ""), (cursor == null ? "" : cursor));
		return (GeometryCollection) this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONRecordHandler()));
	}
	
	/**
	 * Asynchronously get the history of the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param limit The maximum number of records to return. Default: 10
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @return {@link com.simplegeo.client.types.GeometryCollection} {@link com.simplegeo.client.types.GeometryCollection} representing the history of record with the specified recordId and layer
	 * @throws IOException
	 */
	public void getHistory(String layer, String recordId,  int limit, String cursor, SimpleGeoCallback<GeometryCollection> callback) throws IOException {
		String uri = String.format(this.getEndpoint("history"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8"), (limit > 0 ? limit : ""), (cursor == null ? "" : cursor));
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONRecordHandler()), callback);
	}

	
	/**
	 * Synchronously search for nearby records in the specified layer.
	 * 
	 * @param lat double latitude
	 * @param lon double longitude
	 * @param layer String name of the layer
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param limit The maximum number of records to return. Default: 25
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public FeatureCollection search(double lat, double lon, String layer, double radius, int limit, String cursor) throws IOException {
 		String uri = String.format(this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon, (limit > 0 ? limit : ""), (cursor == null ? "" : cursor), (radius > 0 ? radius : ""));
		return (FeatureCollection) this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONHandler()));
	}
	
	/**
	 * Asynchronously search for nearby records in the specified layer.
	 * 
	 * @param lat Double latitude
	 * @param lon double longitude
	 * @param layer String name of the layer
	 * @param radius double A distance in kilometers used to restrict searches
	 * @param limit The maximum number of records to return. Default: 25
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(double lat, double lon, String layer, double radius, int limit, String cursor, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
 		String uri = String.format(this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon, (limit > 0 ? limit : ""), (cursor == null ? "" : cursor), (radius > 0 ? radius : ""));
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONHandler()), callback);
	}

	/**
	 * Synchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param limit The maximum number of records to return. Default: 25
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public FeatureCollection searchByIP(String ip, String layer, int limit, String cursor) throws IOException {
		String uri = String.format(this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8"), (limit > 0 ? limit : ""), (cursor == null ? "" : cursor));
		return (FeatureCollection) this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONHandler()));
	}
	
	/**
	 * Asynchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param limit The maximum number of records to return. Default: 25
	 * @param cursor String encrypted string that is returned when a previous query has reached its prescribed limit and still has more records to return. 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(String ip, String layer, int limit, String cursor, SimpleGeoCallback<FeatureCollection> callback) throws IOException {
		String uri = String.format(this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8"), (limit > 0 ? limit : ""), (cursor == null ? "" : cursor));
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new GeoJSONHandler()), callback);
	}
	
	/**
	 * Synchronously create a new layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be created
	 * @return HashMap<String, Object> containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public HashMap<String, Object> createLayer(Layer layer) throws IOException, JSONException {
		String jsonString = Layer.toJSONString(layer);
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layer.getName(), "UTF-8"));
		return (HashMap<String, Object>) this.execute(uri, HttpRequestMethod.PUT, jsonString, new SimpleGeoHandler(new JSONHandler()));
	}

	/**
	 * Asynchronously create a new layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be created
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 * @throws JSONException 
	 */
	public void createLayer(Layer layer, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		String jsonString = Layer.toJSONString(layer);
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layer.getName(), "UTF-8"));
		this.execute(uri, HttpRequestMethod.PUT, jsonString, new SimpleGeoHandler(new JSONHandler()), callback);
	}

	/**
	 * Synchronously update a layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be updated
	 * @return HashMap<String, Object> containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public HashMap<String, Object> updateLayer(Layer layer) throws IOException, JSONException {
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
	public void updateLayer(Layer layer, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException, JSONException {
		createLayer(layer, callback);
	}

	/**
	 * Synchronously delete a layer.
	 * 
	 * @param layerName String name of the layer to be deleted
	 * @return HashMap<String, Object> containing the "status"
	 * @throws IOException
	 */
	public HashMap<String, Object> deleteLayer(String layerName) throws IOException {
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8"));
		return (HashMap<String, Object>)this.execute(uri, HttpRequestMethod.DELETE , "", new SimpleGeoHandler(new JSONHandler()));
	}

	/**
	 * Asynchronously delete a layer.
	 * 
	 * @param layerName String name of the layer to be deleted
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void deleteLayer(String layerName, SimpleGeoCallback<HashMap<String, Object>> callback) throws IOException {
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8"));
		this.execute(uri, HttpRequestMethod.DELETE, "", new SimpleGeoHandler(new JSONHandler()), callback);
	}

	/**
	 * Synchronously retrieve a layer.
	 * 
	 * @param layerName String name of the layer to be retrieved
	 * @return {@link com.simplegeo.client.types.Layer}
	 * @throws IOException
	 */
	public Layer getLayer(String layerName) throws IOException {
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8"));
		return (Layer)this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new JSONLayerHandler()));
	}

	/**
	 * Asynchronously retrieve a layer.
	 * 
	 * @param layerName String name of the layer to be retrieved
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getLayer(String layerName, SimpleGeoCallback<Layer> callback) throws IOException {
		String uri = String.format(this.getEndpoint("layer"), URLEncoder.encode(layerName, "UTF-8"));
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new JSONLayerHandler()), callback);
	}

	/**
	 * Synchronously retrieve all layers.
	 * 
	 * @return {@link com.simplegeo.client.types.LayerCollection}
	 * @throws IOException
	 */
	public LayerCollection getLayers() throws IOException {
		String uri = this.getEndpoint("allLayers");
		return (LayerCollection)this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new JSONLayerHandler()));
	}

	/**
	 * Asynchronously retrieve a layer.
	 * 
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getLayers(SimpleGeoCallback<LayerCollection> callback) throws IOException {
		String uri = this.getEndpoint("allLayers");
		this.execute(uri, HttpRequestMethod.GET, "", new SimpleGeoHandler(new JSONLayerHandler()), callback);
	}
	
	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}
}

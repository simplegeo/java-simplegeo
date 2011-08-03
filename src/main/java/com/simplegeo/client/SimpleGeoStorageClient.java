package com.simplegeo.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;

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
	
	/**
	 * {@link com.simplegeo.client.SimpleGeoStorageClient} constructor
	 */
	public SimpleGeoStorageClient() {
		super();
		
		endpoints.put("singleRecord", "0.1/records/%s/%s.json");
		endpoints.put("multipleRecords", "0.1/records/%s.json");
		endpoints.put("history", "0.1/records/%s/%s/history.json");
		endpoints.put("searchByLatLon", "0.1/records/%s/nearby/%f,%f.json");
		endpoints.put("searchByIP", "0.1/records/%s/nearby/%s.json");
		endpoints.put("searchByMyIP", "0.1/records/%s/nearby/ip.json");
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
	public String getRecord(String layer, String recordId) throws IOException {
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
	public String deleteRecord(String layer, String recordId) throws IOException {
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
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as limit and cursor.
	 * @return String representing the history of record with the specified recordId and layer.
	 * @throws IOException
	 */
	public String getHistory(String layer, String recordId, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("history"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(recordId, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously get the history of the record that corresponds to the recordId
	 * 
	 * @param layer String name of the layer
	 * @param recordId String id of this record
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as limit and cursor.
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
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @return {@link com.simplegeo.client.types.FeatureCollection} {@link com.simplegeo.client.types.FeatureCollection} containing search results.
	 * @throws IOException
	 */
	public String search(double lat, double lon, String layer, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search for nearby records in the specified layer.
	 * 
	 * @param lat Double latitude
	 * @param lon double longitude
	 * @param layer String name of the layer
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void search(double lat, double lon, String layer, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByLatLon"), URLEncoder.encode(layer, "UTF-8"), lat, lon), HttpRequestMethod.GET, queryParams, "", callback);
	}

	/**
	 * Synchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @return String
	 * @throws IOException
	 */
	public String searchByIP(String ip, String layer, HashMap<String, String[]> queryParams) throws IOException {
		if (ip == null || "".equals(ip)) { return this.searchByMyIP(layer, queryParams); }
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param ip String IP address If blank, your IP address will be used
	 * @param layer String name of the layer
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByIP(String ip, String layer, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		if (ip == null || "".equals(ip)) { 
			this.searchByMyIP(layer, queryParams, callback);
		} else {
			this.execute(String.format(Locale.US, this.getEndpoint("searchByIP"), URLEncoder.encode(layer, "UTF-8"), URLEncoder.encode(ip, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
		}
	}
	
	/**
	 * Synchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param layer String name of the layer
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @return String
	 * @throws IOException
	 */
	public String searchByMyIP(String layer, HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP"), URLEncoder.encode(layer, "UTF-8")), HttpRequestMethod.GET, queryParams, "");
	}
	
	/**
	 * Asynchronously search for records near a given IP address for the specified layer.
	 * 
	 * @param layer String name of the layer
	 * @param queryParams HashMap<String, String[]> Extra parameters to put in the query string such as radius, limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void searchByMyIP(String layer, HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(String.format(Locale.US, this.getEndpoint("searchByMyIP"), URLEncoder.encode(layer, "UTF-8")), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	/**
	 * Synchronously create a new layer.
	 * 
	 * @param layer {@link com.simplegeo.client.types.Layer} to be created
	 * @return String containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public String createLayer(Layer layer) throws IOException, JSONException {
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
	 * @return String containing the "status"
	 * @throws IOException
	 * @throws JSONException 
	 */
	public String updateLayer(Layer layer) throws IOException, JSONException {
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
	 * @return String containing the "status"
	 * @throws IOException
	 */
	public String deleteLayer(String layerName) throws IOException {
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
	public String getLayer(String layerName) throws IOException {
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
	 * @param queryParams HashMap<String, String[]> A HashMap of extra query parameters like limit and cursor.
	 * @return {@link com.simplegeo.client.types.LayerCollection}
	 * @throws IOException
	 */
	public String getLayers(HashMap<String, String[]> queryParams) throws IOException {
		return this.execute(this.getEndpoint("allLayers"), HttpRequestMethod.GET, queryParams, "");
	}

	/**
	 * Asynchronously retrieve a layer.
	 * 
	 * @param queryParams HashMap<String, String[]> A HashMap of extra query parameters like limit and cursor.
	 * @param callback {@link com.simplegeo.client.callbacks.SimpleGeoCallback} Any object implementing the {@link com.simplegeo.client.callbacks.SimpleGeoCallback} interface
	 * @throws IOException
	 */
	public void getLayers(HashMap<String, String[]> queryParams, SimpleGeoCallback callback) throws IOException {
		this.execute(this.getEndpoint("allLayers"), HttpRequestMethod.GET, queryParams, "", callback);
	}
	
	@Override
	public OAuthClient getHttpClient() {
		return super.getHttpClient();
	}
}

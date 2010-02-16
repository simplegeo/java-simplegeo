/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.encoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
//import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;

/**
 * Encodes and decodes {@link org.json.JSONObject} into {@link IRecord}s.
 * 
 * @author Derek Smith
 */
public class GeoJSONEncoder {

	private static Logger logger = Logger.getLogger(GeoJSONEncoder.class);
	private static final String TAG = GeoJSONEncoder.class.getName();

	/**
	 * Converts a {@link org.json.JSONObject} into a 
	 * {@link com.simplegeo.client.model.DefaultRecord}. If
	 * a parse exception is thrown, the return value will be null.
	 * 
	 * @param jsonObject the {@link org.json.JSONObject} to convert
	 * @return a newly constructed {@link com.simplegeo.client.model.DefaultRecord}
	 */
	public static DefaultRecord getRecord(JSONObject jsonObject) {
		
		DefaultRecord defaultRecord = null;
		try {

			if (jsonObject == null)
				return null;

			GeoJSONObject geoJSONObject = new GeoJSONObject("Feature",
					jsonObject.toString());

			JSONObject properties = new JSONObject(geoJSONObject
					.getProperties().toString());
			defaultRecord = new DefaultRecord(geoJSONObject.optString("id"),
					geoJSONObject.optString("layer"), geoJSONObject
							.getProperties().optString("type"), geoJSONObject
							.getLongitude(), geoJSONObject.getLatitude());

			defaultRecord.setProperties(properties);

			// Don't create an exception as these are optional keys
			defaultRecord.setCreated(geoJSONObject.optLong("created"));
			defaultRecord.setExpiration(geoJSONObject.optLong("expires"));

		} catch (JSONException e) {

			logger.debug(String.format("%s", e.getLocalizedMessage()));

		}

		return defaultRecord;
	}

	/**
	 * Converts a {@link com.simplegeo.client.model.GeoJSONObject} into an Array of 
	 * {@link com.simplegeo.client.model.DefaultRecord}. If
	 * a parse exception is thrown, the return value will be null. An array 
	 * is returned instead of a single object due to the fact that a single 
	 * GeoJSON object has the capacity to maintain a collection. 
	 * 
	 * @param geoJSONObject the {@link com.simplegeo.client.model.GeoJSONObject} to convert
	 * @return a newly constructed list of {@link com.simplegeo.client.model.DefaultRecord}
	 */
	public static List<DefaultRecord> getRecords(GeoJSONObject geoJSONObject) {

		List<DefaultRecord> defaultRecords = null;

		try {

			if (geoJSONObject.isFeatureCollection()) {

				defaultRecords = new ArrayList<DefaultRecord>();
				JSONArray features = geoJSONObject.getFeatures();
				int size = features.length();
				for (int index = 0; index < size; index++) {

					JSONObject feature = features.getJSONObject(index);
					DefaultRecord record = GeoJSONEncoder.getRecord(feature);
					if (record != null)
						defaultRecords.add(record);

				}

			} else if (geoJSONObject.getType().equals("Feature")) {

				defaultRecords = new ArrayList<DefaultRecord>();
				DefaultRecord record = GeoJSONEncoder.getRecord(geoJSONObject);
				if (record != null)
					defaultRecords.add(record);
			}

		} catch (JSONException e) {

			logger.debug(String.format("%s for %s", e.getLocalizedMessage(),
					geoJSONObject.toString()));
		}

		return defaultRecords;
	}

	/**
	 * Converts a {@link com.simplegeo.client.model.IRecord} into a 
	 * {@link com.simplegeo.client.model.GeoJSONObject}. If
	 * a parse exception is thrown, the return value will be null.
	 * 
	 * @param record the {@link com.simplegeo.client.model.IRecord} to convert
	 * @return a newly constructed {@link com.simplegeo.client.model.GeoJSONObject}
	 */
	public static GeoJSONRecord getGeoJSONRecord(IRecord record) {

		if (record == null)
			return null;

		GeoJSONRecord geoJSONRecord = null;
		try {
			
			geoJSONRecord = new GeoJSONRecord(record.getRecordId(), record.getLayer(), record.getObjectType());

			JSONObject properties = record.getProperties();
			if (properties != null)
				properties = new JSONObject(properties.toString());
			else
				properties = new JSONObject();
			
			geoJSONRecord.setLatitude(record.getLatitude());
			geoJSONRecord.setLongitude(record.getLongitude());

			geoJSONRecord.setProperties(properties);

			geoJSONRecord.setCreated(record.getCreated());
			geoJSONRecord.setExpiration(record.getExpiration());

		} catch (JSONException e) {

			logger.debug(TAG+  String.format("%s for %s", e.getLocalizedMessage(),
					record.toString()));

		}

		return geoJSONRecord;
	}

	/**
	 * @see GeoJSONEncoder#getGeoJSONRecord(com.simplegeo.client.model.IRecord)
	 */
	public static GeoJSONObject getGeoJSONRecord(List<IRecord> records) {

		if (records == null || records.isEmpty())
			return null;

		GeoJSONObject geoJSONObject = null;
		try {

			geoJSONObject = new GeoJSONObject("FeatureCollection");

			JSONArray features = new JSONArray();
			for (IRecord record : records) {

				GeoJSONObject jsonObject = GeoJSONEncoder
						.getGeoJSONRecord(record);

				if (jsonObject != null)
					features.put(jsonObject);
			}

			geoJSONObject.put("features", geoJSONObject);

		} catch (JSONException e) {

			logger.debug(TAG + e.getLocalizedMessage());
		}

		return geoJSONObject;
	}

}

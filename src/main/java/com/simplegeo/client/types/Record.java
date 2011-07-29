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
package com.simplegeo.client.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Representation of a record as exposed by the SimpleGeo Storage API.
 * 
 * @author Pranil Kanderi
 */
public class Record extends Feature {

	private String layer;
	private long created;
	private long expiration;

	public Record() {

	}

	public Record(String recordId, String layer, String type, double longitude,
			double latitude) {
		super(recordId, new Geometry(new Point(latitude, longitude)), type,
				new HashMap<String, Object>());

		this.layer = layer;
		this.created = (long) (System.currentTimeMillis() / 1000);
		this.expiration = 0;
	}

	/**
	 * @return the id associated with the record
	 */
	public String getRecordId() {
		return this.simpleGeoId;
	}

	/**
	 * @param recordId
	 *            the id associated with the record
	 */
	public void setRecordId(String recordId) {
		this.simpleGeoId = recordId;
	}

	/**
	 * @return the layer where the record is held
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer where the record is held
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @return the time at which this record was created in milliseconds
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the time at which this record was created in milliseconds
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @return the time at which this record will expire in milliseconds
	 */
	public long getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration
	 *            the time at which this record will expire in milliseconds
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = super.toJSON();
		jsonObject.put("layer", layer);
		jsonObject.put("created", created);
		jsonObject.put("expiration", expiration);

		return jsonObject;
	}

	public static Record fromJSONString(String jsonString) throws JSONException {
		return fromJSON(new JSONObject(jsonString));
	}

	public static Record fromJSON(JSONObject json) throws JSONException {
		Record record = new Record();
		Feature feature = Feature.fromJSON(json);

		record.setSimpleGeoId(feature.getSimpleGeoId());
		record.setGeometry(feature.getGeometry());
		record.setType(feature.getType());
		record.setProperties(feature.getProperties());

		record.setLayer((String)feature.getProperties().get("layer"));
		record.setCreated(json.optLong("created"));

		return record;
	}

	public static JSONObject toJSON(ArrayList<Record> records)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "FeatureCollection");
		
		JSONArray jsonArray = new JSONArray();
		for (Record record : records) {
			jsonArray.put(record.toJSON());
		}
		jsonObject.put("features", jsonArray);

		return jsonObject;
	}

	public static String toJSONString(ArrayList<Record> records)
			throws JSONException {
		return toJSON(records).toString();
	}
	
	public String toString() {
		
		return String.format(Locale.US, "<Record id=%s, layer=%s, created=%d>", 
				getRecordId(), getLayer(), getCreated());
	}


}

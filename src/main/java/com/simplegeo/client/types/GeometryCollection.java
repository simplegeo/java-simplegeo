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

package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Collection of {@link com.simplegeo.client.types.Geometry} objects
 * 
 * @author Pranil Kanderi
 *
 */
public class GeometryCollection {

	private ArrayList<Geometry> geometries;
	private String cursor;
	
	public GeometryCollection() {
		
	}
	
	public GeometryCollection(ArrayList<Geometry> geometries) {
		this.setGeometries(geometries);
	}

	/**
	 * @param geometries the geometries to set
	 */
	public void setGeometries(ArrayList<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * @return the geometries
	 */
	public ArrayList<Geometry> getGeometries() {
		return geometries;
	}
	
	/**
	 * @param cursor the cursor to set
	 */
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	
	/**
	 * @return the cursor
	 */
	public String getCursor() {
		return cursor;
	}	
	
	public static Object fromJSONString(String response) throws JSONException {
		JSONObject jsonObject = new JSONObject(response);
		
		JSONArray jsonArray = jsonObject.getJSONArray("geometries");
		
		GeometryCollection geometryCollection = new GeometryCollection();
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			
			Geometry geometry = new Geometry();
			JSONObject jsonGeometry = jsonArray.getJSONObject(i);
			
			if ("Point".equals(jsonGeometry.getString("type"))) {
				JSONArray coordinates = jsonGeometry.getJSONArray("coordinates");
				Point point = new Point(coordinates.getDouble(1), coordinates.getDouble(0));
				geometry = new Geometry(point);
			} else if ("Polygon".equals(jsonGeometry.getString("type"))) {
				JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
				Polygon polygon = Polygon.fromJSONArray(polygonArray);
				geometry = new Geometry(polygon);
			} else {
				JSONArray polygonArray = jsonGeometry.getJSONArray("coordinates");
				MultiPolygon multiPolygon = MultiPolygon.fromJSONArray(polygonArray);
				geometry = new Geometry(multiPolygon);
			}
			
			geometry.setCreated(jsonGeometry.getLong("created"));			
			geometries.add(geometry);
		}
		geometryCollection.setCursor(new JSONObject(response).optString("cursor"));
		geometryCollection.setGeometries(geometries);
		
		return geometryCollection;
	}

}

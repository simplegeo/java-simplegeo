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
package com.simplegeo.client.service.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplegeo.client.service.exceptions.ValidLayerException;
import com.simplegeo.client.utilities.SimpleGeoUtilities;

/**
 * An abstracted class that defines the some of the common parameters
 * between a Geohash and LatLon nearby query.
 * 
 * @author Derek Smith
 */
public abstract class NearbyQuery implements IQuery {
	
	private String cursor;
	private String layer;
	private List<String> types;
	private int limit;

	/**
	 * @param layer @see com.simplegeo.client.service.query.NearbyQuery#getLayer()
	 * @param types @see com.simplegeo.client.service.query.NearbyQuery#getTypes()
	 * @param limit @see com.simplegeo.client.service.query.NearbyQuery#getLimit()
	 * @param cursor @see com.simplegeo.client.service.query.IQuery#getCursor()
	 * @throws ValidLayerException
	 */
	public NearbyQuery(String layer, List<String> types, int limit, String cursor) throws ValidLayerException {
		this.types = types;
		this.cursor = cursor;
		this.limit = limit;
		
		if (layer == null || layer.equals(""))
			throw new ValidLayerException("");

		this.layer = layer;
	}
	
	/**
	 * @see com.simplegeo.client.service.query.IQuery#getParams()
	 */
	public Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		if(limit > 0)
			params.put("limit", Integer.toString(limit));
		
		if(types != null && !types.isEmpty())			
			params.put("types", SimpleGeoUtilities.commaSeparatedString(types));
		
		if(cursor != null)
			params.put("cursor", cursor);

		return params;
	}
	
	/**
	 * @see com.simplegeo.client.service.query.IQuery#getUri()
	 */
	public String getUri() {
		return String.format("/records/%s/nearby", layer);
	}
	
	/**
	 * @see com.simplegeo.client.service.query.IQuery#getCursor()
	 */
	public String getCursor() {
		return cursor;
	}

	/**
	 * @see com.simplegeo.client.service.query.IQuery#setCursor()
	 */
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	/**
	 * @return the layer
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to search in
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * If this value is null, ALL types will be searched.
	 * 
	 * @return the types to look for
	 */
	public List<String> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<String> types) {
		this.types = types;
	}

	/**
	 * The default limit is 25.
	 * 
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}

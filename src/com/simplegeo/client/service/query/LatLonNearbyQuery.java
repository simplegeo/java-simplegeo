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

import java.util.List;
import java.util.Map;

import com.simplegeo.client.service.exceptions.ValidLayerException;

/**
 * A nearby query that uses latitude, longitude and radius as its search
 * parameters. 
 * 
 * @see com.simplegeo.client.service.query.NearbyQuery
 * 
 * @author Derek Smith
 */
public class LatLonNearbyQuery extends NearbyQuery {
	
	private double lat;
	private double lon;
	private double radius;

	/**
	 * @param lat the latitude
	 * @param lon the longitude
	 * @param radius the radius of the search in km
	 * @param @see com.simplegeo.client.service.query.NearbyQuery#getLayer()
	 * @throws ValidLayerException
	 */
	public LatLonNearbyQuery(double lat, double lon, double radius, String layer) throws ValidLayerException {
		this(lat, lon, radius, layer, null, -1, null);
	}
	
	/**
	 * @param lat the latitude
	 * @param lon the longitude
	 * @param radius the radius of the search in km
	 * @param @see com.simplegeo.client.service.query.NearbyQuery#getLayer()
	 * @param @see com.simplegeo.client.service.query.NearbyQuery#getTypes()
	 * @param @see com.simplegeo.client.service.query.NearbyQuery#getLimit()
	 * @param @see com.simplegeo.client.service.query.NearbyQuery#getCursor()
	 * @throws ValidLayerException
	 */
	public LatLonNearbyQuery(double lat, double lon, double radius, String layer, List<String> types, int limit,
			String cursor) throws ValidLayerException {
		super(layer, types, limit, cursor);
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
	}
	
	public String getUri() {
		return String.format("%s/%f,%f.json", super.getUri(), lat, lon);
	}

	public Map<String, String> getParams() {
		Map<String, String> params = super.getParams();
		params.put("radius", String.valueOf(radius));
		return params;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
}

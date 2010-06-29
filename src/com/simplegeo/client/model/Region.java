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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Region {

	private Envelope envelope;
	private String name;
	private String type;
	private String id;
	
	public static List<Region> getRegions(JSONArray jsonArray) {
		List<Region> regions = new ArrayList<Region>();
		for(int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject region = jsonArray.getJSONObject(i);
				regions.add(Region.getRegion(region));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return regions;
	}
	
	public static Region getRegion(JSONObject jsonObject) throws JSONException {
		Region region = null;
		if(jsonObject != null) {
			region = new Region(jsonObject.getString("name"),
					jsonObject.getString("type"),
					jsonObject.getString("id"),
					Envelope.fromJSON(jsonObject.getJSONArray("bounds")));
		}
		
		return region;
	}
	
	public static List<Region> difference(List<Region> regionSetOne, List<Region> regionSetTwo) {		
		List<Region> difference = new ArrayList<Region>();
		if(regionSetOne == null || regionSetOne.isEmpty())
			return difference;
		
		if(regionSetTwo == null || regionSetTwo.isEmpty())
			return regionSetOne;
			
		for(Region regionOne : regionSetOne) {
			if(!regionOne.contained(regionSetTwo))
				difference.add(regionOne);
		}
			
		return difference;
	}
	
	public Region(String name, String type, String id, Envelope envelope) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.envelope = envelope;
	}
	
	/**
	 * @return the envelope
	 */
	public Envelope getEnvelope() {
		return envelope;
	}

	/**
	 * @param envelope the envelope to set
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return String.format("<Region: %s, %s>", this.id, this.type);
	}
	
	public boolean contained(List<Region> regions) {
		if(regions == null || regions.isEmpty())
			return false;
		
		for(Region region : regions)
			if(equals(region))
				return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object region) {
		if(region instanceof Region) {
			return ((Region)region).getId().equals(this.id);
		}
		
		return false;
	}
}

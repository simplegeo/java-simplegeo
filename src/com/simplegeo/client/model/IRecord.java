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

import org.json.JSONObject;

/**
 * A simple interface that defines the proper required state
 * of a record in SimpleGeo.
 * 
 * @author Derek Smith
 */
public interface IRecord {

	/**
	 * @return the id associated with the record
	 */
	public String getRecordId();
	
	/**
	 * @return the layer where the record is held
	 */
	public String getLayer();
	
	/**
	 * @return the type associated with the record
	 * @see com.simplegeo.client.model.RecordType
	 */
	public String getObjectType();
	
	/**
	 * @return the latitude for the record
	 */
	public double getLatitude();
	
	/**
	 * @return the longitude for the record
	 */
	public double getLongitude();
	
	/**
	 * @return the time at which this record was created in milliseconds
	 */
	public long getCreated();
	
	/**
	 * @return the time at which this record will expire in milliseconds
	 */
	public long getExpiration();
	
	/**
	 * @return other values associated with this record
	 */
	public JSONObject getProperties();

}

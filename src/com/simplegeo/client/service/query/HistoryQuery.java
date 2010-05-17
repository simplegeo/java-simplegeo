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
import java.util.Map;

/**
 * Contains information to make a query to the history endpoint.
 * 
 * @author Derek Smith
 */
public class HistoryQuery implements IQuery {

	private String cursor;
	private String layer;
	private String recordId;
	private int limit;
	
	/**
	 * @param recordId @see com.simplegeo.client.service.query.HistoryQuery#getRecordId()
	 * @param layer @see com.simplegeo.client.service.query.HistoryQuery#getLayer()
	 * @param limit @see com.simplegeo.client.service.query.HistoryQuery#getLimit()
	 * @param cursor @see com.simplegeo.client.service.query.IQuery#getCursor()
	 */
	public HistoryQuery(String recordId, String layer, int limit, String cursor) {
		this.recordId = recordId;
		this.layer = layer;
		this.cursor = cursor;
		this.limit = limit;
	}
	
	/**
	 * @param recordId @see com.simplegeo.client.service.query.HistoryQuery#getRecordId()
	 * @param layer @see com.simplegeo.client.service.query.HistoryQuery#getLayer()
	 * @param layer
	 */
	public HistoryQuery(String recordId, String layer, int limit) {
		this(recordId, layer, limit, null);
	}
	
	/**
	 * @see com.simplegeo.client.service.query.IQuery#getCursor()
	 */
	public String getCursor() {
		return cursor;
	}

	/**
	 * @see com.simplegeo.client.service.query.IQuery#getParams()
	 */
	public Map<String, String> getParams() {
		Map<String, String> params =  new HashMap<String, String>();
		if(cursor != null)
			params.put("cursor", cursor);
		
		if(limit > 0)
			params.put("limit", Integer.toString(limit));

		return params;
	}

	/**
	 * @see com.simplegeo.client.service.query.IQuery#getUri()
	 */
	public String getUri() {
		return String.format("/records/%s/%s/history.json", this.layer, this.recordId);
	}

	/**
	 * @return the layer
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @return the recordId
	 */
	public String getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/**
	 * @see com.simplegeo.client.service.query.IQuery#setCursor(java.lang.String)
	 */
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	
	/**
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

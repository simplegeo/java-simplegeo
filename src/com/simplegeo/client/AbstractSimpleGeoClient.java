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

package com.simplegeo.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplegeo.client.ISimpleGeoClient.Handler;
import com.simplegeo.client.handler.GeoJSONHandler;
import com.simplegeo.client.handler.JSONHandler;
import com.simplegeo.client.handler.ISimpleGeoJSONHandler;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.http.exceptions.APIException;
import com.simplegeo.client.http.exceptions.UnsupportedHandlerException;
import com.simplegeo.client.query.NearbyQuery;

/**
 * Extracts as much common code as possible between the SimpleGeoClient and the SimpleGeoURLConnClient.
 * 
 * @author Casey Crites
 */

public abstract class AbstractSimpleGeoClient implements ISimpleGeoClient {
	
	public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
	
	protected static Logger logger = Logger.getLogger(AbstractSimpleGeoClient.class.getName());
	
	protected static final String mainURL = "http://api.simplegeo.com/0.1";
	
	protected static ISimpleGeoClient sharedLocationService = null;
	
	protected GeoJSONHandler geoJSONHandler = null;
	protected JSONHandler jsonHandler = null;
	
	/**
	 * Tells the service whether to make the Http call on the same thread.  Note: if the underlying
	 * client doesn't handle future tasks, this flag will be ignored.
	 */
	protected boolean futureTask = false; 
		
	protected AbstractSimpleGeoClient() {
		
		setHandler(Handler.JSON, new JSONHandler());
		setHandler(Handler.GEOJSON, new GeoJSONHandler());
		
	}

	/**
	 * Set the handler per type.
	 * 
	 * This is useful if subclasses or implementations of the model
	 * objects change and the response object needs to be built 
	 * differently.
	 * 
	 * @param type the Handler type to override
	 * @param handler the handler must implement ISimpleGeoJSONHandler.
	 */
	public void setHandler(Handler type, ISimpleGeoJSONHandler handler) {
		
		switch(type) {
			case GEOJSON:
				geoJSONHandler = (GeoJSONHandler)handler;
				break;
			case JSON:
				jsonHandler = (JSONHandler)handler;
				break;
			default:
				break;
		}
	}
	
	/**
	 * @param type the type of handler to retrieved defined by 
	 * {@link com.simplegeo.client.Handler}
	 * @return the instance of {@link com.simplegeo.client.handler.ISimpleGeoJSONHandler}
	 * that is associated with the type
	 */
	public ISimpleGeoJSONHandler getHandler(Handler type) {
		
		ISimpleGeoJSONHandler handler = null;
		switch(type) {
			
			case GEOJSON:
				handler = geoJSONHandler;
				break;
			case JSON:
				handler = jsonHandler;
				break;
			default:
				break;
				
		}
		
		return handler;
	}
	
	private String buildUrl(String url, Map<String, ?> parameters) {
		
		if(parameters == null)
			return url;
		
		StringBuilder sb = new StringBuilder(url);
		boolean first = true;
		for (Entry<String, ?> entry : parameters.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(first ? "?" : "&");
				try {
					String key = URLEncoder.encode(entry.getKey(), DEFAULT_CONTENT_CHARSET);
					String value = URLEncoder.encode(entry.getValue().toString(), DEFAULT_CONTENT_CHARSET);
					sb.append(key).append("=").append(value);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				first = false;
			}
		}
		return sb.toString();
	}

	private ISimpleGeoJSONHandler getHandler(Object record) {
		// TODO Fix this
		return geoJSONHandler;

	}
	
	protected void dealWithAuthorizationException(Exception e) throws APIException {
		
		e.printStackTrace();
		throw new APIException(SimpleGeoHandler.NOT_AUTHORIZED, e.getMessage());
	}
	
	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#supportsFutureTasks()
	 */
	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#supportsFutureTasks()
	 */
	@Override
	public boolean supportsFutureTasks() {
		// 
		// By default, future tasks aren't supported.
		//
		return false;
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#setFutureTask(boolean)
	 */
	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#setFutureTask(boolean)
	 */
	@Override
	public void setFutureTask(boolean futureTask) {
		this.futureTask = futureTask;		
	}

	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#getFutureTask()
	 */
	/* (non-Javadoc)
	 * @see com.simplegeo.client.ISimpleGeoClient#getFutureTask()
	 */
	@Override
	public boolean getFutureTask() {
		return futureTask;	
	}
	
	private String getURI(String string) {
		return mainURL + string;
	}

	protected abstract Object executeGet(String uri, ISimpleGeoJSONHandler handler) throws IOException;
	
	protected abstract Object executePost(String uri, String jsonPayload, ISimpleGeoJSONHandler handler) throws IOException;

	protected abstract Object executeDelete(String uri, ISimpleGeoJSONHandler handler) throws IOException;

}

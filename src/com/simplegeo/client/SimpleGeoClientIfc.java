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
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.SimpleGeoClient.Handler;
import com.simplegeo.client.geojson.GeoJSONObject;
import com.simplegeo.client.http.OAuthHttpClient;
import com.simplegeo.client.http.SimpleGeoHandler;
import com.simplegeo.client.model.Envelope;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.query.HistoryQuery;
import com.simplegeo.client.query.NearbyQuery;

/**
 * Defines a parent interface for both SimpleGeoClient and SimpleGeoURLConnClient
 * 
 * Both interface with the SimpleGeo API. The difference is that SimpleGeoClient
 * uses the Apache HTTPClient and SimpleGeoURLConnClient uses a java.net URLConnection.
 * 
 * Requests can be created and set on the same thread or they can be
 * used in a non-blocking fashion (in the case of the SimpleGeoClient).
 * The default behavior is to send the
 * request on the same thread the call was made. By setting 
 * {@link com.simplegeo.client.SimpleGeoClient#futureTask} to true,
 * requests will be built and sent on a thread chosen by
 * {@link com.simplegeo.client.concurrent.RequestThreadPoolExcecutor} and
 * a {@link java.util.concurrent.FutureTask} will be returned instead of
 * the return object that is build from the ResponseHandler.
 * 
 * In order to properly authenticate requests, an OAuth token is required.
 * This property is set by calling 
 * {@link com.simplegeo.client.OAuthClientIfc#setToken(String, String)}.
 * 
 * @author Derek Smith (refactored by Mark Fogle)
 */

public interface SimpleGeoClientIfc {
	
	/**
	 * Retrieves information from SimpleGeo about the 
	 * {@link com.simplegeo.client.model.IRecord}. The record
	 * must already exist in SimpleGeo in order for the request to be
	 * successful. 
	 * 
	 * @param defaultRecord
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object retrieve(IRecord defaultRecord) throws ClientProtocolException, IOException;	
					
	/**
	 * Retrieves information from SimpleGeo about all of the 
	 * {@link com.simplegeo.client.model.IRecord}s within the list. At least
	 * one record must already exist in SimpleGeo in order for the request to
	 * be successful.
	 * 
	 * @param records
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object retrieve(List<IRecord> records) throws ClientProtocolException, IOException;
	
	/**
	 * Updates the {@link com.simplegeo.client.model.IRecord} in SimpleGeo, creating the
	 * record if necessary.
	 * 
	 * @param record the record to update
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object update(IRecord record) throws ClientProtocolException, IOException;
	
	/**
	 * Updates the list of {@link com.simplegeo.client.model.IRecord} in SimpleGeo, creating
	 * them if necessary.
	 * 
	 * @param records the records to update
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object update(List<IRecord> records) throws ClientProtocolException, IOException;

	/**
	 * Uses the {@link com.simplegeo.client.geojson.GeoJSONObject} to update records 
	 * in SimpleGeo, creating the record if necessary.
	 * 
	 * @param geoJSONObject the geojson object to send in the request
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object update(GeoJSONObject geoJSONObject) throws ClientProtocolException, IOException;
	
	/**
	 * Uses the {@link com.simplegeo.client.geojson.GeoJSONObject} and layer name
	 * to update records in SimpleGeo, creating the record if necessary.
	 * 
	 * @param layer the layer to add the records to
	 * @param geoJSONObject the GeoJSON object that contains a FeatureCollection
	 * @param handler the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object update(String layer, GeoJSONObject geoJSONObject, SimpleGeoHandler handler) 
	throws ClientProtocolException, IOException;
	
	/**
	 * Deletes the {@link com.simplegeo.client.model.IRecord} from SimpleGeo.
	 * The record must already exist in SimpleGeo in order for the request to be successful. 
	 * 
	 * @param record the record to delete
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object delete(IRecord record) throws ClientProtocolException, IOException;
	
	/**
	 * Deletes the record form SimpleGeo. The record must already exist in SimpleGeo in
	 * order for the request to be successful.
	 * 
	 * @param layer the name of the layer that holds the record.
	 * @param recordId the id of the record.
	 * @param handler the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object delete(String layer, String recordId, SimpleGeoHandler handler) 
		throws ClientProtocolException, IOException;
	
	/**
	 * Return a reverse chronological list of where a record has been over time.
	 * Currently only returns the last 10 places a record has been. The response
	 * is a GeoJSON GeometryCollection containing a list of Point objects,
	 * each with a created field containing the timestamp. 
	 * 
	 * This query supports pagination which means "next_cursor" will be set as a top-level key/value
	 * pair if there are more data points to offer after the initial query limit has been reached.
	 * You can use the value at "next_cursor" to get the remaining results of the query.
	 * @see com.simpelgeo.com.client.service.query.IQuery#setCursor(String).
	 * 
	 * @param query the history query object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object history(HistoryQuery query) throws ClientProtocolException, IOException;

	/**
	 * Return a reverse chronological list of where a record has been over time.
	 * Currently only returns the last 10 places a record has been. The response
	 * is a GeoJSON GeometryCollection containing a list of Point objects,
	 * each with a created field containing the timestamp. 
	 * 
	 * This query supports pagination which means "next_cursor" will be set as a top-level key/value
	 * pair if there are more data points to offer after the initial query limit has been reached.
	 * You can use the value at "next_cursor" to get the remaining results of the query.
	 * @see com.simpelgeo.com.client.service.query.IQuery#setCursor(String).
	 * 
	 * @param query the history query object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object history(HistoryQuery query, Handler type) 
		throws ClientProtocolException, IOException;
	
	/**
	 * Sends a nearby request to SimpleGeo. 
	 * 
	 * This query supports pagination which means "next_cursor" will be set as a top-level key/value
	 * pair if there are more data points to offer after the initial query limit has been reached.
	 * You can use the value at "next_cursor" to get the remaining results of the query.
	 * @see com.simpelgeo.com.client.service.query.IQuery#setCursor(String).
	 * 
	 * @param query the nearby query
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object nearby(NearbyQuery query) throws ClientProtocolException, IOException;
	
	/**
	 * Sends a nearby request to SimpleGeo.
	 * 
	 * This query supports pagination which means "next_cursor" will be set as a top-level key/value
	 * pair if there are more data points to offer after the initial query limit has been reached.
	 * You can use the value at "next_cursor" to get the remaining results of the query.
	 * @see com.simpelgeo.com.client.service.query.IQuery#setCursor(String).
	 * 
	 * @param type the handler responsible for creating the return object 
	 * @param query the nearby query
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object nearby(NearbyQuery query, Handler type) throws ClientProtocolException, IOException;
		
	/**
	 * Reverse geocodes a lat/lon pair.
	 * 
	 * @param lat the latitude
	 * @param lon the longitude
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object reverseGeocode(double lat, double lon)
			throws ClientProtocolException, IOException;
	
	/**
	 * 
	 * @param day any day from {@link java.util.Calendar#DAY_OF_WEEK} in the DAY_OF_WEEK section
	 * @param hour an hour between 0 and 23, or something outside that range to query the whole day
	 * @param lat the latitude
	 * @param lon the longitude
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object density(int day, int hour, double lat, double lon)
	throws ClientProtocolException, IOException;
	
	/**
	 * 
	 * @param day any day from {@link java.util.Calendar#DAY_OF_WEEK} in the DAY_OF_WEEK section
	 * @param hour an hour between 0 and 23, or something outside that range to query the whole day
	 * @param lat the latitude
	 * @param lon the longitude
	 * @param type the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object density(int day, int hour, double lat, double lon, Handler type)
			throws ClientProtocolException, IOException;
	
	/**
	 * Does a "pushpin" query through a series of polygon layers and identifies the "cone" of
	 * administrative and other boundaries in which the point lies.
	 *  
	 * Returns a list of elements where each element contains these key fields:
	 * <ul>
	 * <li>id: A string that uniquely identifies the feature in the SimpleGeo gazetteer. This ID can 
	 * be used to query the exact shape of the polygon itself via the `boundary` API call.</li>
	 * <li>name: A well-known name English-language name for the entity. This may change in the future.</li>
	 * <li>abbr: If present, an official abbreviation for the entity, e.g. an ISO code, postal 
	 * code, or similar.</li>
	 * <li>bounds: A list containing the (west, south, east, north) bounds of the polygon, in that order.</li>
	 * <li>type: A string describing the type of feature identified. May or may not be one of:
	 *   <ul>
	 *   <li>Country: A national boundary.</li>
     *   <li>Province: A state or province.</li>
	 *   <li>County: A county, parish, or similar sub-provincial administrative entity. US only at present.</li>
	 *   <li>City: An incorporated city, town, village, hamlet, etc. US only at present.</li>
	 *   <li>Urban Area: An approximate metropolitan region, from the 1:10M NaturalEarth cultural dataset. 
	 *   May be useful for places outside the US where no incorporated place boundary is (yet) available.</li>
	 *   <li>Neighborhood: A usually approximate boundary for a (usually informal) sub-division of a city. 
	 *   US only at present.</li>
	 *   <li>Postal Code: A postal delivery region. In the US, a ZIP code. US only at present.</li>
	 *   <li>Legislative District: In the US, a congressional district for the currently sitting US Congress.</li>
	 *   <li>Census Tract: As defined by the US Census Bureau in the most recently published census (2000).</li> 
	 *   <li>Other values are possible, so this list will be updated from time to time.</li>
	 *   </ul>
	 * </li>
	 * </ul>
	 * @param lat the latitude
	 * @param lon the longitude
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object contains(double lat, double lon) throws ClientProtocolException, IOException;
	
	/**
	 * Does a "pushpin" query through a series of polygon layers and identifies the "cone" of
	 * administrative and other boundaries in which the point lies.
	 *  
	 * Returns a list of elements where each element contains these key fields:
	 * <ul>
	 * <li>id: A string that uniquely identifies the feature in the SimpleGeo gazetteer. This ID can 
	 * be used to query the exact shape of the polygon itself via the `boundary` API call.</li>
	 * <li>name: A well-known name English-language name for the entity. This may change in the future.</li>
	 * <li>abbr: If present, an official abbreviation for the entity, e.g. an ISO code, postal 
	 * code, or similar.</li>
	 * <li>bounds: A list containing the (west, south, east, north) bounds of the polygon, in that order.</li>
	 * <li>type: A string describing the type of feature identified. May or may not be one of:
	 *   <ul>
	 *   <li>Country: A national boundary.</li>
     *   <li>Province: A state or province.</li>
	 *   <li>County: A county, parish, or similar sub-provincial administrative entity. US only at present.</li>
	 *   <li>City: An incorporated city, town, village, hamlet, etc. US only at present.</li>
	 *   <li>Urban Area: An approximate metropolitan region, from the 1:10M NaturalEarth cultural dataset. 
	 *   May be useful for places outside the US where no incorporated place boundary is (yet) available.</li>
	 *   <li>Neighborhood: A usually approximate boundary for a (usually informal) sub-division of a city. 
	 *   US only at present.</li>
	 *   <li>Postal Code: A postal delivery region. In the US, a ZIP code. US only at present.</li>
	 *   <li>Legislative District: In the US, a congressional district for the currently sitting US Congress.</li>
	 *   <li>Census Tract: As defined by the US Census Bureau in the most recently published census (2000).</li> 
	 *   <li>Other values are possible, so this list will be updated from time to time.</li>
	 *   </ul>
	 * </li>
	 * </ul>
	 * @param lat the latitude
	 * @param lon the longitude
	 * @param type the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object contains(double lat, double lon, Handler type)
	throws ClientProtocolException, IOException;
	
	/**
	 * Returns a feature object from the SimpleGeo gazetteer, {@link com.simplegeo.client.SimpleGeoClient#contains}, 
	 * along with the geometry of the feature in GeoJSON format in the geometry field.
	 * 
	 * @param featureId A string that uniquely identifies the feature in the SimpleGeo gazetteer.
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object boundaries(String featureId)
	throws ClientProtocolException, IOException;
	
	/**
	 * Returns a feature object from the SimpleGeo gazetteer, {@link com.simplegeo.client.SimpleGeoClient#contains}, 
	 * along with the geometry of the feature in GeoJSON format in the geometry field.
	 * 
	 * @param featureId A string that uniquely identifies the feature in the SimpleGeo gazetteer.
	 * @param type the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object boundaries(String featureId, Handler type)
	throws ClientProtocolException, IOException;
	
	/**
	 * Queries a series of polygon layers and identifies the "cone" of administrative and
	 * other boundaries that overlap with the given bounding box.
	 * 
	 * Specifying a limit is not a strict count of how many features will be returned, but 
	 * rather the maximum amount that will be returned. The maximum 
	 * number of features returned is 1000.  The results are not paginated, so if you need more than
	 * 1000 results, consider breaking down your bounding box into multiple queries.
	 * 
	 * @param envelope the bounding box area
	 * @param limit the amount of features to return. 
	 * @param featureType the feature type to filter by
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object overlaps(Envelope envelope, int limit, String featureType) 
	throws ClientProtocolException, IOException;
	
	/**
	 * Queries a series of polygon layers and identifies the "cone" of administrative and
	 * other boundaries that overlap with the given bounding box.
	 * 
	 * Specifying a limit is not a strict count of how many features will be returned, but 
	 * rather the maximum amount that will be returned. The maximum 
	 * number of features returned is 1000.  The results are not paginated, so if you need more than
	 * 1000 results, consider breaking down your bounding box into multiple queries.
	 * 
	 * @param envelope the bounding box area
	 * @param limit the amount of features to return. 
	 * @param featureType the feature type to filter by
	 * @param type the handler responsible for creating the return object
	 * @return if {@link com.simplegeo.client.SimpleGeoClient#futureTask} is false (or the client 
	 * doesn't support asynchronous execution), then the return value will be the result of the response 
	 * based on the handler used. Otherwise, the return value will be a {@link java.util.concurrent.FutureTask}.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @see <a href="http://help.simplegeo.com/faqs/api-documentation/endpoints"</a>
	 */
	public Object overlaps(Envelope envelope, int limit, String featureType, Handler type)
	throws ClientProtocolException, IOException;
	
	/**
	 * @return the Http client used to execute all requests
	 */
	public OAuthHttpClient getHttpClient();
	
}
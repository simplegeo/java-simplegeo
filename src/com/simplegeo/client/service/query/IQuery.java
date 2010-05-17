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

import java.util.Map;

/**
 * Defines the necessary methods in order for a query to be sent to
 * to the SimpleGeo API. Some of the SimpleGeo endpoints support
 * pagination (e.g. nearby and history). This interface helps define
 * query objects that support pagination.
 * 
 * @author Derek Smith
 */
public interface IQuery {

	/**
	 * @return the URI associated with this query
	 */
	public String getUri();
	
	/**
	 * @return the HTTP params that are associated
	 * with this query
	 */
	public Map<String, String> getParams();
	
	/**
	 * The cursor is an encrypted string that is returned when
	 * a previous query has reached its prescribed limit and still
	 * has more records to return. 
	 * 
	 * @return the cursor
	 */
	public String getCursor();

	/**
	 * The cursor is generated from the SimpleGeo servers. It is an encrypted
	 * String that is returned from certain endpoints (e.g. history and nearby). This
	 * value allows the next, incoming query to return the next sequence of results.  
	 * 
	 * @param cursor the cursor to set
	 */
	public void setCursor(String cursor);

}

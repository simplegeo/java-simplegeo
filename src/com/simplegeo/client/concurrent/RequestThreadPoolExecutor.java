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

package com.simplegeo.client.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple subclass of {@link java.util.concurrent.ThreadPoolExecutor} that is used
 * mainly by {@link com.simplegeo.client.service.LocationService} to do threaded HttpRequests.
 * 
 * @author Derek Smith
 */
public class RequestThreadPoolExecutor extends ThreadPoolExecutor {
	
	/**
	 * A default constructor that builds the object using
	 * {@link NamedThreadFactory}
	 *  
	 * @param name name of the {@link com.simplegeo.client.concurrent.NamedThreadFactory}
	 */
	public RequestThreadPoolExecutor(String name) {
		
		this(1, 8, Integer.MAX_VALUE, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory(name));
		
	}
	
	/**
	 * Constructs a new {@link java.util.concurrent.ThreadPoolExecutor}.
	 * 
	 * @param corePoolSize the minimum size of the thread pool
	 * @param maximumPoolSize the maximum size of the thread pool
	 * @param keepAliveTime the amount of time to keep the thread alive
	 * @param unit time unit to associate with keepAliveTime parameter
	 * @param workQueue @see java.util.concurrent.BlockingQueue
	 * @param threadFactory @see java.util.concurrent.ThreadFactory
	 */
	public RequestThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		
	}


}

package com.simplegeo.client.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple subclass of {@link java.util.concurrent.ThreadPoolExecutor} that is used
 * mainly by {@link com.simplegeo.client.SimpleGeoClient} to do threaded HttpRequests.
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

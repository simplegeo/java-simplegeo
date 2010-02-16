/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * A simple subclass of {@link java.util.concurrent.ThreadFactory} that
 * is used by {@link com.simplegeo.client.concurrent.RequestThreadPoolExecutor}.
 * 
 * @author Derek Smith
 */
public class NamedThreadFactory implements ThreadFactory
{
    protected final String id;    
    protected final AtomicInteger n = new AtomicInteger(1);

    /**
     * Creates a new ThreadFactory with a value that can be used
     * to retrieve itself later.
     * 
     * @param id The name of the the thread factory;
     */
    public NamedThreadFactory(String id)
    {
    	super();
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    public Thread newThread(Runnable runnable)
    {        
        String name = id + ":" + n.getAndIncrement();
        return new Thread(runnable, name);
    }
}

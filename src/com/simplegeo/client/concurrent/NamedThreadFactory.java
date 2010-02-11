/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class NamedThreadFactory implements ThreadFactory
{
    protected final String id;
    protected final AtomicInteger n = new AtomicInteger(1);

    public NamedThreadFactory(String id)
    {
        this.id = id;
    }

    public Thread newThread(Runnable runnable)
    {        
        String name = id + ":" + n.getAndIncrement();
        return new Thread(runnable, name);
    }
}

package com.simplegeo.client.types;

import org.junit.Assert;

public class PointTest {

	public void testNewPoint() {
		Point point = new Point(37.0, -105.0);
		Assert.assertEquals(37.0, point.getLat(), 0d);
		Assert.assertEquals(-105.0, point.getLon(), 0d);
	}
	
}

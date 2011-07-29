package com.simplegeo.client.types;

import junit.framework.TestCase;

public class PointTest extends TestCase {

	public void testNewPoint() {
		Point point = new Point(37.0, -105.0);
		TestCase.assertEquals(37.0, point.getLat());
		TestCase.assertEquals(-105.0, point.getLon());
	}
	
}

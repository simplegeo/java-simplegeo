package com.simplegeo.client.types;

import java.util.List;

import org.json.JSONObject;

public class Geometry {
	
	private Point point;
	private Polygon polygon;
	
	public Geometry() {
		
	}
	
	public Geometry(Point point) {
		this.point = point;
	}
	
	public Geometry(Polygon polygon) {
		this.polygon = polygon;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

}
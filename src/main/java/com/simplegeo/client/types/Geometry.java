package com.simplegeo.client.types;

public class Geometry {
	
	private Point point;
	private Polygon polygon;
	private MultiPolygon multiPolygon;
	private long created;
	
	public Geometry() {
		
	}
	
	public Geometry(Point point) {
		this.point = point;
	}
	
	public Geometry(Polygon polygon) {
		this.polygon = polygon;
	}
	
	public Geometry(MultiPolygon multiPolygon) {
		this.multiPolygon = multiPolygon;
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

	public MultiPolygon getMultiPolygon() {
		return multiPolygon;
	}

	public void setMultiPolygon(MultiPolygon multiPolygon) {
		this.multiPolygon = multiPolygon;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getCreated() {
		return created;
	}

}
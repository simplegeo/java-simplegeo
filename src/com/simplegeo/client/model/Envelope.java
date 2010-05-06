package com.simplegeo.client.model;

/**
 * An object that represents a bounding box for a given area
 * by the latitude and longitude lines that surround it.
 * 
 * @author dsmith
 */
public class Envelope {
	
	private double south;
	private double west;
	private double north;
	private double east;

	/**
	 * Creates a new Envelope object with the given latitude and longitude
	 * lines.
	 * 
	 * @param south the south latitude line of the bounding box
	 * @param west the west longitude line of the bounding box
	 * @param north the north latitude line of the bounding box
	 * @param east the east longitude line of the bounding box
	 */
	public Envelope(double south, double west, double north, double east) {
		
		this.south = south;
		this.west = west;
		this.north = north;
		this.east = east;
	}
	
	/**
	 * @return the south
	 */
	public double getSouth() {
		return south;
	}

	/**
	 * @param south the south to set
	 */
	public void setSouth(double south) {
		this.south = south;
	}

	/**
	 * @return the west
	 */
	public double getWest() {
		return west;
	}

	/**
	 * @param west the west to set
	 */
	public void setWest(double west) {
		this.west = west;
	}

	/**
	 * @return the north
	 */
	public double getNorth() {
		return north;
	}

	/**
	 * @param north the north to set
	 */
	public void setNorth(double north) {
		this.north = north;
	}

	/**
	 * @return the east
	 */
	public double getEast() {
		return east;
	}

	/**
	 * @param east the east to set
	 */
	public void setEast(double east) {
		this.east = east;
	}

	public String toString() {
		
		return String.format("%f,%f,%f,%f", south, west, north, east);
		
	}
}

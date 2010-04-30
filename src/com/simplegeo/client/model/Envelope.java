package com.simplegeo.client.model;

/**
 * An object that represents a bounding box for a given area
 * by the latitude and longitude lines that surround it.
 * 
 * @author dsmith
 */
public class Envelope {
	
	private String south;
	private String west;
	private String north;
	private String east;

	/**
	 * Creates a new Envelope object with the given latitude and longitude
	 * lines.
	 * 
	 * @param south the south latitude line of the bounding box
	 * @param west the west longitude line of the bounding box
	 * @param north the north latitude line of the bounding box
	 * @param east the east longitude line of the bounding box
	 */
	public Envelope(String south, String west, String north, String east) {
		
		this.south = south;
		this.west = west;
		this.north = north;
		this.east = east;
	}
	
	/**
	 * @return the south
	 */
	public String getSouth() {
		return south;
	}

	/**
	 * @param south the south to set
	 */
	public void setSouth(String south) {
		this.south = south;
	}

	/**
	 * @return the west
	 */
	public String getWest() {
		return west;
	}

	/**
	 * @param west the west to set
	 */
	public void setWest(String west) {
		this.west = west;
	}

	/**
	 * @return the north
	 */
	public String getNorth() {
		return north;
	}

	/**
	 * @param north the north to set
	 */
	public void setNorth(String north) {
		this.north = north;
	}

	/**
	 * @return the east
	 */
	public String getEast() {
		return east;
	}

	/**
	 * @param east the east to set
	 */
	public void setEast(String east) {
		this.east = east;
	}

	public String toString() {
		
		return String.format("%s,%s,%s,%s", south, west, north, east);
		
	}
}

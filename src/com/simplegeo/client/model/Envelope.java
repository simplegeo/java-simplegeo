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
package com.simplegeo.client.model;

/**
 * An object that represents a bounding box for a given area
 * by the latitude and longitude lines that surround it.
 * 
 * @author Derek Smith
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

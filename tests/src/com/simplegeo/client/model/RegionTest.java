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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class RegionTest extends TestCase {

	public void testDifference() {
		List<Region> regionSetOne = new ArrayList<Region>();
		List<Region> regionSetTwo = new ArrayList<Region>();
		List<Region> differenceRegion = new ArrayList<Region>();
		
		List<Region> difference = Region.difference(regionSetOne, regionSetTwo);
		assertRegionsEqual(difference, differenceRegion);
		
		regionSetOne.clear(); regionSetTwo.clear(); differenceRegion.clear();
		
		addRegion(regionSetOne, 1);
		addRegion(differenceRegion, 1);
		difference = Region.difference(regionSetOne, regionSetTwo);
		assertRegionsEqual(difference, differenceRegion);

		differenceRegion.clear();
		difference = Region.difference(regionSetTwo, regionSetOne);
		assertRegionsEqual(difference, differenceRegion);
		
		difference = Region.difference(regionSetOne, regionSetOne);
		assertRegionsEqual(difference, differenceRegion);
		
		regionSetOne.clear(); regionSetTwo.clear(); differenceRegion.clear();
		addRegions(regionSetOne, 5);
		addRegions(regionSetTwo, 4);
		addRegion(differenceRegion, 4);
		difference = Region.difference(regionSetOne, regionSetTwo);
		assertRegionsEqual(difference, differenceRegion); 
	}
	
	private void addRegions(List<Region> regionSet, int amount) {
		for(int i = 0; i < amount; i++)
			addRegion(regionSet, i);
	}
	
	private void addRegion(List<Region> regionSet, int value) {
		String v = String.valueOf(value);
		regionSet.add(new Region(v, v, v, null));	
	}
	
    private void assertRegionsEqual(List<Region> regionSetOne, List<Region> regionSetTwo) {
    	if(regionSetOne == null && regionSetTwo == null)
    		assertEquals(regionSetOne, regionSetTwo);
    	else {
    		assertNotNull(regionSetOne);
    		assertNotNull(regionSetTwo);
    		assertEquals(regionSetOne.size(), regionSetTwo.size());
    		
    		for(int i = 0; i < regionSetOne.size(); i++)
    			assertTrue(regionSetOne.get(i).getId().equals(regionSetTwo.get(i).getId()));
    			
    	}
    }
}

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

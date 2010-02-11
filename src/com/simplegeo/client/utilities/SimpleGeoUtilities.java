/**
 * Copyright 2010 SimpleGeo. All rights reserved.
 */
package com.simplegeo.client.utilities;

import java.util.List;

/**
 * @author dsmith
 *
 */
public class SimpleGeoUtilities {

	static public String commaSeparatedString(List<String> strings) {
		
		String mainString = "";
		for(String string : strings)
			mainString += "," + string;
		
		mainString.replaceFirst(",", "");
		
		return mainString;
		
	}
}

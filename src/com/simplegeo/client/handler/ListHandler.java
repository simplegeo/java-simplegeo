package com.simplegeo.client.handler;

import java.util.logging.Logger;

import org.json.JSONException;

import com.simplegeo.client.types.CategoryCollection;;

/**
 * A response handler used for creating {@link com.simplegeo.client.types.CategoryCollection} from a given payload
 * 
 * @author Pranil Kanderi
 *
 */
public class ListHandler implements SimpleGeoResponseHandler{

	private static Logger logger = Logger.getLogger(ListHandler.class.getName());

	public Object parseResponse(String response) {
		Object returnObject = new Object();
		
		if (response.contains("category")) {
			try {
				returnObject = CategoryCollection.fromJSONString(response);
			} catch (JSONException e){
				logger.info(e.getMessage());
			}
		} 

		
		return returnObject;
	}

}

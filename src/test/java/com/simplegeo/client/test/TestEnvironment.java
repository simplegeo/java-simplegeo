package com.simplegeo.client.test;

public abstract class TestEnvironment {

	private static String ACCESS_KEY = "e8tFTe6uzstnJmNWNxamQaJTwkEz4FNj";
	private static String SECRET_KEY = "MdfLgasKYJHgf7CX7Fqdg9em9HCadJ4G";
	private static boolean PAID_ACCOUNT = true;

    private static final String JSON_POINT_STRING = "{\"geometry\": { \"type\": \"Point\",\"coordinates\": [-122.937467,47.046962]},\"type\": \"Feature\",\"id\": \"SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830\",\"properties\": {\"city\": \"Olympia\",\"name\": \"Burger Master West Olympia\",\"country\": \"us\",\"phone\": \"3603575451\",\"owner\": \"simplegeo\",\"state\": \"WA\",\"address\": \"2820 Harrison Ave NW\",\"postcode\": \"98502\"}}";
    private static final String JSON_POINT_STRING_NO_ID = "{\"geometry\": { \"type\": \"Point\",\"coordinates\": [-122.937467,47.046962]},\"type\": \"Feature\",\"properties\": {\"city\": \"Olympia\",\"name\": \"Burger Mistress West Olympia\",\"country\": \"us\",\"phone\": \"3603575451\",\"owner\": \"simplegeo\",\"state\": \"WA\",\"address\": \"2820 Harrison Ave NW\",\"postcode\": \"98502\"}}";
    private static final String JSON_POINT_BAD_STRING = "{\"geometry\": { \"type\": \"Point\",\"coordinates\": [-122.937467,47.046962]},\"type\": \"Feature\",\"id\": \"SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830\",\"properties\": {\"city\": \"Gildford\",\"name\": \"Burger Master West Olympia\",\"tags\": [\"eating\"],\"country\": \"us\",\"phone\": \"3603575451\",\"owner\": \"simplegeo\",\"state\": \"WA\",\"address\": \"2820 Harrison Ave NW\",\"categories\": [[\"Food & Drink\",\"Restaurants\",\"\"]],\"postcode\": \"98502\"}}";
    private static final String JSON_POLYGON_STRING = "{\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.444406,37.759271],[-122.444291,37.759487],[-122.444127,37.759709],[-122.443135,37.760357],[-122.442843,37.760444],[-122.441282,37.76053],[-122.440682,37.76043],[-122.439282,37.76133],[-122.438082,37.76163],[-122.436982,37.76183],[-122.435882,37.76193],[-122.434982,37.762429],[-122.432982,37.764229],[-122.430982,37.765729],[-122.430682,37.765929],[-122.430382,37.766029],[-122.428882,37.767429],[-122.428482,37.766129],[-122.428382,37.764629],[-122.428328,37.763683],[-122.428277,37.762825],[-122.428182,37.762029],[-122.428082,37.761229],[-122.428082,37.76053],[-122.427882,37.75963],[-122.427882,37.75893],[-122.427782,37.75803],[-122.425582,37.75823],[-122.423082,37.75833],[-122.422344,37.758422],[-122.422282,37.75843],[-122.421382,37.75853],[-122.421282,37.75773],[-122.421282,37.75693],[-122.422186,37.756874],[-122.422991,37.756814],[-122.423615,37.756757],[-122.424182,37.75673],[-122.42479,37.756688],[-122.425482,37.75663],[-122.426682,37.75653],[-122.427782,37.75653],[-122.429882,37.75643],[-122.431582,37.75633],[-122.432102,37.756266],[-122.434386,37.756119],[-122.435374,37.756034],[-122.436516,37.755973],[-122.437597,37.755899],[-122.43752,37.75546],[-122.438682,37.75533],[-122.439682,37.75523],[-122.440282,37.75513],[-122.439882,37.75633],[-122.440219,37.756934],[-122.441403,37.75808],[-122.441961,37.758277],[-122.442982,37.75883],[-122.443482,37.75903],[-122.444406,37.759271]]]},\"type\":\"Feature\",\"properties\":{\"category\":\"Neighborhood\",\"license\":\"http://creativecommons.org/publicdomain/mark/1.0/\",\"handle\":\"SG_0Bw22I6fWoxnZ4GDc8YlXd_37.759737_-122.433203\",\"subcategory\":\"\",\"name\":\"Castro District\",\"type\":\"Boundary\",\"abbr\":\"\"},\"id\":\"SG_0Bw22I6fWoxnZ4GDc8YlXd\"}";
    private static final String JSON_MULTIPOLYGON_STRING = "{\"geometry\": { \"type\": \"MultiPolygon\",\"coordinates\": [[[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]], [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],[[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]]},\"type\": \"Feature\",\"id\": \"SG_4CsrE4oNy1gl8hCLdwu0F0_47.046962_-122.937467@1290636830\",\"properties\": {\"city\": \"Olympia\",\"name\": \"Burger Master West Olympia\",\"tags\": [\"eating\"],\"country\": \"us\",\"phone\": \"3603575451\",\"owner\": \"simplegeo\",\"state\": \"WA\",\"address\": \"2820 Harrison Ave NW\",\"categories\": [[\"Food & Drink\",\"Restaurants\",\"\"]],\"postcode\": \"98502\"}}";
    
    public static String getKey() throws Exception {
	    if(ACCESS_KEY.equals("consumerKey")) {
	    	if (System.getenv("OAUTH_KEY") != null && !"".equals(System.getenv("OAUTH_KEY"))) {
	    		ACCESS_KEY = System.getenv("OAUTH_KEY");
	    	} else {
			    throw new Exception("Please replace ACCESS_KEY with a valid String or set the OAUTH_KEY environment var.");
	    	}
	    }
	    return ACCESS_KEY;
    }

    public static String getSecret() throws Exception {
	    if(SECRET_KEY.equals("consumerSecret")) {
	    	if (System.getenv("OAUTH_SECRET") != null && !"".equals(System.getenv("OAUTH_SECRET"))) {
	    		SECRET_KEY = System.getenv("OAUTH_SECRET");
	    	} else {
			    throw new Exception("Please replace SECRET_KEY with a valid String or set the OAUTH_SECRET environment var.");
	    	}
	    }
	    return SECRET_KEY;    	   
   	}
    
    public static boolean isPaidAccount() {
    	if (System.getenv("PAID_ACCOUNT") != null && !"".equals(System.getenv("PAID_ACCOUNT"))) {
    		PAID_ACCOUNT = "true".equals(System.getenv("PAID_ACCOUNT")) ? true : false;
    	}
		return PAID_ACCOUNT;
	}
    
	public static String getJsonPointString() {
		return JSON_POINT_STRING;
	}
    
	public static String getJsonPointStringNoId() {
		return JSON_POINT_STRING_NO_ID;
	}

	public static String getJsonPointBadString() {
		return JSON_POINT_BAD_STRING;
	}

	public static String getJsonPolygonString() {
		return JSON_POLYGON_STRING;
	}

	public static String getJsonMultipolygonString() {
		return JSON_MULTIPOLYGON_STRING;
	}

}
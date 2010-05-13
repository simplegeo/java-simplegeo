package com.simplegeo.client.test;

public class TestEnvironment {

       private static final String TESTING_LAYER =  "com.testing.my";
       
       private static final String ACCESS_KEY = "my-key";
       private static final String SECRET_KEY = "my-secret";
       
       public static String getKey() throws Exception {
    	   if(ACCESS_KEY.equals("my-key"))
    		   throw new Exception("Please replace ACCESS_KEY with a valid String");
    	   
    	   return ACCESS_KEY;
       }
       
       public static String getSecret() throws Exception {
    	   
    	   if(SECRET_KEY.equals("my-secret"))
    		   throw new Exception("Please replace SECRET_KEY with a valid String");
    	   
    	   return SECRET_KEY;    	   
       }
       
       public static String getLayer() throws Exception {
    	   
    	   if(TESTING_LAYER.equals("com.testing.my"))
    		   throw new Exception("Please replace TESTING_LAYER with a valid layer");
    	   
    	   return TESTING_LAYER;
       }

}


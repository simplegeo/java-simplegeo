# java-simplegeo

A thread-safe, Java HttpClient used to interface with the SimpleGeo Places and Context API. Both
synchronous and asynchronous calls are provided. 

## Disclaimer

Version 3.0 of this client is non-backwards compatible.  Older versions of the client are still available by selecting a 1.x or 2.x tag from the drop down.

## Changes from 2.0

* Optional parameters are now truly optional.  Instead of setting things to null or empty strings, optional parameters are to be specified in a HashMap<String, String[]>
* Individual clients are no longer singletons.  The use case didn't seem to fit everyone's needs, so we're leaving it up to you from now on whether you want that structure.
* All return types are now Strings.  These Strings are actually JSON and can be converted directly to JSONObject or JSONArray.  Also, most data structures contain static methods that will convert the String to that structure for you.
* Handlers have gone away.  Previously we allowed specific handlers to be specified to format returned data for you.  Now if you want to do this, simply use the appropriate fromJSONString method of the data structure you want with the resulting json String.
* Querying by multiple categories, filtering by multiple sections in context, etc. is now fully supported.  To query by multiple categories, simply populate the String array in your queryParams HashMap as so.
    HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
    queryParams.put("category", new String[] {"restaurant", "bar"});
* Demographics searching for Context is also fully supported.  Simply create a query parameter with type demographics.acs\_\_table and add as many table names to the String[].
* The entire project has been converted to Maven in order to help automate builds and documentation.

## Maven

## Adding to a Java/Android project

### Eclipse

Right click (ctrl + click) on your project -> Build Path -> Configure Build Path -> Select the Libraries tab -> Add JARs (if the SimpleGeo jar is in your workspace)/Add External JARs (if the SimpleGeo jar is on your filesystem) -> Navigate to the jar you want to add and click OK.  The jar has been added to your project and you can start using it by getting an instance and setting your OAuth Token like so:

    $ SimpleGeoPlacesClient placesClient = new SimpleGeoPlacesClient();
    $ placesClient.getHttpClient().setToken("oauth-key", "oauth-secret");

## Documents

The docs are generated using `javadoc` and are updated as often as possible in the `gh-pages` branch of this repository.  You can view them [here](http://simplegeo.github.com/java-simplegeo/2.0/index)

### Simple Example

    import java.io.IOException;
    import java.util.ArrayList;

    import org.json.JSONException;

    import com.simplegeo.client.SimpleGeoPlacesClient;
    import com.simplegeo.client.types.Feature;
    import com.simplegeo.client.types.FeatureCollection;
    import com.simplegeo.client.types.Point;

    public class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello World");
            SimpleGeoPlacesClient placesClient = new SimpleGeoPlacesClient();
            placesClient.getHttpClient().setToken("2Z7Jkrx49kp8DUwqcqmSAWRGRLyQ5Yhe", "nkdzubf2KXH2qGjkEwf3hFdnawj69yCa");
            Point bensHouse = new Point(37.800426, -122.439516);
            try {
                HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
                queryParams.put("category", new String[] {"sushi"});
                JSONObject sushiJSON = placesClient.search(bensHouse, queryParams);
                FeatureCollection sushiFeatureCollection = FeatureCollection.fromJSON(sushiJSON);
                ArrayList<Feature> sushiFeatures = sushiFeatureCollection.getFeatures();
                for (Feature feature: sushiFeatures) {
                    System.out.println(feature.getProperties().get("name"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

### Copyright (C) 2011 SimpleGeo Inc. All rights reserved.

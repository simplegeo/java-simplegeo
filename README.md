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

If you're developing a Java project with Maven, adding the SimpleGeo jar to your project is super simple.  Just add this snippet to to your pom.xml

    <dependency>
        <groupId>com.simplegeo</groupId>
        <artifactId>java-simplegeo</artifactId>
        <version>3.0</version>
    </dependency>

## Adding to a Java/Android project without Maven

### Eclipse

1. Right click (ctrl + click) on your project
2. Build Path
3. Configure Build Path
4. Select the Libraries tab
5. Add JARs (if the SimpleGeo jar is in your workspace)/Add External JARs (if the SimpleGeo jar is on your filesystem)
6. Navigate to the jar you want to add and click OK.  The jar has been added to your project and you can start using it by getting an instance and setting your OAuth Token like so:

    $ SimpleGeoPlacesClient placesClient = new SimpleGeoPlacesClient();
    $ placesClient.getHttpClient().setToken("oauth-key", "oauth-secret");

## Documentation

The docs are generated using `javadoc` and are updated as often as we build the client in our public Jenkins environment.  You can view them [here](https://ci.public.simplegeo.com/job/java-simplegeo/)

### Simple Example

    import java.io.IOException;
    import java.util.ArrayList;

    import org.json.JSONException;

    import com.simplegeo.client.SimpleGeoPlacesClient;
    import com.simplegeo.client.types.Feature;
    import com.simplegeo.client.types.FeatureCollection;

    public class HelloWorld {
        public static void main(String[] args) {
            SimpleGeoPlacesClient placesClient = new SimpleGeoPlacesClient();
            placesClient.getHttpClient().setToken("your_token", "your_secret");
            HashMap<String, String[]> queryParams = new HashMap<String, String[]>();
            queryParams.put("category", new String[] {"sushi"});
            try {
                String sushiString = placesClient.search(37.800426, -122.439516, queryParams);
                FeatureCollection sushiFeatureCollection = FeatureCollection.fromJSONString(sushiString);
                ArrayList<Feature> sushiFeatures = sushiFeatureCollection.getFeatures();
                for (Feature feature: sushiFeatures) {
                    System.out.println(feature.getProperties().get("name"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

### Copyright (C) 2011 SimpleGeo Inc. All rights reserved.

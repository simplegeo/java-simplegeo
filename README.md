# java-simplegeo

A thread-safe, Java HttpClient used to interface with the SimpleGeo Places and Context API. Both
synchronous and asynchronous calls are provided. 

## Disclaimer

Version 3.0 of this client is non-backwards compatible.  Older versions of the client are still available by selecting a 1.x or 2.x tag from the drop down.

## Google App Engine

If you need to run this client on Google App Engine, you can use the dev/feature/gae branch for now.  This will be merged into master when a clean way of doing this is determined.

## Getting Started

For network tests to succeed, you'll want to clone and start the mock SimpleGeo
server:

    $ git submodule update --init
    $ ruby -rubygems server/server.rb

If it worked, it should say something like:

    == Sinatra/1.1.0 has taken the stage on 4567 for development with backup from Mongrel

If it failed, install the dependencies and try again:

    $ sudo gem install oauth json sinatra

To actually run the tests in Eclipse, navigate to the `tests/src` folder, right click, Run As, JUnit Tests.

## Building

There are four sets of jar ant tasks included in `build.xml`.  There are 2 jar tasks for each of the Places client, Context client Storage client and all.  One task is for building the clients without any of the supporting libs and the other includes the libs.  They are:

* jar-with-libs (includes all clients and supporting libs)
* jar-without-libs (includes all clients but no supporting libs)
* places-jar-with-libs (includes places client and supporting libs)
* places-jar-without-libs (includes places client but no supporting libs)
* context-jar-with-libs (includes context client and supporting libs)
* context-jar-without-libs (includes context client but no supporting libs)
* storage-jar-with-libs (includes storage client and supporting libs)
* storage-jar-without-libs (includes storage client but no supporting libs)

After running the ant task you require, your jar will be located in the `bin/` folder.

    $ ant build jar-with-libs
    $ ls bin
    ./  ../  classes/  java-simplegeo-2.2.1.jar

## Adding to a Java/Android project

### Eclipse

Right click (ctrl + click) on your project -> Build Path -> Configure Build Path -> Select the Libraries tab -> Add JARs (if the SimpleGeo jar is in your workspace)/Add External JARs (if the SimpleGeo jar is on your filesystem) -> Navigate to the jar you want to add and click OK.  The jar has been added to your project and you can start using it by getting an instance and setting your OAuth Token like so:

    $ SimpleGeoPlacesClient placesClient = SimpleGeoPlacesClient.getInstance();
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
            SimpleGeoPlacesClient placesClient = SimpleGeoPlacesClient.getInstance();
            placesClient.getHttpClient().setToken("2Z7Jkrx49kp8DUwqcqmSAWRGRLyQ5Yhe", "nkdzubf2KXH2qGjkEwf3hFdnawj69yCa");
            Point bensHouse = new Point(37.800426, -122.439516);
            FeatureCollection sushiFeatureCollection;
            try {
                sushiFeatureCollection = placesClient.search(bensHouse, "sushi", "", 10);
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

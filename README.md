# java-simplegeo

A thread-safe, Java HttpClient used to interface with the SimpleGeo Places and Context API. Both
synchronous and asynchronous calls are provided. 

## Getting Started

For network tests to succeed, you'll want to clone and start the mock SimpleGeo
server:

    $ git submodule update --init
    $ ruby -rubygems server/server.rb

If it worked, it should say something like:

    == Sinatra/1.1.0 has taken the stage on 4567 for development with backup from Mongrel

If it failed, install the dependencies and try again:

    $ sudo gem install oauth json sinatra

To actually run the tests, navigate to the tests/src folder and run the SimpleGeoPlacesClientTest, SimpleGeoContextClientTest and FeatureTests as JUnits.

## Building

There are three jar ant tasks included in build.xml.  places-jar, context-jar and jar will generate jars that package just the SimpleGeoPlacesClient, just the SimpleGeoContextClient and both, respectively.

### Copyright (C) 2010 SimpleGeo Inc. All rights reserved.

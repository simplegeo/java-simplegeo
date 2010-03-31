SGClient v0.1.1
================================================================================

ABSTRACT:
--------------------------------------------------------------------------------

A thread-safe, Java HttpClient used to interface with the SimpleGeo API. Both
synchronous and asynchronous calls are provided. The project is also capable of 
transforming into an Android project.

USAGE:
--------------------------------------------------------------------------------

The initial state of the project uses normal JRE system libraries. To transform 
the project into a more suitable environment for Android development, the 
_build.xml_ comes with a target called **to_android**. Running `ant to-android`
will move everything from the _.android_ into the the top-level
directory. The target will also search and replace certain string values in 
the source files (swapping log4j with Log, changing import packages). Once
the conversion process is done, running `ant to-java` will revert back to the
initial state.

Running `ant dist` will generate jars and JavaDocs.

Currently, there is only one example located in _examples_. There will be more
to come. The unit tests in _tests_ should also provide as examples. In order for
unit tests to be successful, you must replace three static fields in
_tests/com/simplegeo/client/test/TestEnvironment.java_: TESTING_LAYER, ACCESS_KEY 
and SECRETY_KEY.

CHANGES FROM PREVIOUS VERSIONS:
--------------------------------------------------------------------------------
Version 1.0.0
- Nearby requests only accept a single layer instead of multiple.

Version 0.1.1
- Added license

### Copyright (C) 2010 SimpleGeo Inc. All rights reserved.

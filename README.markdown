SGClient v0.1.0
================================================================================

ABSTRACT:
--------------------------------------------------------------------------------

A Java HttpClient used to interface with the SimpleGeo API. The project is capable
of transforming into an Android project as well.

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

CHANGES FROM PREVIOUS VERSIONS:
--------------------------------------------------------------------------------
TBA

Copyright (C) 2010 SimpleGeo Inc. All rights reserved.
--------------------------------------------------------------------------------
## MusicBrainz Android

This is the official [MusicBrainz](http://www.musicbrainz.org) Android app. It started as a Google Summer of Code project in 2010. Here is the [MusicBrainz product page](http://musicbrainz.org/doc/MusicBrainz_for_Android) for the app.

### Download

Get the latest release of the app from [Google Play](https://play.google.com/store/apps/details?id=org.musicbrainz.mobile).

## Licenses

Copyright © 2012 Jamie McDonald. Licensed under the GNU General Public License Version 3.
ZXing barcode scanner integration code and miscellaneous helper classes are licensed under the Apache License, Version 2 (see headers).

## Contributing

Please report issues on GitHub and submit feature requests on the [MusicBrainz issue tracker](http://tickets.musicbrainz.org/).

Code contributions are welcomed using [pull requests](https://help.github.com/articles/using-pull-requests). Please use the following code formatting and style guidelines:

* Tabs are 4 spaces in Java code and 2 spaces in XML
* 120 character line width
* Use descriptive names and comment as a last resort

A code formatting configuration file is included in the repo.

### Building with Maven

You need [this profile](https://github.com/novoda/public-mvn-repo/blob/master/poms/settings.xml) in your ~/.m2/settings.xml.

The following command will setup the project and copy the necessary dependencies so that you can work in Eclipse.

    $mvn clean initialize -Peclipse
  
You can also do something like `mvn install`, which will build, install and run the tests (you'll need to attach an Android device for that).

### Eclipse project setup

Create an Android project from the 'app' directory and a plain Java project from 'api' directory. Add the API project to the app project build path and set it as exported.

There are two Android library projects that need to be included and specified in the Android project properties.

* [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock)
* [ViewPagerIndicator](https://github.com/JakeWharton/Android-ViewPagerIndicator)
  
Clone these from the GitHub repos above, import them to your Eclipse workspace and point the app project to them in Properties > Android. Make sure that the Android support library is only included in one of these projects and the others depend on it.

### Can I skip all this Maven stuff?

Yes. However, you will need to look at the dependencies in the POMs and go hunting for the jars yourself.
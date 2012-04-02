### MusicBrainz for Android

This is the primary repository for the [MusicBrainz](http://www.musicbrainz.org) Android app. This project started as a Google Summer of Code project in 2010. You might also be interested in the [official product page](http://musicbrainz.org/doc/MusicBrainz_for_Android) over at the MusicBrainz website.

### Android Market

This app has been available on the [Google Play Store](https://market.android.com/details?id=org.musicbrainz.mobile) since 1st August 2011.

### Betas

There may occasionally be preview builds to test new functionality. Check the [downloads](https://github.com/jdamcd/musicbrainz-android/downloads) page.

### Licenses

This app is licensed under the GNU General Public License Version 3.
The ZXing barcode scanner integration code and other helper classes are licensed under the Apache License, Version 2.
Refer to headers for further details.

### Helping out

Please report issues on the [MusicBrainz issue tracker](http://tickets.musicbrainz.org/).

Code contributions are encouraged in the form of pull requests. Please use the following code formatting and style guidelines:

* Tabs are 4 spaces in Java code and 2 spaces in XML
* 120 character line width
* Use descriptive names and comment as a last resort

It might be helpful to use this [eclipse code formatting configuration](https://github.com/novoda/public-mvn-repo/blob/master/eclipse/clean_code_formatter_profile.xml).

### Building with Maven

You will need [this profile](https://github.com/novoda/public-mvn-repo/blob/master/poms/settings.xml) in your ~/.m2/settings.xml.

The following command will setup the project and copy the necessary dependencies so that you can work in Eclipse.

    $mvn clean initialize -Peclipse
  
You can also just do something like `mvn install`, which will build, install and run the tests (you may need to attach an Android device first).
  

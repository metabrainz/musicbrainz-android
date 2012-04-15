## MusicBrainz for Android

This is the primary repository for the [MusicBrainz](http://www.musicbrainz.org) Android app. This project started as a Google Summer of Code project in 2010. You might also be interested in the [official product page](http://musicbrainz.org/doc/MusicBrainz_for_Android) over at the MusicBrainz website.

### Android Market

This app has been available on the [Google Play Store](https://market.android.com/details?id=org.musicbrainz.mobile) since 1st August 2011.

### Betas

There may occasionally be preview builds to test new functionality. Check the [downloads](https://github.com/jdamcd/musicbrainz-android/downloads) page.

## Licenses

This app is licensed under the GNU General Public License Version 3.
The ZXing barcode scanner integration code and other helper classes are licensed under the Apache License, Version 2.
Refer to headers for further details.

## Helping out

Please report issues on the [MusicBrainz issue tracker](http://tickets.musicbrainz.org/).

Code contributions are encouraged in the form of pull requests. Please use the following code formatting and style guidelines:

* Tabs are 4 spaces in Java code and 2 spaces in XML
* 120 character line width
* Use descriptive names and comment as a last resort

It might be helpful to use this [eclipse code formatting configuration](https://github.com/novoda/public-mvn-repo/blob/master/eclipse/clean_code_formatter_profile.xml).

### Building with Maven

You need [this profile](https://github.com/novoda/public-mvn-repo/blob/master/poms/settings.xml) in your ~/.m2/settings.xml. 
Install the Android SDK (level 15) and Android Support Library using the Android SDK Manager. Deploy them to your local maven repo using the [Maven Android SDK Deployer](https://github.com/mosabua/maven-android-sdk-deployer).

The following command will setup the project and copy the necessary dependencies so that you can work in Eclipse.

    $mvn clean initialize -Peclipse
  
You can also do something like `mvn install`, which will build, install and run the tests (you need to attach an Android device for that).

### Eclipse project setup

Create an Android project from the source in app and a Java project from the source in api. Add the api project to the app project build path and set it to exported.

There are three Android library projects that need to be included and specified in the Android project properties.

* [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock)
* [ViewPagerIndicator](https://github.com/JakeWharton/Android-ViewPagerIndicator)
* [Boosterstrap](https://github.com/jdamcd/boosterstrap)
  
Clone these from the GitHub repos above, import them to your Eclipse workspace and point the app project to them in Properties > Android.

### Can I skip all this Maven stuff?

Yes. However, you will need to look at the dependencies in the POMs and go hunting for the jars yourself.
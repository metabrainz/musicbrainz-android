## MusicBrainz Android

This is the official [MusicBrainz](http://www.musicbrainz.org) Android app. It started as a Google Summer of Code project in 2010. Here is the [MusicBrainz product page](http://musicbrainz.org/doc/MusicBrainz_for_Android) for the app.

### Download

Get the latest release of the app from [Google Play](https://play.google.com/store/apps/details?id=org.musicbrainz.mobile).

## Licenses

Copyright © 2013 Jamie McDonald. Licensed under the GNU General Public License Version 3.
ZXing barcode scanner integration code and miscellaneous helper classes are licensed under the Apache License, Version 2 (see headers).

## Contributing

Please submit issues and feature requests on GitHub.

Code contributions are welcomed in the form of [pull requests](https://help.github.com/articles/using-pull-requests). Please use the following code formatting and style guidelines:

* Tabs are 4 spaces in Java code and 2 spaces in XML
* 120 character line width
* Use descriptive names and comment as a last resort

A code formatting configuration file is included in the repo.

### Building with Maven

If you have Maven 3, the Android SDK and an ANDROID_HOME environment variable, you can simply build with:

    $mvn clean install
  
This expects an Android device to be attached for running instrumentation tests. Alternatively, use the ```-DskipTests``` option.

### IntelliJ project setup

Import as existing Maven project. Just works.

### Can I skip all this Maven stuff?

Yes. However, you'll need to look at the dependencies in the POMs and go hunting for the dependencies yourself.

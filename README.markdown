### MusicBrainz for Android

This is the main repository for the [MusicBrainz](http://www.musicbrainz.org) Android app. This project started as a Google Summer of Code project in 2010. You might also be interested in the [official product page](http://musicbrainz.org/doc/MusicBrainz_for_Android).

### Android Market

This app is available on [Android Market](https://market.android.com/details?id=org.musicbrainz.mobile). It was first published on 1st August 2011.

### Betas

There may occasionally be preview builds to test new functionality. Check the [downloads](https://github.com/jdamcd/MusicBrainzAndroid/downloads) page.

### Licenses

This app is licensed under the GNU General Public License Version 3.
The ZXing barcode scanner integration code and other helper classes are licensed under the Apache License, Version 2.
Refer to headers for further details.
Graphics originating from [androidicons.com](https://www.androidicons.com) are included under the Creative Commons Attribution 3.0 Unported License.

### Helping out

Please report issues on the [MusicBrainz issue tracker](http://tickets.musicbrainz.org/).

Code contributions are encouraged in the form of pull requests. Please use the following code formatting and style guidelines:

* Tabs are 4 spaces
* 120 character line width
* Use descriptive naming and comment as a last resort

It might be helpful to use this [eclipse code formatting configuration](https://github.com/novoda/public-mvn-repo/blob/master/eclipse/clean_code_formatter_profile.xml).

### Maven

If you want to build the project with Maven, you will need the following profile:

    <profile>
    <id>novoda</id>
	<repositories>
		<repository>
			<id>central</id>
			<name>Maven Repository Switchboard</name>
			<layout>default</layout>
			<url>http://repo1.maven.org/maven2</url>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
		<repository>
			<id>public-mvn-repo-snapshots</id>
			<url>https://github.com/novoda/public-mvn-repo/raw/master/snapshots</url>
		</repository>
		<repository>
			<id>public-mvn-repo-releases</id>
			<url>https://github.com/novoda/public-mvn-repo/raw/master/releases</url>
		</repository>
	</repositories>
    </profile>

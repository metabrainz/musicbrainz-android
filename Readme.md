<h1 align="center">
  <br>
  <a href="https://github.com/metabrainz/musicbrainz-android/archive/master.zip"><img src="https://github.com/metabrainz/metabrainz-logos/blob/master/logos/MusicBrainz/SVG/MusicBrainz_logo.svg" alt="MusicBrainz Android"></a>
</h1>

<h4 align="center">MusicBrainz Android</h4>

<p align="center">
    <a href="https://github.com/metabrainz/musicbrainz-android/commits/master">
    <img src="https://img.shields.io/github/last-commit/metabrainz/musicbrainz-android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub last commit"></a>
    <a href="https://github.com/metabrainz/musicbrainz-android/issues">
    <img src="https://img.shields.io/github/issues-raw/metabrainz/musicbrainz-android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub issues"></a>
    <a href="https://github.com/metabrainz/musicbrainz-android/pulls">
    <img src="https://img.shields.io/github/issues-pr-raw/metabrainz/musicbrainz-android.svg?style=flat-square&logo=github&logoColor=white"
         alt="GitHub pull requests"></a>
</p>
      
<p align="center">
  <a href="#What Is MusicBrainz?">About</a> •
  <a href="#development">Development</a> •
  <a href="#downloading">Downloading</a> •
  <a href="#contributing">Contributing</a> •
  <a href="roadmap">Roadmap</a> • 
  <a href="#issues">Issues</a> •
  <a href="#support">Support</a> •
  <a href="#license">License</a>
</p>

---

## What Is MusicBrainz?
<li>The ultimate source of music information by allowing anyone to contribute and releasing the data under open licenses.</li>
    <li>The universal lingua franca for music by providing a reliable and unambiguous form of music identification, enabling both people and machines to have meaningful conversations about music.</li>
    <br>
    Like Wikipedia, MusicBrainz is maintained by a global community of users and we want everyone, including you, to participate and contribute.
    <br><br>
    Visit https://musicbrainz.org/ to know more

## History of MusicBrainz
The roots of this project are in a software program called Workman for playing Audio CDs on UNIX systems. Workman had the ability to display the name of the track it was currently playing. An index file was used to store the track names for each Audio CD. After a while, a large index file with information about thousands of Audio CDs was created by the Internet community.

This was a long time ago. The index file system became more widely used when Windows users started using this index file, but the system was not very mature then. The Windows Audio CD player could use an index file with track information, but the index size was limited to 640KB. This meant that Windows users could not use the large Internet index file without correcting software.

In 1996 things changed when the Internet Compact Disc Database was created. Instead of a flat-file with information of thousands of Audio CDs, the client/server model was applied. A single central server called "CDDB.com" could be used to access the information of Audio CDs. This server accepted new submissions of Audio CD information. At that stage, the index file was reported to have grown by up to 800 Audio CDs per day. But these numbers say nothing about the quality of the submissions. The number of duplicate Audio CDs that now exist in the database is high — 10 entries of the same Audio CD under a different number is not uncommon. Many entries also contain numerous spelling errors. CDDB.com had no mechanism to correct errors.

Despite this, the system became popular and useful. Things changed dramatically when the open CDDB.com server was bought by a company that wanted to make money from the contributions that users had made. The index file created by the Internet community could no longer be copied. Patents were obtained and granted. A large public outcry resulted and led to the start of several projects to create an Open Source competitor for the commercial CDDB.com (now Gracenote).

Of the five originally started projects, two projects are still active — you are currently visiting one of them. The other, the FreeDB project was very quick in duplicating the functionality of the commercial CDDB.com server. This project has a very large collection of Audio CDs, more than 660,000 entries. A large group of users queries this information at a rate of more than 500,000 per day. The FreeDB.org servers do not use a relational database. The servers use a very large collection of files, one for each entered Audio CD.

The MusicBrainz project does not aim to be a drop-in replacement for CDDB. MusicBrainz uses a relational database and has a list of other features that makes it more advanced than the original CDDB server (although it did previously work as a replacement for CDDB clients, see FreeDB Gateway). MusicBrainz started as a tool called "CD Index." The new name was selected after a meeting in Amsterdam in 1999 where it was decided that the free insertion of information and website-based voting would be the focus of the second generation project. Over time, MusicBrainz's focus has changed from CD-identification to being an exhaustive source of information about all things musical, further advanced by the May 2011 release of the Next Generation Schema.

## What Is Musicbrainz Android For?
MusicBrainz is designed to do the following:

<li> View release information by scanning a barcode</li>
<li> Search for information about artists, releases, release groups,labels, recordings, instruments, and events.</li>
<li> View collections</li>
<li> Tag audio files like Picard</li>
<li> Send Releases to your Picard</li>
<li> Donate to the MetaBrainz Foundation via PayPal</li>

## How does MusicBrainz Do This?

MusicBrainz captures information about artists, their recorded works, and the relationships between them. Recorded works entries capture at a minimum the album title, track titles, and the length of each track. These entries are maintained by volunteer editors who follow community written style guidelines. Recorded works can also store information about the release date and country, the CD ID, cover art, acoustic fingerprint, free-form annotation text, and other metadata. As of September 2020, MusicBrainz contained information on roughly 1.7 million artists, 2.6 million releases, and 23 million recordings. End-users can use software that communicates with MusicBrainz to add metadata tags to their digital media files, such as ALAC, FLAC, MP3, Ogg Vorbis, or AAC.

## Development
	    
* Prerequisite: Latest version of the Android Studio and SDKs on your pc.
* Clone this repository.
* Use the `gradlew build` command to build the project directly or use the IDE to run the project to your phone or the emulator.

## Downloading

* [Google Play Store](https://play.google.com/store/apps/details?id=org.metabrainz.android)
	    
* [F-Droid](https://f-droid.org/en/packages/org.metabrainz.android/)
	    
## Contributing
	  
Got **something interesting** you'd like to **ask or share**? Start a discussion at `#metabrainz` IRC channel on libera.chat.

## Roadmap

Proposed future plans for the app are as follows:

- Introduce Jetpack Compose to the app.
- Support more entities for search.
- Showcase more \*Brainz powered functionalities.
- Find whether the recordings are present in your collections or not.
- Add AcoustId functionalities to the app.
	    
## Issues
	  
If you think you have found a bug, please report it on the [issue tracker](https://tickets.metabrainz.org/projects/MOBILE/issues). The app is under active development and some new features are planned. You can suggest and vote for new features in the same location.

## Tutorials

[Tagger Tutorials](https://picard-docs.musicbrainz.org/en/tutorials/android_app.html)
	    
## Support

Reach out to the developers at one of the following places:

- MetaBrainz Community: https://community.metabrainz.org/tag/android
- Development IRC Channel: `#metabrainz`
- E-Mail: **support@metabrainz.org**

## License

This Project is licensed under the [GPL version 3 or later](https://www.gnu.org/licenses/gpl-3.0.html) with sections under the [Apache License version](https://www.apache.org/licenses/LICENSE-2.0.html) 

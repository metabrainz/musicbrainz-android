/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.musicbrainz.mobile.data.Artist;
import org.musicbrainz.mobile.data.ArtistStub;
import org.musicbrainz.mobile.data.Release;
import org.musicbrainz.mobile.data.ReleaseGroup;
import org.musicbrainz.mobile.data.ReleaseStub;
import org.musicbrainz.mobile.parsers.ArtistLookupParser;
import org.musicbrainz.mobile.parsers.ArtistSearchParser;
import org.musicbrainz.mobile.parsers.BarcodeSearchParser;
import org.musicbrainz.mobile.parsers.RGSearchParser;
import org.musicbrainz.mobile.parsers.ReleaseLookupParser;
import org.musicbrainz.mobile.parsers.ReleaseStubParser;
import org.musicbrainz.mobile.parsers.TagRatingParser;
import org.musicbrainz.mobile.util.Config;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This class makes the web service available to Activity classes. The XML
 * returned is parsed into data objects with SAX parser handlers.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class WebService {
	
	public static final String WEB_SERVICE = Config.WEB_SERVICE;
	
	private static final String LOOKUP_ARTIST = "artist/";
	private static final String LOOKUP_ARTIST_PARAMS = "?inc=url-rels+tags+ratings";
	private static final String BROWSE_ARTIST_RGS = "release-group?artist=";
	private static final String BROWSE_ARTIST_RGS_PARAMS = "&limit=100";
	
	private static final String LOOKUP_RELEASE = "release/";
	private static final String LOOKUP_RELEASE_PARAMS = "?inc=release-groups+artists+recordings+labels+tags+ratings";
	private static final String BROWSE_RG_RELEASES = "release?release-group=";
	private static final String BROWSE_RG_RELEASES_PARAMS = "&inc=artist-credits+labels+mediums";
	
	private static final String SEARCH_ARTIST = "artist?query=";
	private static final String SEARCH_RG = "release-group?query=";
	private static final String SEARCH_RELEASE = "release?query=";
	private static final String SEARCH_BARCODE = "release/?query=barcode:";
	private static final String SEARCH_BARCODE_PARAMS = "&limit=1";

	public static Release lookupReleaseFromBarcode(String barcode) throws IOException, SAXException {
		
		XMLReader reader = getXMLReader();
		BarcodeSearchParser bHandler = new BarcodeSearchParser();
		reader.setContentHandler(bHandler);
		
		URL bQuery = new URL(WEB_SERVICE + SEARCH_BARCODE + barcode + SEARCH_BARCODE_PARAMS);
		reader.parse(new InputSource(bQuery.openStream()));
		
		if (bHandler.isBarcodeFound()) {
			return lookupRelease(bHandler.getMbid());
		} 
		return null;
	}
	
	public static Release lookupRelease(String mbid) throws IOException, SAXException {
		
		Release release = new Release();
		release.setReleaseMbid(mbid);
		
		XMLReader reader = getXMLReader();
		ReleaseLookupParser rHandler = new ReleaseLookupParser(release);
		reader.setContentHandler(rHandler);
		
		URL rQuery = new URL(WEB_SERVICE + LOOKUP_RELEASE + mbid + LOOKUP_RELEASE_PARAMS);
		reader.parse(new InputSource(rQuery.openStream()));
		
		return release;
	}
	
	public static LinkedList<ReleaseStub> browseReleases(String mbid) throws IOException, SAXException {
		
		LinkedList<ReleaseStub> stubs = new LinkedList<ReleaseStub>();
		
		XMLReader reader = getXMLReader();
		ReleaseStubParser handler = new ReleaseStubParser(stubs);
		reader.setContentHandler(handler);
		
		URL query = new URL(WEB_SERVICE + BROWSE_RG_RELEASES + mbid
				+ BROWSE_RG_RELEASES_PARAMS);
		reader.parse(new InputSource(query.openStream()));
		
		return stubs;
	}
	
	public static Artist lookupArtist(String mbid) throws IOException, SAXException {
		
		Artist artist = new Artist();
		artist.setMbid(mbid);

		XMLReader reader = getXMLReader();
		ArtistLookupParser handler = new ArtistLookupParser(artist);
		reader.setContentHandler(handler);

		URL artistQuery = new URL(WEB_SERVICE + LOOKUP_ARTIST + mbid + LOOKUP_ARTIST_PARAMS);
		reader.parse(new InputSource(artistQuery.openStream()));

		URL releaseGroupQuery = new URL(WEB_SERVICE + BROWSE_ARTIST_RGS + mbid + BROWSE_ARTIST_RGS_PARAMS);
		reader.parse(new InputSource(releaseGroupQuery.openStream()));

		return artist;
	}
	
	public static LinkedList<ArtistStub> searchArtist(String searchTerm) throws IOException, SAXException {
		
		LinkedList<ArtistStub> results = new LinkedList<ArtistStub>();
		
		XMLReader reader = getXMLReader();
		ArtistSearchParser handler = new ArtistSearchParser(results);
		reader.setContentHandler(handler);

		URL query = new URL(WEB_SERVICE + SEARCH_ARTIST + sanitise(searchTerm));
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	public static LinkedList<ReleaseGroup> searchReleaseGroup(String searchTerm) throws IOException, SAXException {
		
		LinkedList<ReleaseGroup> results = new LinkedList<ReleaseGroup>();
		
		XMLReader reader = getXMLReader();
		RGSearchParser handler = new RGSearchParser(results);
		reader.setContentHandler(handler);

		URL query = new URL(WEB_SERVICE + SEARCH_RG + sanitise(searchTerm));
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	public static LinkedList<ReleaseStub> searchRelease(String searchTerm) throws IOException, SAXException {
		
		LinkedList<ReleaseStub> results = new LinkedList<ReleaseStub>();
		
		XMLReader reader = getXMLReader();
		ReleaseStubParser handler = new ReleaseStubParser(results);
		reader.setContentHandler(handler);
					
		URL query = new URL(WEB_SERVICE + SEARCH_RELEASE + sanitise(searchTerm));
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	public static Collection<String> refreshTags(MBEntity type, String mbid) throws IOException, SAXException {
		
		LinkedList<String> tags = new LinkedList<String>();
		
		XMLReader reader = getXMLReader();
		TagRatingParser tagHandler = new TagRatingParser(tags);
		reader.setContentHandler(tagHandler);
		
		String entity = "";
		switch(type) {
		case ARTIST:
			entity = "artist";
			break;
		case RELEASE_GROUP:
			entity = "release-group";
		}
		
		URL tagQuery = new URL(WEB_SERVICE + entity + "/" + mbid + "?inc=tags");
		reader.parse(new InputSource(tagQuery.openStream()));
		
		return tags;
	}
	
	public static float refreshRating(MBEntity type, String mbid) throws IOException, SAXException {
		
		float rating;
		
		XMLReader reader = getXMLReader();
		TagRatingParser ratingHandler = new TagRatingParser();
		reader.setContentHandler(ratingHandler);
		
		String entity = "";
		switch(type) {
		case ARTIST:
			entity = "artist";
			break;
		case RELEASE_GROUP:
			entity = "release-group";
		}
		
		URL tagQuery = new URL(WEB_SERVICE + entity + "/" + mbid + "?inc=ratings");
		reader.parse(new InputSource(tagQuery.openStream()));
		
		rating = ratingHandler.getRating();
		
		return rating;
	}
	
	private static XMLReader getXMLReader() {
		
		// TODO hang on to factory reference, avoid new instances
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			SAXParser parser = factory.newSAXParser();
			return parser.getXMLReader();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String sanitise(String input) throws UnsupportedEncodingException {
		return URLEncoder.encode(input, "UTF-8");
	}
	
	public enum MBEntity {
		ARTIST,
		RELEASE_GROUP
	}
	
}

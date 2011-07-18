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
import java.net.URL;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This class makes the web service available to Activity classes. Static
 * methods manage web service calls and return the appropriate data objects. The
 * XML returned is parsed into data objects with custom SAX parser handlers.
 * This is really fast.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class WebService {
	
	protected static final String WEB_SERVICE = "http://test.musicbrainz.org/ws/2/";
	
	// lookups
	private static final String ARTIST_LOOKUP = "artist/";
	private static final String ARTIST_PARAMS = "?inc=url-rels+tags+ratings";
	
	private static final String ARTIST_RG_BROWSE = "release-group?artist=";
	private static final String ARTIST_RG_PARAMS = "&limit=100";
	
	private static final String RELEASE_LOOKUP = "release/";
	private static final String RELEASE_PARAMS = "?inc=release-groups+artists+recordings+labels+tags+ratings";
	
	private static final String RG_RELEASE_BROWSE = "release?release-group=";
	private static final String RG_RELEASE_PARAMS = "&inc=artist-credits+labels+mediums";
	
	// searches
	private static final String SEARCH_ARTIST = "artist?query=";
	private static final String SEARCH_RG = "release-group?query=";
	private static final String SEARCH_RELEASE = "release?query=";
	
	private static final String SEARCH_BARCODE = "release/?query=barcode:";
	private static final String BARCODE_PARAMS = "&limit=1";

	/**
	 * Search for barcode and if associated release is found, get release data
	 * with the lookupRequest method.
	 * 
	 * @return Release object or null if barcode not found.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static Release lookupBarcode(String barcode) throws IOException, SAXException {
		
		XMLReader reader = getXMLReader();
		BarcodeSearchParser bHandler = new BarcodeSearchParser();
		reader.setContentHandler(bHandler);
		
		URL bQuery = new URL(WEB_SERVICE + SEARCH_BARCODE + barcode
				+ BARCODE_PARAMS);
		reader.parse(new InputSource(bQuery.openStream()));
		
		if (bHandler.isFound())
			return lookupRelease(bHandler.getID());
		else
			return null; // barcode not found
	}
	
	/**
	 * Lookup release and create Release object from the XML returned.
	 * 
	 * @param id: Release MBID.
	 * @return Release data.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static Release lookupRelease(String id) throws IOException, SAXException {
		
		Release release = new Release();
		release.setReleaseMbid(id);
		
		XMLReader reader = getXMLReader();
		ReleaseLookupParser rHandler = new ReleaseLookupParser(release);
		reader.setContentHandler(rHandler);
		
		URL rQuery = new URL(WEB_SERVICE + RELEASE_LOOKUP + id + RELEASE_PARAMS);
		reader.parse(new InputSource(rQuery.openStream()));
		
		return release;
	}
	
	/**
	 * Browse request which creates a list of ReleaseStubs given a release group ID.
	 * 
	 * @param Release group MBID.
	 * @return Release stubs.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static LinkedList<ReleaseStub> browseReleases(String id) throws IOException, SAXException {
		
		LinkedList<ReleaseStub> stubs = new LinkedList<ReleaseStub>();
		
		XMLReader reader = getXMLReader();
		ReleaseStubParser handler = new ReleaseStubParser(stubs);
		reader.setContentHandler(handler);
		
		URL query = new URL(WEB_SERVICE + RG_RELEASE_BROWSE + id
				+ RG_RELEASE_PARAMS);
		reader.parse(new InputSource(query.openStream()));
		
		return stubs;
	}
	
	/**
	 * Lookup artist and create Artist object from the XML returned.
	 * 
	 * @param Artist MBID.
	 * @return Artist data.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static Artist lookupArtist(String id) throws IOException, SAXException {
		
		Artist artist = new Artist();
		artist.setMbid(id);

		XMLReader reader = getXMLReader();
		ArtistLookupParser handler = new ArtistLookupParser(artist);
		reader.setContentHandler(handler);

		URL query = new URL(WEB_SERVICE + ARTIST_LOOKUP + id + ARTIST_PARAMS);
		reader.parse(new InputSource(query.openStream()));
		// get release groups list
		query = new URL(WEB_SERVICE + ARTIST_RG_BROWSE + id + ARTIST_RG_PARAMS);
		reader.parse(new InputSource(query.openStream()));

		return artist;
	}
	
	/**
	 * Search for artists given a String search term.
	 * 
	 * @param Search term.
	 * @return Artist matches list.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static LinkedList<ArtistStub> searchArtist(String term) throws IOException, SAXException {
		
		term = sanitize(term);
		LinkedList<ArtistStub> results = new LinkedList<ArtistStub>();
		
		XMLReader reader = getXMLReader();
		ArtistSearchParser handler = new ArtistSearchParser(results);
		reader.setContentHandler(handler);

		URL query = new URL(WEB_SERVICE + SEARCH_ARTIST + term);
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	/**
	 * Search for release groups given a String search term.
	 * 
	 * @param Search term.
	 * @return ReleaseGroup matches list.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static LinkedList<ReleaseGroup> searchReleaseGroup(String term) throws IOException, SAXException {
		
		term = sanitize(term);
		LinkedList<ReleaseGroup> results = new LinkedList<ReleaseGroup>();
		
		XMLReader reader = getXMLReader();
		RGSearchParser handler = new RGSearchParser(results);
		reader.setContentHandler(handler);

		URL query = new URL(WEB_SERVICE + SEARCH_RG + term);
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	/**
	 * Search for release groups given a String search term.
	 * 
	 * @param Search term.
	 * @return ReleaseGroup matches list.
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static LinkedList<ReleaseStub> searchRelease(String term) throws IOException, SAXException {
		
		term = sanitize(term);
		LinkedList<ReleaseStub> results = new LinkedList<ReleaseStub>();
		
		XMLReader reader = getXMLReader();
		ReleaseStubParser handler = new ReleaseStubParser(results);
		reader.setContentHandler(handler);
					
		URL query = new URL(WEB_SERVICE + SEARCH_RELEASE + term);
		reader.parse(new InputSource(query.openStream()));

		return results;
	}
	
	public static Collection<String> refreshTags(MBEntity type, String entityID) throws IOException, SAXException {
		
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
		
		URL tagQuery = new URL(WEB_SERVICE + entity + "/" + entityID + "?inc=tags");
		reader.parse(new InputSource(tagQuery.openStream()));
		
		return tags;
	}
	
	public static float refreshRating(MBEntity type, String entityID) throws IOException, SAXException {
		
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
		
		URL tagQuery = new URL(WEB_SERVICE + entity + "/" + entityID + "?inc=ratings");
		reader.parse(new InputSource(tagQuery.openStream()));
		
		rating = ratingHandler.getRating();
		
		return rating;
	}
	
	/*
	 * Create an XML reader. This happens for every request.
	 */
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
	
	/*
	 * Remove characters which are not safe in the web service request URL.
	 * 
	 * TODO This doesn't allow text search using Unicode characters.
	 */
	private static String sanitize(String input) {
		
		String output = "";
		
		for (char c : input.toCharArray()) {
			if (c == ' ')
				output += '+';
			else if (c >= 48 && c <= 57)  
				output += c; // 0-9
			else if (c >= 65 && c <= 90)
				output += c; // A-Z
			else if (c >= 97 && c <= 122)
				output += c; // a-z
		}
		
		return output;
	}
	
	/**
	 * Types for tag/rating entities.
	 */
	public enum MBEntity {
		ARTIST,
		RELEASE_GROUP
	}
	
}

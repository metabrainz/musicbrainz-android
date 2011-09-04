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

package org.musicbrainz.android.api.ws;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.parsers.ArtistLookupParser;
import org.musicbrainz.android.api.parsers.ArtistSearchParser;
import org.musicbrainz.android.api.parsers.BarcodeSearchParser;
import org.musicbrainz.android.api.parsers.RGBrowseParser;
import org.musicbrainz.android.api.parsers.RGSearchParser;
import org.musicbrainz.android.api.parsers.RatingParser;
import org.musicbrainz.android.api.parsers.ReleaseLookupParser;
import org.musicbrainz.android.api.parsers.ReleaseStubParser;
import org.musicbrainz.android.api.parsers.TagParser;
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
	
	private SAXParserFactory factory;
	
	public WebService() {
		factory = SAXParserFactory.newInstance();
	}
	
	public Release lookupReleaseFromBarcode(String barcode) throws IOException, SAXException {
		URL url = QueryBuilder.barcodeLookup(barcode); 
		InputSource xmlStream = new InputSource(url.openStream());
		String barcodeMbid = parseMbidFromBarcode(xmlStream);
		if (barcodeMbid == null) {
			throw new BarcodeNotFoundException(barcode);
		}
		return lookupRelease(barcodeMbid);
	}
	
	private String parseMbidFromBarcode(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		BarcodeSearchParser parser = new BarcodeSearchParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getMbid();
	}
	
	public Release lookupRelease(String mbid) throws IOException, SAXException {
		URL url = QueryBuilder.releaseLookup(mbid);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseRelease(xmlStream);
	}
	
	private Release parseRelease(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		ReleaseLookupParser parser = new ReleaseLookupParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResult();
	}
	
	public LinkedList<ReleaseStub> browseReleases(String mbid) throws IOException, SAXException {
		URL url = QueryBuilder.releaseGroupReleaseBrowse(mbid);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseRGReleases(xmlStream);
	}
	
	private LinkedList<ReleaseStub> parseRGReleases(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		ReleaseStubParser parser = new ReleaseStubParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResults();
	}
	
	public Artist lookupArtist(String mbid) throws IOException, SAXException {
		URL artistUrl = QueryBuilder.artistLookup(mbid);
		InputSource artistXmlStream = new InputSource(artistUrl.openStream());
		Artist artist = parseArtist(artistXmlStream);
		
		URL rgUrl = QueryBuilder.artistReleaseGroupBrowse(mbid);
		InputSource rgXmlStream = new InputSource(rgUrl.openStream());
		ArrayList<ReleaseGroup> releases = parseReleaseGroupBrowse(rgXmlStream);
		
		artist.setReleaseGroups(releases);
		return artist;
	}
	
	private Artist parseArtist(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		ArtistLookupParser parser = new ArtistLookupParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResult();
	}
	
	private ArrayList<ReleaseGroup> parseReleaseGroupBrowse(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		RGBrowseParser parser = new RGBrowseParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResults();		
	}
	
	public LinkedList<ArtistStub> searchArtists(String searchTerm) throws IOException, SAXException {
		URL url = QueryBuilder.artistSearch(searchTerm);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseArtistSearch(xmlStream);
	}
	
	private LinkedList<ArtistStub> parseArtistSearch(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		ArtistSearchParser parser = new ArtistSearchParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResults();
	}
	
	public LinkedList<ReleaseGroup> searchReleaseGroup(String searchTerm) throws IOException, SAXException {
		URL url = QueryBuilder.releaseGroupSearch(searchTerm);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseReleaseGroupSearch(xmlStream);
	}
	
	private LinkedList<ReleaseGroup> parseReleaseGroupSearch(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		RGSearchParser parser = new RGSearchParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResults();
	}
	
	public LinkedList<ReleaseStub> searchRelease(String searchTerm) throws IOException, SAXException {
		URL url = QueryBuilder.releaseSearch(searchTerm);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseReleaseSearch(xmlStream);
	}
	
	private LinkedList<ReleaseStub> parseReleaseSearch(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		ReleaseStubParser parser = new ReleaseStubParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getResults();
	}
	
	public Collection<String> lookupTags(MBEntity type, String mbid) throws IOException, SAXException {
		URL url = QueryBuilder.tagLookup(type, mbid);
		InputSource xmlStream = new InputSource(url.openStream());
		return parseTagLookup(xmlStream);
	}
	
	private Collection<String> parseTagLookup(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		TagParser parser = new TagParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getTags();
	}
	
	public float lookupRating(MBEntity type, String mbid) throws IOException, SAXException {
		URL url = QueryBuilder.ratingLookup(type, mbid);
		InputSource xmlStream= new InputSource(url.openStream());
		return parseRatingLookup(xmlStream);
	}
	
	private float parseRatingLookup(InputSource source) throws IOException, SAXException {
		XMLReader reader = getXMLReader();
		RatingParser parser = new RatingParser();
		reader.setContentHandler(parser);
		reader.parse(source);
		return parser.getRating();
	}
	
	private XMLReader getXMLReader() {

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
	
	public enum MBEntity {
		ARTIST,
		RELEASE_GROUP
	}
	
}

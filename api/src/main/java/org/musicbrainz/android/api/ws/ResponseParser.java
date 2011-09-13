/*
 * Copyright (C) 2011 Jamie McDonald
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.handlers.ArtistLookupHandler;
import org.musicbrainz.android.api.handlers.ArtistSearchHandler;
import org.musicbrainz.android.api.handlers.BarcodeSearchHandler;
import org.musicbrainz.android.api.handlers.ReleaseGroupBrowseHandler;
import org.musicbrainz.android.api.handlers.ReleaseGroupSearchHandler;
import org.musicbrainz.android.api.handlers.RatingHandler;
import org.musicbrainz.android.api.handlers.ReleaseLookupHandler;
import org.musicbrainz.android.api.handlers.ReleaseStubHandler;
import org.musicbrainz.android.api.handlers.TagHandler;
import org.musicbrainz.android.api.handlers.UserDataHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ResponseParser {
	
	private SAXParserFactory factory;
	
	public ResponseParser() {
		factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
	}
	
	public ResponseParser(SAXParserFactory factory) {
		this.factory = factory;
	}
	
	public String parseMbidFromBarcode(InputStream stream) throws IOException {
		BarcodeSearchHandler handler = new BarcodeSearchHandler();
		doParsing(stream, handler);
		return handler.getMbid();
	}
	
	public Release parseRelease(InputStream stream) throws IOException {
		ReleaseLookupHandler handler = new ReleaseLookupHandler();
		doParsing(stream, handler);
		return handler.getResult();
	}
	
	public LinkedList<ReleaseStub> parseRGReleases(InputStream stream) throws IOException {
		ReleaseStubHandler handler = new ReleaseStubHandler();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public Artist parseArtist(InputStream stream) throws IOException {
		ArtistLookupHandler handler = new ArtistLookupHandler();
		doParsing(stream, handler);
		return handler.getResult();
	}
	
	public ArrayList<ReleaseGroupStub> parseReleaseGroupBrowse(InputStream stream) throws IOException {
		ReleaseGroupBrowseHandler handler = new ReleaseGroupBrowseHandler();
		doParsing(stream, handler);
		return handler.getResults();		
	}
	
	public LinkedList<ArtistStub> parseArtistSearch(InputStream stream) throws IOException {
		ArtistSearchHandler handler = new ArtistSearchHandler();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public LinkedList<ReleaseGroupStub> parseReleaseGroupSearch(InputStream stream) throws IOException {
		ReleaseGroupSearchHandler handler = new ReleaseGroupSearchHandler();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public LinkedList<ReleaseStub> parseReleaseSearch(InputStream stream) throws IOException {
		ReleaseStubHandler handler = new ReleaseStubHandler();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public Collection<String> parseTagLookup(InputStream stream) throws IOException {
		TagHandler handler = new TagHandler();
		doParsing(stream, handler);
		return handler.getTags();
	}
	
	public float parseRatingLookup(InputStream stream) throws IOException {
		RatingHandler handler = new RatingHandler();
		doParsing(stream, handler);
		return handler.getRating();
	}
	
	public UserData parseUserData(InputStream stream) throws IOException {
		UserDataHandler handler = new UserDataHandler();
		doParsing(stream, handler);
		return handler.getResult();
	}
	
	protected void doParsing(InputStream stream, DefaultHandler handler) throws IOException {
		try {
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			InputSource source = new InputSource(stream);
			reader.setContentHandler(handler);
			reader.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
}

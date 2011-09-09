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
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.parsers.ArtistLookupParser;
import org.musicbrainz.android.api.parsers.ArtistSearchParser;
import org.musicbrainz.android.api.parsers.BarcodeSearchParser;
import org.musicbrainz.android.api.parsers.RGBrowseParser;
import org.musicbrainz.android.api.parsers.RGSearchParser;
import org.musicbrainz.android.api.parsers.RatingParser;
import org.musicbrainz.android.api.parsers.ReleaseLookupParser;
import org.musicbrainz.android.api.parsers.ReleaseStubParser;
import org.musicbrainz.android.api.parsers.TagParser;
import org.musicbrainz.android.api.parsers.UserDataParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ResponseParser {
	
	private SAXParserFactory factory;
	
	public ResponseParser() {
		factory = SAXParserFactory.newInstance();
	}
	
	public ResponseParser(SAXParserFactory factory) {
		this.factory = factory;
	}
	
	public String parseMbidFromBarcode(InputStream stream) throws IOException {
		BarcodeSearchParser handler = new BarcodeSearchParser();
		doParsing(stream, handler);
		return handler.getMbid();
	}
	
	public Release parseRelease(InputStream stream) throws IOException {
		ReleaseLookupParser handler = new ReleaseLookupParser();
		doParsing(stream, handler);
		return handler.getResult();
	}
	
	public LinkedList<ReleaseStub> parseRGReleases(InputStream stream) throws IOException {
		ReleaseStubParser handler = new ReleaseStubParser();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public Artist parseArtist(InputStream stream) throws IOException {
		ArtistLookupParser handler = new ArtistLookupParser();
		doParsing(stream, handler);
		return handler.getResult();
	}
	
	public ArrayList<ReleaseGroup> parseReleaseGroupBrowse(InputStream stream) throws IOException {
		RGBrowseParser handler = new RGBrowseParser();
		doParsing(stream, handler);
		return handler.getResults();		
	}
	
	public LinkedList<ArtistStub> parseArtistSearch(InputStream stream) throws IOException {
		ArtistSearchParser handler = new ArtistSearchParser();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public LinkedList<ReleaseGroup> parseReleaseGroupSearch(InputStream stream) throws IOException {
		RGSearchParser handler = new RGSearchParser();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public LinkedList<ReleaseStub> parseReleaseSearch(InputStream stream) throws IOException {
		ReleaseStubParser handler = new ReleaseStubParser();
		doParsing(stream, handler);
		return handler.getResults();
	}
	
	public Collection<String> parseTagLookup(InputStream stream) throws IOException {
		TagParser handler = new TagParser();
		doParsing(stream, handler);
		return handler.getTags();
	}
	
	public float parseRatingLookup(InputStream stream) throws IOException {
		RatingParser handler = new RatingParser();
		doParsing(stream, handler);
		return handler.getRating();
	}
	
	public UserData parseUserData(InputStream stream) throws IOException {
		UserDataParser handler = new UserDataParser();
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
			System.out.println("" + e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.out.println("" + e.getMessage());
			e.printStackTrace();
		}
	}
	
}

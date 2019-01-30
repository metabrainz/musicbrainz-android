package org.metabrainz.mobile.api.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.ArtistSearchResult;
import org.metabrainz.mobile.api.data.UserCollection;
import org.metabrainz.mobile.api.data.UserSearchResult;
import org.metabrainz.mobile.api.data.Label;
import org.metabrainz.mobile.api.data.LabelSearchResult;
import org.metabrainz.mobile.api.data.Recording;
import org.metabrainz.mobile.api.data.RecordingSearchResult;
import org.metabrainz.mobile.api.data.Release;
import org.metabrainz.mobile.api.data.ReleaseGroup;
import org.metabrainz.mobile.api.data.ReleaseGroupSearchResult;
import org.metabrainz.mobile.api.data.ReleaseSearchResult;
import org.metabrainz.mobile.api.data.Tag;
import org.metabrainz.mobile.api.data.UserData;
import org.metabrainz.mobile.api.handler.ArtistLookupHandler;
import org.metabrainz.mobile.api.handler.ArtistSearchHandler;
import org.metabrainz.mobile.api.handler.BarcodeSearchHandler;
import org.metabrainz.mobile.api.handler.CollectionHandler;
import org.metabrainz.mobile.api.handler.CollectionListHandler;
import org.metabrainz.mobile.api.handler.LabelLookupHandler;
import org.metabrainz.mobile.api.handler.LabelSearchHandler;
import org.metabrainz.mobile.api.handler.RatingHandler;
import org.metabrainz.mobile.api.handler.RecordingLookupHandler;
import org.metabrainz.mobile.api.handler.RecordingSearchHandler;
import org.metabrainz.mobile.api.handler.ReleaseGroupBrowseHandler;
import org.metabrainz.mobile.api.handler.ReleaseGroupLookupHandler;
import org.metabrainz.mobile.api.handler.ReleaseGroupSearchHandler;
import org.metabrainz.mobile.api.handler.ReleaseLookupHandler;
import org.metabrainz.mobile.api.handler.ReleaseInfoHandler;
import org.metabrainz.mobile.api.handler.TagHandler;
import org.metabrainz.mobile.api.handler.UserDataHandler;
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
        parse(stream, handler);
        return handler.getMbid();
    }

    public Release parseRelease(InputStream stream) throws IOException {
        ReleaseLookupHandler handler = new ReleaseLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public LinkedList<ReleaseSearchResult> parseReleaseGroupReleases(InputStream stream) throws IOException {
        ReleaseInfoHandler handler = new ReleaseInfoHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public Artist parseArtist(InputStream stream) throws IOException {
        ArtistLookupHandler handler = new ArtistLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public ArrayList<ReleaseGroupSearchResult> parseReleaseGroupBrowse(InputStream stream) throws IOException {
        ReleaseGroupBrowseHandler handler = new ReleaseGroupBrowseHandler();
        parse(stream, handler);
        return handler.getResults();
    }
    
    public ReleaseGroup parseReleaseGroupLookup(InputStream stream) throws IOException {
        ReleaseGroupLookupHandler handler = new ReleaseGroupLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public Label parseLabel(InputStream stream) throws IOException {
        LabelLookupHandler handler = new LabelLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public Recording parseRecording(InputStream stream) throws IOException {
        RecordingLookupHandler handler = new RecordingLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public LinkedList<ArtistSearchResult> parseArtistSearch(InputStream stream) throws IOException {
        ArtistSearchHandler handler = new ArtistSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<ReleaseGroupSearchResult> parseReleaseGroupSearch(InputStream stream) throws IOException {
        ReleaseGroupSearchHandler handler = new ReleaseGroupSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<ReleaseSearchResult> parseReleaseSearch(InputStream stream) throws IOException {
        ReleaseInfoHandler handler = new ReleaseInfoHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<LabelSearchResult> parseLabelSearch(InputStream stream) throws IOException {
        LabelSearchHandler handler = new LabelSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<RecordingSearchResult> parseRecordingSearch(InputStream stream) throws IOException {
        RecordingSearchHandler handler = new RecordingSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<Tag> parseTagLookup(InputStream stream) throws IOException {
        TagHandler handler = new TagHandler();
        parse(stream, handler);
        return handler.getTags();
    }

    public float parseRatingLookup(InputStream stream) throws IOException {
        RatingHandler handler = new RatingHandler();
        parse(stream, handler);
        return handler.getRating();
    }

    public UserData parseUserData(InputStream stream) throws IOException {
        UserDataHandler handler = new UserDataHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public LinkedList<UserSearchResult> parseCollectionListLookup(InputStream stream) throws IOException {
        CollectionListHandler handler = new CollectionListHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public UserCollection parseCollectionLookup(InputStream stream) throws IOException {
        CollectionHandler handler = new CollectionHandler();
        parse(stream, handler);
        return handler.getCollection();
    }

    protected void parse(InputStream stream, DefaultHandler handler) throws IOException {
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

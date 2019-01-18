package org.metabrainz.android.api.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.metabrainz.android.api.data.Artist;
import org.metabrainz.android.api.data.ArtistSearchResult;
import org.metabrainz.android.api.data.UserCollection;
import org.metabrainz.android.api.data.UserCollectionInfo;
import org.metabrainz.android.api.data.Label;
import org.metabrainz.android.api.data.LabelSearchResult;
import org.metabrainz.android.api.data.Recording;
import org.metabrainz.android.api.data.RecordingInfo;
import org.metabrainz.android.api.data.Release;
import org.metabrainz.android.api.data.ReleaseGroup;
import org.metabrainz.android.api.data.ReleaseGroupInfo;
import org.metabrainz.android.api.data.ReleaseInfo;
import org.metabrainz.android.api.data.Tag;
import org.metabrainz.android.api.data.UserData;
import org.metabrainz.android.api.handler.ArtistLookupHandler;
import org.metabrainz.android.api.handler.ArtistSearchHandler;
import org.metabrainz.android.api.handler.BarcodeSearchHandler;
import org.metabrainz.android.api.handler.CollectionHandler;
import org.metabrainz.android.api.handler.CollectionListHandler;
import org.metabrainz.android.api.handler.LabelLookupHandler;
import org.metabrainz.android.api.handler.LabelSearchHandler;
import org.metabrainz.android.api.handler.RatingHandler;
import org.metabrainz.android.api.handler.RecordingLookupHandler;
import org.metabrainz.android.api.handler.RecordingSearchHandler;
import org.metabrainz.android.api.handler.ReleaseGroupBrowseHandler;
import org.metabrainz.android.api.handler.ReleaseGroupLookupHandler;
import org.metabrainz.android.api.handler.ReleaseGroupSearchHandler;
import org.metabrainz.android.api.handler.ReleaseLookupHandler;
import org.metabrainz.android.api.handler.ReleaseInfoHandler;
import org.metabrainz.android.api.handler.TagHandler;
import org.metabrainz.android.api.handler.UserDataHandler;
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

    public LinkedList<ReleaseInfo> parseReleaseGroupReleases(InputStream stream) throws IOException {
        ReleaseInfoHandler handler = new ReleaseInfoHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public Artist parseArtist(InputStream stream) throws IOException {
        ArtistLookupHandler handler = new ArtistLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public ArrayList<ReleaseGroupInfo> parseReleaseGroupBrowse(InputStream stream) throws IOException {
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

    public LinkedList<ReleaseGroupInfo> parseReleaseGroupSearch(InputStream stream) throws IOException {
        ReleaseGroupSearchHandler handler = new ReleaseGroupSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<ReleaseInfo> parseReleaseSearch(InputStream stream) throws IOException {
        ReleaseInfoHandler handler = new ReleaseInfoHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<LabelSearchResult> parseLabelSearch(InputStream stream) throws IOException {
        LabelSearchHandler handler = new LabelSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<RecordingInfo> parseRecordingSearch(InputStream stream) throws IOException {
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

    public LinkedList<UserCollectionInfo> parseCollectionListLookup(InputStream stream) throws IOException {
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

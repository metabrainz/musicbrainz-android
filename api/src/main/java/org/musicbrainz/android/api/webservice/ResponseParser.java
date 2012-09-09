package org.musicbrainz.android.api.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.data.Label;
import org.musicbrainz.android.api.data.LabelSearchStub;
import org.musicbrainz.android.api.data.Recording;
import org.musicbrainz.android.api.data.RecordingStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.handler.ArtistLookupHandler;
import org.musicbrainz.android.api.handler.ArtistSearchHandler;
import org.musicbrainz.android.api.handler.BarcodeSearchHandler;
import org.musicbrainz.android.api.handler.CollectionHandler;
import org.musicbrainz.android.api.handler.CollectionListHandler;
import org.musicbrainz.android.api.handler.LabelLookupHandler;
import org.musicbrainz.android.api.handler.LabelSearchHandler;
import org.musicbrainz.android.api.handler.RatingHandler;
import org.musicbrainz.android.api.handler.RecordingLookupHandler;
import org.musicbrainz.android.api.handler.RecordingSearchHandler;
import org.musicbrainz.android.api.handler.ReleaseGroupBrowseHandler;
import org.musicbrainz.android.api.handler.ReleaseGroupLookupHandler;
import org.musicbrainz.android.api.handler.ReleaseGroupSearchHandler;
import org.musicbrainz.android.api.handler.ReleaseLookupHandler;
import org.musicbrainz.android.api.handler.ReleaseStubHandler;
import org.musicbrainz.android.api.handler.TagHandler;
import org.musicbrainz.android.api.handler.UserDataHandler;
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

    public LinkedList<ReleaseStub> parseReleaseGroupReleases(InputStream stream) throws IOException {
        ReleaseStubHandler handler = new ReleaseStubHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public Artist parseArtist(InputStream stream) throws IOException {
        ArtistLookupHandler handler = new ArtistLookupHandler();
        parse(stream, handler);
        return handler.getResult();
    }

    public ArrayList<ReleaseGroupStub> parseReleaseGroupBrowse(InputStream stream) throws IOException {
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

    public LinkedList<ArtistSearchStub> parseArtistSearch(InputStream stream) throws IOException {
        ArtistSearchHandler handler = new ArtistSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<ReleaseGroupStub> parseReleaseGroupSearch(InputStream stream) throws IOException {
        ReleaseGroupSearchHandler handler = new ReleaseGroupSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<ReleaseStub> parseReleaseSearch(InputStream stream) throws IOException {
        ReleaseStubHandler handler = new ReleaseStubHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<LabelSearchStub> parseLabelSearch(InputStream stream) throws IOException {
        LabelSearchHandler handler = new LabelSearchHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public LinkedList<RecordingStub> parseRecordingSearch(InputStream stream) throws IOException {
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

    public LinkedList<EditorCollectionStub> parseCollectionListLookup(InputStream stream) throws IOException {
        CollectionListHandler handler = new CollectionListHandler();
        parse(stream, handler);
        return handler.getResults();
    }

    public EditorCollection parseCollectionLookup(InputStream stream) throws IOException {
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

package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CollectionHandler extends MBHandler {

    private EditorCollection collection = new EditorCollection();
    private ReleaseStub stub;
    private ArtistNameMbid releaseArtist;

    private boolean inArtist;

    public EditorCollection getCollection() {
        return collection;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("collection")) {
            collection.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("editor")) {
            buildString();
        } else if (localName.equals("release-list")) {
            collection.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("release")) {
            stub = new ReleaseStub();
            stub.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("date")) {
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("sort-name")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("name") && !inArtist) {
            collection.setName(getString());
        } else if (localName.equals("name")) {
            releaseArtist.setName(getString());
            stub.addArtist(releaseArtist);
        } else if (localName.equals("editor")) {
            collection.setEditor(getString());
        } else if (localName.equals("release")) {
            collection.addRelease(stub);
        } else if (localName.equals("title")) {
            stub.setTitle(getString());
        } else if (localName.equals("date")) {
            stub.setDate(getString());
        } else if (localName.equals("country")) {
            stub.setCountryCode(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("sort-name")) {
            releaseArtist.setSortName(getString());
        }
    }

}

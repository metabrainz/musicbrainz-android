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

        if (localName.equalsIgnoreCase("collection")) {
            collection.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name")) {
            buildString();
        } else if (localName.equalsIgnoreCase("editor")) {
            buildString();
        } else if (localName.equalsIgnoreCase("release-list")) {
            collection.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("release")) {
            stub = new ReleaseStub();
            stub.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("date")) {
            buildString();
        } else if (localName.equalsIgnoreCase("country")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("sort-name")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name") && !inArtist) {
            collection.setName(getString());
        } else if (localName.equalsIgnoreCase("name")) {
            releaseArtist.setName(getString());
            stub.addArtist(releaseArtist);
        } else if (localName.equalsIgnoreCase("editor")) {
            collection.setEditor(getString());
        } else if (localName.equalsIgnoreCase("release")) {
            collection.addRelease(stub);
        } else if (localName.equalsIgnoreCase("title")) {
            stub.setTitle(getString());
        } else if (localName.equalsIgnoreCase("date")) {
            stub.setDate(getString());
        } else if (localName.equalsIgnoreCase("country")) {
            stub.setCountryCode(getString());
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        } else if (localName.equalsIgnoreCase("sort-name")) {
            releaseArtist.setSortName(getString());
        }
    }

}

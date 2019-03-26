package org.metabrainz.mobile.api.handler;

import org.metabrainz.mobile.api.data.obsolete.ReleaseArtist;
import org.metabrainz.mobile.api.data.obsolete.UserCollection;
import org.metabrainz.mobile.api.data.obsolete.ReleaseSearchResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CollectionHandler extends MBHandler {

    private UserCollection collection = new UserCollection();
    private ReleaseSearchResult release;
    private ReleaseArtist releaseArtist;

    private boolean inArtist;

    public UserCollection getCollection() {
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
            release = new ReleaseSearchResult();
            release.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("date")) {
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ReleaseArtist();
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
            release.addArtist(releaseArtist);
        } else if (localName.equals("editor")) {
            collection.setEditor(getString());
        } else if (localName.equals("release")) {
            collection.addRelease(release);
        } else if (localName.equals("title")) {
            release.setTitle(getString());
        } else if (localName.equals("date")) {
            release.setDate(getString());
        } else if (localName.equals("country")) {
            release.setCountryCode(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("sort-name")) {
            releaseArtist.setSortName(getString());
        }
    }

}

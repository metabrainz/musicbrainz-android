package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupSearchHandler extends MBHandler {

    private LinkedList<ReleaseGroupStub> stubs = new LinkedList<ReleaseGroupStub>();
    private ReleaseGroupStub rg;
    private ArtistNameMbid releaseArtist;

    private boolean inArtist;

    public LinkedList<ReleaseGroupStub> getResults() {
        return stubs;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("release-group")) {
            rg = new ReleaseGroupStub();
            rg.setMbid(atts.getValue("id"));
            rg.setType(atts.getValue("type"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            buildString();
        } else if (localName.equalsIgnoreCase("release")) {
            rg.addReleaseMbid(atts.getValue("id"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("release-group")) {
            stubs.add(rg);
        } else if (localName.equalsIgnoreCase("title")) {
            rg.setTitle(getString());
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            releaseArtist.setName(getString());
            rg.addArtist(releaseArtist);
        }
    }

}

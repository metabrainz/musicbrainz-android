package org.metabrainz.mobile.api.handler;

import java.util.LinkedList;

import org.metabrainz.mobile.api.data.ReleaseArtist;
import org.metabrainz.mobile.api.data.ReleaseGroupSearchResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupSearchHandler extends MBHandler {

    private LinkedList<ReleaseGroupSearchResult> releaseGroup = new LinkedList<ReleaseGroupSearchResult>();
    private ReleaseGroupSearchResult rg;
    private ReleaseArtist releaseArtist;

    private boolean inArtist;

    public LinkedList<ReleaseGroupSearchResult> getResults() {
        return releaseGroup;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release-group")) {
            rg = new ReleaseGroupSearchResult();
            rg.setMbid(atts.getValue("id"));
            rg.setType(atts.getValue("type"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ReleaseArtist();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name") && inArtist) {
            buildString();
        } else if (localName.equals("release")) {
            rg.addReleaseMbid(atts.getValue("id"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("release-group")) {
            releaseGroup.add(rg);
        } else if (localName.equals("title")) {
            rg.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name") && inArtist) {
            releaseArtist.setName(getString());
            rg.addArtist(releaseArtist);
        }
    }

}

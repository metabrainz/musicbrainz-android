package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseStubHandler extends MBHandler {

    private LinkedList<ReleaseStub> results = new LinkedList<ReleaseStub>();
    private ReleaseStub stub;
    private ArtistNameMbid releaseArtist;

    private boolean inArtist;
    private boolean inLabel;
    private boolean inMedium;

    public LinkedList<ReleaseStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release")) {
            stub = new ReleaseStub();
            stub.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("date")) {
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("track-list")) {
            String num = atts.getValue("count");
            int tracks = Integer.parseInt(num);
            stub.setTracksNum(stub.getTracksNum() + tracks);
        } else if (localName.equals("label")) {
            inLabel = true;
        } else if (localName.equals("format")) {
            buildString();
        } else if (localName.equals("medium")) {
            inMedium = true;
        } else if (localName.equals("sort-name")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("release")) {
            results.add(stub);
        } else if (localName.equals("title") && !inMedium) {
            stub.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name") && inArtist) {
            releaseArtist.setName(getString());
            stub.addArtist(releaseArtist);
        } else if (localName.equals("name") && inLabel) {
            stub.addLabel(getString());
        } else if (localName.equals("date")) {
            stub.setDate(getString());
        } else if (localName.equals("country")) {
            stub.setCountryCode(getString().toUpperCase());
        } else if (localName.equals("label")) {
            inLabel = false;
        } else if (localName.equals("format")) {
            stub.addFormat(getString());
        } else if (localName.equals("medium")) {
            inMedium = false;
        } else if (localName.equals("sort-name") && inArtist) {
            releaseArtist.setSortName(getString());
        }
    }

}

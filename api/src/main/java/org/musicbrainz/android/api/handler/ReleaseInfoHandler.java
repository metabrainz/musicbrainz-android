package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseInfoHandler extends MBHandler {

    private LinkedList<ReleaseInfo> results = new LinkedList<ReleaseInfo>();
    private ReleaseInfo release;
    private ReleaseArtist releaseArtist;

    private boolean inArtist;
    private boolean inLabel;
    private boolean inMedium;

    public LinkedList<ReleaseInfo> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release")) {
            release = new ReleaseInfo();
            release.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ReleaseArtist();
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
            release.setTracksNum(release.getTracksNum() + tracks);
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
            results.add(release);
        } else if (localName.equals("title") && !inMedium) {
            release.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name") && inArtist) {
            releaseArtist.setName(getString());
            release.addArtist(releaseArtist);
        } else if (localName.equals("name") && inLabel) {
            release.addLabel(getString());
        } else if (localName.equals("date")) {
            release.setDate(getString());
        } else if (localName.equals("country")) {
            release.setCountryCode(getString().toUpperCase());
        } else if (localName.equals("label")) {
            inLabel = false;
        } else if (localName.equals("format")) {
            release.addFormat(getString());
        } else if (localName.equals("medium")) {
            inMedium = false;
        } else if (localName.equals("sort-name") && inArtist) {
            releaseArtist.setSortName(getString());
        }
    }

}

package org.musicbrainz.android.api.handler;

import java.util.ArrayList;

import org.musicbrainz.android.api.data.ReleaseGroupInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupBrowseHandler extends MBHandler {

    private ArrayList<ReleaseGroupInfo> results = new ArrayList<ReleaseGroupInfo>();
    private ReleaseGroupInfo releaseGroup;

    private int total = 0;

    public ArrayList<ReleaseGroupInfo> getResults() {
        return results;
    }

    public int getTotal() {
        return total;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release-group")) {
            releaseGroup = new ReleaseGroupInfo();
            String mbid = atts.getValue("id");
            releaseGroup.setMbid(mbid);
            releaseGroup.setType(atts.getValue("type"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("first-release-date")) {
            buildString();
        } else if (localName.equals("release-group-list")) {
            total = Integer.parseInt(atts.getValue("count"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("release-group")) {
            results.add(releaseGroup);
        } else if (localName.equals("title")) {
            releaseGroup.setTitle(getString());
        } else if (localName.equals("first-release-date")) {
            releaseGroup.setFirstRelease(getString());
        }
    }

}

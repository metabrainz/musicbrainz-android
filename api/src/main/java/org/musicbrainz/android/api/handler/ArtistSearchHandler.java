package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArtistSearchHandler extends MBHandler {

    private LinkedList<ArtistSearchResult> results = new LinkedList<ArtistSearchResult>();
    private ArtistSearchResult result;

    private boolean inTag;

    public LinkedList<ArtistSearchResult> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("artist")) {
            result = new ArtistSearchResult();
            result.setMbid(atts.getValue("id"));
        } else if (localName.equals("name") && !inTag) {
            buildString();
        } else if (localName.equals("disambiguation")) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("artist")) {
            addOrIgnoreResult();
        } else if (localName.equals("name") && !inTag) {
            result.setName(getString());
        } else if (localName.equals("disambiguation")) {
            result.setDisambiguation(getString());
        } else if (localName.equals("tag")) {
            inTag = false;
        }
    }

    private void addOrIgnoreResult() {
        for (String id : Artist.SPECIAL_PURPOSE) {
            if (result.getMbid().equals(id)) {
                return;
            }
        }
        results.add(result);
    }

}

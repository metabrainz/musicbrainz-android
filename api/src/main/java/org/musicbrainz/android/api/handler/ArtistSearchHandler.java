package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArtistSearchHandler extends MBHandler {

    private LinkedList<ArtistSearchStub> results = new LinkedList<ArtistSearchStub>();
    private ArtistSearchStub stub;

    private boolean inTag;

    public LinkedList<ArtistSearchStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("artist")) {
            stub = new ArtistSearchStub();
            stub.setMbid(atts.getValue("id"));
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
            stub.setName(getString());
        } else if (localName.equals("disambiguation")) {
            stub.setDisambiguation(getString());
        } else if (localName.equals("tag")) {
            inTag = false;
        }
    }

    private void addOrIgnoreResult() {
        for (String id : Artist.SPECIAL_PURPOSE) {
            if (stub.getMbid().equals(id)) {
                return;
            }
        }
        results.add(stub);
    }

}

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

        if (localName.equalsIgnoreCase("artist")) {
            stub = new ArtistSearchStub();
            stub.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name") && !inTag) {
            buildString();
        } else if (localName.equalsIgnoreCase("disambiguation")) {
            buildString();
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("artist")) {
            addOrIgnoreResult();
        } else if (localName.equalsIgnoreCase("name") && !inTag) {
            stub.setName(getString());
        } else if (localName.equalsIgnoreCase("disambiguation")) {
            stub.setDisambiguation(getString());
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = false;
        }
    }

    private void addOrIgnoreResult() {
        for (String id : Artist.SPECIAL_PURPOSE) {
            if (stub.getMbid().equalsIgnoreCase(id)) {
                return;
            }
        }
        results.add(stub);
    }

}

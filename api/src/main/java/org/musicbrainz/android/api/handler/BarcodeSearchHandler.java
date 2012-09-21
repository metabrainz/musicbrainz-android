package org.musicbrainz.android.api.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BarcodeSearchHandler extends MBHandler {

    private String mbid;

    public String getMbid() {
        return mbid;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release")) {
            mbid = atts.getValue("id");
        }
    }

}

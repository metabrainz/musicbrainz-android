package org.musicbrainz.android.api.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RatingHandler extends MBHandler {

    private float rating = 0;

    public float getRating() {
        return rating;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("rating")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("rating")) {
            float value = Float.parseFloat(getString());
            rating = value;
        }
    }

}

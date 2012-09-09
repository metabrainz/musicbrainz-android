package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.UserData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class UserDataHandler extends MBHandler {

    private UserData data = new UserData();

    public UserData getResult() {
        return data;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("user-tag")) {
            buildString();
        } else if (localName.equals("user-rating")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("user-tag")) {
            data.addTag(getString());
        } else if (localName.equals("user-rating")) {
            float rating = Float.parseFloat(getString());
            data.setRating(rating);
        }
    }

}

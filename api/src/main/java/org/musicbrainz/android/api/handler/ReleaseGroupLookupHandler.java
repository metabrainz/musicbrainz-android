package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.ReleaseGroup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupLookupHandler extends MBHandler {

    private ReleaseGroup releaseGroup = new ReleaseGroup();

    public ReleaseGroup getResult() {
        return releaseGroup;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("")) {

        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("")) {

        }
    }

}

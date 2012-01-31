package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.EditorCollection;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CollectionHandler extends MBHandler {

    private EditorCollection collection = new EditorCollection();

    public EditorCollection getCollection() {
        return collection;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("collection")) {
            collection.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("editor")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("release-list")) {
            collection.setCount(Integer.parseInt(atts.getValue("count")));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name")) {
            collection.setName(sb.toString());
        } else if (localName.equalsIgnoreCase("editor")) {
            collection.setEditor(sb.toString());
        }
    }

}

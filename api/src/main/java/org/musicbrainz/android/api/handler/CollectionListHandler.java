package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.UserCollectionInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CollectionListHandler extends MBHandler {

    private LinkedList<UserCollectionInfo> collections = new LinkedList<UserCollectionInfo>();
    private UserCollectionInfo editorCollection;

    public LinkedList<UserCollectionInfo> getResults() {
        return collections;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("collection")) {
            editorCollection = new UserCollectionInfo();
            editorCollection.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("editor")) {
            buildString();
        } else if (localName.equals("release-list")) {
            editorCollection.setCount(Integer.parseInt(atts.getValue("count")));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("name")) {
            editorCollection.setName(getString());
        } else if (localName.equals("editor")) {
            editorCollection.setEditor(getString());
        } else if (localName.equals("collection")) {
            collections.add(editorCollection);
        }
    }

}

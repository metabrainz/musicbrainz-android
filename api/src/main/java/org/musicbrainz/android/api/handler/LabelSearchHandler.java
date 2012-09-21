package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.LabelSearchStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LabelSearchHandler extends MBHandler {
    
    private LinkedList<LabelSearchStub> results = new LinkedList<LabelSearchStub>();
    private LabelSearchStub stub;
    
    private boolean inTag;

    public LinkedList<LabelSearchStub> getResults() {
        return results;
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("label")) {
            stub = new LabelSearchStub();
            stub.setMbid(atts.getValue("id"));
        } else if (localName.equals("name") && !inTag) {
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
        }
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("label")) {
            results.add(stub);
        } else if (localName.equals("name") && !inTag) {
            stub.setName(getString());
        } else if (localName.equals("country")) {
            stub.setCountry(getString());
        } else if (localName.equals("tag")) {
            inTag = true;
        }
    }

}

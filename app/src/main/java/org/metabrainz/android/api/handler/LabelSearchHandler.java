package org.metabrainz.android.api.handler;

import java.util.LinkedList;

import org.metabrainz.android.api.data.LabelSearchResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LabelSearchHandler extends MBHandler {
    
    private LinkedList<LabelSearchResult> results = new LinkedList<LabelSearchResult>();
    private LabelSearchResult result;
    
    private boolean inTag;

    public LinkedList<LabelSearchResult> getResults() {
        return results;
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("label")) {
            result = new LabelSearchResult();
            result.setMbid(atts.getValue("id"));
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
            results.add(result);
        } else if (localName.equals("name") && !inTag) {
            result.setName(getString());
        } else if (localName.equals("country")) {
            result.setCountry(getString());
        } else if (localName.equals("tag")) {
            inTag = true;
        }
    }

}

package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.Label;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class LabelLookupHandler extends MBHandler {
    
    private boolean inTag;
    private boolean inUrlRelations;
    
    private Label label = new Label();
    private Tag tag;
    private WebLink link;

    public Label getResult() {
        return label;
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("label")) {
            label.setMbid(atts.getValue("id"));
        } else if (localName.equals("name") && !inTag) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("rating")) {
            label.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("begin")) {
            buildString();
        } else if (localName.equals("end")) {
            buildString();
        } else if (localName.equals("relation-list")) {
            if (atts.getValue("target-type").equals("url")) {
                inUrlRelations = true;
            }
        } else if (localName.equals("relation") && inUrlRelations) {
            buildString();
            link = new WebLink();
            link.setType(atts.getValue("type"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("name")) {
            if (!inTag) {
                label.setName(getString());    
            } else {
                tag.setText(getString());
                label.addTag(tag);
            }
        } else if (localName.equals("tag")) {
            inTag = false;
        } else if (localName.equals("rating")) {
            label.setRating(Float.parseFloat(getString()));
        } else if (localName.equals("country")) {
            label.setCountry(getString());
        } else if (localName.equals("begin")) {
            label.setStart(getString());
        } else if (localName.equals("end")) {
            label.setEnd(getString());
        } else if (localName.equals("relation") && inUrlRelations) {
            link.setUrl(getString());
            label.addLink(link);
        } else if (localName.equals("relations-list")) {
            inUrlRelations = false;
        }
    }

}

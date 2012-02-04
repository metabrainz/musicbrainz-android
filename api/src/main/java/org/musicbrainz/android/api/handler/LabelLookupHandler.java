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

        if (localName.equalsIgnoreCase("label")) {
            label.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name") && !inTag) {
            buildString();
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = true;
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("rating")) {
            label.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equalsIgnoreCase("country")) {
            buildString();
        } else if (localName.equalsIgnoreCase("begin")) {
            buildString();
        } else if (localName.equalsIgnoreCase("end")) {
            buildString();
        } else if (localName.equalsIgnoreCase("relation-list")) {
            if (atts.getValue("target-type").equalsIgnoreCase("url")) {
                inUrlRelations = true;
            }
        } else if (localName.equalsIgnoreCase("relation") && inUrlRelations) {
            buildString();
            link = new WebLink();
            link.setType(atts.getValue("type"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name")) {
            if (!inTag) {
                label.setName(getString());    
            } else {
                tag.setText(getString());
                label.addTag(tag);
            }
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = false;
        } else if (localName.equalsIgnoreCase("rating")) {
            label.setRating(Float.parseFloat(getString()));
        } else if (localName.equalsIgnoreCase("country")) {
            label.setCountry(getString());
        } else if (localName.equalsIgnoreCase("begin")) {
            label.setStart(getString());
        } else if (localName.equalsIgnoreCase("end")) {
            label.setEnd(getString());
        } else if (localName.equals("relation") && inUrlRelations) {
            link.setUrl(getString());
            label.addLink(link);
        } else if (localName.equals("relations-list")) {
            inUrlRelations = false;
        }
    }

}

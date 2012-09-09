package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArtistLookupHandler extends MBHandler {

    private boolean inTag;
    private boolean inUrlRelations;
    private boolean inArtistRelations;
    private boolean inLabelRelations;
    private boolean inLifeSpan;

    private Artist artist = new Artist();
    private Tag tag;
    private WebLink link;

    public Artist getResult() {
        return artist;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("artist") && !inArtistRelations) {
            artist.setMbid(atts.getValue("id"));
            artist.setType(atts.getValue("type"));
        } else if (localName.equalsIgnoreCase("name")) {
            buildString();
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = true;
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("rating")) {
            artist.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equalsIgnoreCase("country")) {
            buildString();
        } else if (localName.equalsIgnoreCase("relation") && inUrlRelations) {
            buildString();
            link = new WebLink();
            link.setType(atts.getValue("type"));
        } else if (localName.equalsIgnoreCase("target")) {
            buildString();
        } else if (localName.equalsIgnoreCase("life-span")) {
            inLifeSpan = true;
        } else if (localName.equalsIgnoreCase("begin")) {
            buildString();
        } else if (localName.equalsIgnoreCase("end")) {
            buildString();
        } else if (localName.equalsIgnoreCase("relation-list")) {
            setRelationStatus(atts.getValue("target-type"));
        }
    }

    private void setRelationStatus(String type) {
        if (type.equalsIgnoreCase("url")) {
            inUrlRelations = true;
        } else if (type.equalsIgnoreCase("artist")) {
            inArtistRelations = true;
        } else if (type.equalsIgnoreCase("label")) {
            inLabelRelations = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name")) {
            if (inTag) {
                tag.setText(getString());
                artist.addTag(tag);
            } else if (!inArtistRelations && !inLabelRelations) {
                artist.setName(getString());
            }
        } else if (localName.equalsIgnoreCase("rating")) {
            artist.setRating(Float.parseFloat(getString()));
        } else if (localName.equalsIgnoreCase("country")) {
            artist.setCountry(getString());
        } else if (localName.equalsIgnoreCase("relation") && inUrlRelations) {
            artist.addLink(link);
        } else if (localName.equalsIgnoreCase("target") && inUrlRelations) {
            link.setUrl(getString());
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = false;
        } else if (localName.equalsIgnoreCase("life-span")) {
            inLifeSpan = false;
        } else if (localName.equalsIgnoreCase("begin") && inLifeSpan) {
            artist.setBegin(getString());
        } else if (localName.equalsIgnoreCase("end") && inLifeSpan) {
            artist.setEnd(getString());
        } else if (localName.equalsIgnoreCase("relation-list")) {
            inUrlRelations = false;
            inArtistRelations = false;
            inLabelRelations = false;
        }
    }

}
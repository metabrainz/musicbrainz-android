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
    private boolean inUnsupported;

    private Artist artist = new Artist();
    private Tag tag;
    private WebLink link;

    public Artist getResult() {
        return artist;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("artist") && !inArtistRelations) {
            artist.setMbid(atts.getValue("id"));
            artist.setType(atts.getValue("type"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("rating")) {
            artist.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("relation") && inUrlRelations) {
            buildString();
            link = new WebLink();
            link.setType(atts.getValue("type"));
        } else if (localName.equals("target")) {
            buildString();
        } else if (localName.equals("life-span")) {
            inLifeSpan = true;
        } else if (localName.equals("begin")) {
            buildString();
        } else if (localName.equals("end")) {
            buildString();
        } else if (localName.equals("relation-list")) {
            setRelationStatus(atts.getValue("target-type"));
        } else if (inUnsupported(localName)) {
            inUnsupported = true;
        }
    }

    private void setRelationStatus(String type) {
        if (type.equals("url")) {
            inUrlRelations = true;
        } else if (type.equals("artist")) {
            inArtistRelations = true;
        } else if (type.equals("label")) {
            inLabelRelations = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("name")) {
            if (inTag) {
                tag.setText(getString());
                artist.addTag(tag);
            } else if (!inArtistRelations && !inLabelRelations && !inUnsupported) {
                artist.setName(getString());
            }
        } else if (localName.equals("rating")) {
            artist.setRating(Float.parseFloat(getString()));
        } else if (localName.equals("country")) {
            artist.setCountry(getString());
        } else if (localName.equals("relation") && inUrlRelations) {
            artist.addLink(link);
        } else if (localName.equals("target") && inUrlRelations) {
            link.setUrl(getString());
        } else if (localName.equals("tag")) {
            inTag = false;
        } else if (localName.equals("life-span")) {
            inLifeSpan = false;
        } else if (localName.equals("begin") && inLifeSpan) {
            artist.setBegin(getString());
        } else if (localName.equals("end") && inLifeSpan) {
            artist.setEnd(getString());
        } else if (localName.equals("relation-list")) {
            inUrlRelations = false;
            inArtistRelations = false;
            inLabelRelations = false;
        } else if (inUnsupported(localName)) {
            inUnsupported = false;
        }
    }

    private boolean inUnsupported(String tag) {
        return tag.equals("area") || tag.equals("begin-area") || tag.equals("end-area");
    }

}
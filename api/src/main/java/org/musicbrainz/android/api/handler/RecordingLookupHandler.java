package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.Recording;
import org.musicbrainz.android.api.data.Tag;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecordingLookupHandler extends MBHandler {
    
    private Recording recording = new Recording();
    private ArtistNameMbid artist;
    private Tag tag;
    
    private boolean inArtist;
    private boolean inTags;

    public Recording getResult() {
        return recording;
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("recording")) {
            recording.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("length")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            artist = new ArtistNameMbid();
            artist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name")) {
            buildString();
        } else if (localName.equalsIgnoreCase("tag-list")) {
            inTags = true;
        } else if (localName.equalsIgnoreCase("tag") && !inArtist) {
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("rating")) {
            recording.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("title")) {
            recording.setTitle(getString());
        } else if (localName.equalsIgnoreCase("length")) {
            recording.setLength(Integer.parseInt(getString()));
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        } else if (localName.equalsIgnoreCase("name") && !inTags) {
            artist.setName(getString());
            recording.addArtist(artist);
        } else if (localName.equalsIgnoreCase("tag-list")) {
            inTags = false;
        } else if (localName.equalsIgnoreCase("tag") && !inArtist) {
            tag.setText(getString());
            recording.addTag(tag);
        } else if (localName.equalsIgnoreCase("rating")) {
            recording.setRating(Float.parseFloat(getString()));
        }
    }

}

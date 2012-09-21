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

        if (localName.equals("recording")) {
            recording.setMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("length")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            artist = new ArtistNameMbid();
            artist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("tag-list")) {
            inTags = true;
        } else if (localName.equals("tag") && !inArtist) {
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("rating")) {
            recording.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("title")) {
            recording.setTitle(getString());
        } else if (localName.equals("length")) {
            recording.setLength(Integer.parseInt(getString()));
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name") && !inTags) {
            artist.setName(getString());
            recording.addArtist(artist);
        } else if (localName.equals("tag-list")) {
            inTags = false;
        } else if (localName.equals("tag") && !inArtist) {
            tag.setText(getString());
            recording.addTag(tag);
        } else if (localName.equals("rating")) {
            recording.setRating(Float.parseFloat(getString()));
        }
    }

}

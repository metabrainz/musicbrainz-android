package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupLookupHandler extends MBHandler {

    private ReleaseGroup releaseGroup = new ReleaseGroup();
    private ArtistNameMbid artist;
    private Tag tag;
    private WebLink link;

    private boolean inArtist;
    private boolean inTag;
    
    public ReleaseGroup getResult() {
        return releaseGroup;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release-group")) {
            releaseGroup.setMbid(atts.getValue("id"));
            releaseGroup.setType(atts.getValue("type"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("first-release-date")) {
            buildString();
        } else if (localName.equals("artist")) {
            artist = new ArtistNameMbid();
            artist.setMbid(atts.getValue("id"));
            inArtist = true;
        } else if (localName.equals("rating")) {
            releaseGroup.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("relation")) {
            link = new WebLink();
            link.setType(atts.getValue("type"));
        } else if (localName.equals("target")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("title")) {
            releaseGroup.setTitle(getString());
        } else if (localName.equals("first-release-date")) {
            releaseGroup.setFirstRelease(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name")) {
            if (inArtist) {
                artist.setName(getString());
                releaseGroup.addArtist(artist);
            } else if (inTag) {
                tag.setText(getString());
                releaseGroup.addTag(tag);
            }
        } else if (localName.equals("rating")) {
            releaseGroup.setRating(Float.parseFloat(getString()));
        } else if (localName.equals("tag")) {
            inTag = false;
        } else if (localName.equals("target")) {
            link.setUrl(getString());
            releaseGroup.addLink(link);
        }
    }

}

package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.Track;
import org.musicbrainz.android.api.data.Tag;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseLookupHandler extends MBHandler {

    private boolean inArtist;
    private boolean inLabel;
    private boolean inReleaseGroup;
    private boolean inTrack;
    private boolean inRecording;
    private boolean inTag;
    private boolean inRelationships;
    private boolean inMedium;

    private Release release = new Release();
    private ArtistNameMbid releaseArtist;
    private Track track;
    private Tag tag;

    public Release getResult() {
        return release;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("release")) {
            release.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("status")) {
            buildString();
        } else if (localName.equalsIgnoreCase("barcode")) {
            buildString();
        } else if (localName.equalsIgnoreCase("asin")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name")) {
            buildString();
        } else if (localName.equalsIgnoreCase("date")) {
            buildString();
        } else if (localName.equalsIgnoreCase("label")) {
            inLabel = true;
        } else if (localName.equalsIgnoreCase("release-group")) {
            inReleaseGroup = true;
            release.setReleaseGroupMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("track")) {
            inTrack = true;
            track = new Track();
        } else if (localName.equalsIgnoreCase("recording")) {
            inRecording = true;
            track.setRecordingMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("position")) {
            buildString();
        } else if (localName.equalsIgnoreCase("length")) {
            buildString();
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = true;
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("rating") && inReleaseGroup) {
            release.setReleaseGroupRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equalsIgnoreCase("relation-list")) {
            inRelationships = true;
        } else if (localName.equalsIgnoreCase("medium")) {
            inMedium = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("title")) {
            if (inTrack && !inRecording) {
                track.setTitle(getString());
            } else if (inRecording) {
                if (track.getTitle() == null)
                    track.setTitle(getString());
            } else if (!inMedium) {
                release.setTitle(getString());
            }
        } else if (localName.equalsIgnoreCase("status")) {
            release.setStatus(getString());
        } else if (localName.equalsIgnoreCase("barcode")) {
            release.setBarcode(getString());
        } else if (localName.equalsIgnoreCase("asin")) {
            release.setAsin(getString());
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        } else if (localName.equalsIgnoreCase("name")) {
            if (inArtist && !inTag && !inRelationships) {
                releaseArtist.setName(getString());
                release.addArtist(releaseArtist);
            } else if (inLabel && !inTag) {
                release.addLabel(getString());
            } else if (inReleaseGroup && inTag) {
                tag.setText(getString());
                release.addReleaseGroupTag(tag);
            }
        } else if (localName.equalsIgnoreCase("date")) {
            release.setDate(getString());
        } else if (localName.equalsIgnoreCase("label")) {
            inLabel = false;
        } else if (localName.equalsIgnoreCase("release-group")) {
            inReleaseGroup = false;
        } else if (localName.equalsIgnoreCase("track")) {
            inTrack = false;
            release.addTrack(track);
        } else if (localName.equalsIgnoreCase("recording")) {
            inRecording = false;
        } else if (localName.equalsIgnoreCase("position") && inTrack) {
            track.setPosition(Integer.parseInt(getString()));
        } else if (localName.equalsIgnoreCase("length") && inTrack) {
            track.setDuration(Integer.parseInt(getString()));
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = false;
        } else if (localName.equalsIgnoreCase("rating") && inReleaseGroup) {
            release.setReleaseGroupRating(Float.parseFloat(getString()));
        } else if (localName.equalsIgnoreCase("relation-list")) {
            inRelationships = false;
        } else if (localName.equalsIgnoreCase("medium")) {
            inMedium = false;
        }
    }

}

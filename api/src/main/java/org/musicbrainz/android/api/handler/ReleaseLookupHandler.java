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

        if (localName.equals("release")) {
            release.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("status")) {
            buildString();
        } else if (localName.equals("barcode")) {
            buildString();
        } else if (localName.equals("asin")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("date")) {
            buildString();
        } else if (localName.equals("label")) {
            inLabel = true;
        } else if (localName.equals("release-group")) {
            inReleaseGroup = true;
            release.setReleaseGroupMbid(atts.getValue("id"));
        } else if (localName.equals("track")) {
            inTrack = true;
            track = new Track();
        } else if (localName.equals("recording")) {
            inRecording = true;
            track.setRecordingMbid(atts.getValue("id"));
        } else if (localName.equals("position")) {
            buildString();
        } else if (localName.equals("length")) {
            buildString();
        } else if (localName.equals("tag")) {
            inTag = true;
            buildString();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equals("rating") && inReleaseGroup) {
            release.setReleaseGroupRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            buildString();
        } else if (localName.equals("relation-list")) {
            inRelationships = true;
        } else if (localName.equals("medium")) {
            inMedium = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("title")) {
            if (inTrack && !inRecording) {
                track.setTitle(getString());
            } else if (inRecording) {
                if (track.getTitle() == null)
                    track.setTitle(getString());
            } else if (!inMedium) {
                release.setTitle(getString());
            }
        } else if (localName.equals("status")) {
            release.setStatus(getString());
        } else if (localName.equals("barcode")) {
            release.setBarcode(getString());
        } else if (localName.equals("asin")) {
            release.setAsin(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name")) {
            if (inArtist && !inTag && !inRelationships) {
                releaseArtist.setName(getString());
                release.addArtist(releaseArtist);
            } else if (inLabel && !inTag) {
                release.addLabel(getString());
            } else if (inReleaseGroup && inTag) {
                tag.setText(getString());
                release.addReleaseGroupTag(tag);
            }
        } else if (localName.equals("date")) {
            release.setDate(getString());
        } else if (localName.equals("label")) {
            inLabel = false;
        } else if (localName.equals("release-group")) {
            inReleaseGroup = false;
        } else if (localName.equals("track")) {
            inTrack = false;
            release.addTrack(track);
        } else if (localName.equals("recording")) {
            inRecording = false;
        } else if (localName.equals("position") && inTrack) {
            track.setPosition(Integer.parseInt(getString()));
        } else if (localName.equals("length") && inTrack) {
            track.setDuration(Integer.parseInt(getString()));
        } else if (localName.equals("tag")) {
            inTag = false;
        } else if (localName.equals("rating") && inReleaseGroup) {
            release.setReleaseGroupRating(Float.parseFloat(getString()));
        } else if (localName.equals("relation-list")) {
            inRelationships = false;
        } else if (localName.equals("medium")) {
            inMedium = false;
        }
    }

}

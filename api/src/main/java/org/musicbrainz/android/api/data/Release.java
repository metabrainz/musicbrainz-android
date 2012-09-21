package org.musicbrainz.android.api.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Release {

    private String mbid;
    private String releaseGroupMbid;
    private String barcode;
    private String asin;
    private ArrayList<ReleaseArtist> artists = new ArrayList<ReleaseArtist>();

    private String title;
    private String status;
    private String date;

    private float releaseGroupRating;
    private int releaseGroupRatingCount;
    private List<Tag> releaseGroupTags = new LinkedList<Tag>();

    private Collection<String> labels = new LinkedList<String>();
    private ArrayList<Track> tracks = new ArrayList<Track>();

    public String getMbid() {
        return mbid;
    }

    public void setReleaseMbid(String releaseMbid) {
        this.mbid = releaseMbid;
    }

    public String getReleaseGroupMbid() {
        return releaseGroupMbid;
    }

    public void setReleaseGroupMbid(String releaseGroupMbid) {
        this.releaseGroupMbid = releaseGroupMbid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public ArrayList<ReleaseArtist> getArtists() {
        return artists;
    }

    public void addArtist(ReleaseArtist artist) {
        artists.add(artist);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getReleaseGroupRating() {
        return releaseGroupRating;
    }

    public void setReleaseGroupRating(float releaseGroupRating) {
        this.releaseGroupRating = releaseGroupRating;
    }

    public int getReleaseGroupRatingCount() {
        return releaseGroupRatingCount;
    }

    public void setReleaseGroupRatingCount(int releaseGroupRatingCount) {
        this.releaseGroupRatingCount = releaseGroupRatingCount;
    }

    public List<Tag> getReleaseGroupTags() {
        Collections.sort(releaseGroupTags);
        return releaseGroupTags;
    }

    public void setReleaseGroupTags(List<Tag> releaseGroupTags) {
        this.releaseGroupTags = releaseGroupTags;
    }
    
    public void addReleaseGroupTag(Tag tag) {
        releaseGroupTags.add(tag);
    }

    public Collection<String> getLabels() {
        return labels;
    }

    public void setLabels(LinkedList<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public ArrayList<Track> getTrackList() {
        return tracks;
    }

    public void setTrackList(ArrayList<Track> trackList) {
        this.tracks = trackList;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

}

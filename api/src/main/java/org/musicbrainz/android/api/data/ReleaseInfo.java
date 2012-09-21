package org.musicbrainz.android.api.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Partial release data to differentiate between similar releases (e.g. part of
 * the same release group).
 */
public class ReleaseInfo implements Comparable<ReleaseInfo> {

    private String releaseMbid;

    private String title;
    private ArrayList<ReleaseArtist> artists = new ArrayList<ReleaseArtist>();
    private String date;
    private int tracksNum;
    private String countryCode;
    private Collection<String> labels = new LinkedList<String>();
    private Collection<String> formats = new LinkedList<String>();

    public String getReleaseMbid() {
        return releaseMbid;
    }

    public void setReleaseMbid(String releaseMbid) {
        this.releaseMbid = releaseMbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ReleaseArtist> getArtists() {
        return artists;
    }

    public void addArtist(ReleaseArtist artist) {
        artists.add(artist);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getTracksNum() {
        return tracksNum;
    }

    public void setTracksNum(int tracks) {
        this.tracksNum = tracks;
    }

    public Collection<String> getLabels() {
        return labels;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public Collection<String> getFormats() {
        return formats;
    }

    public void addFormat(String format) {
        formats.add(format);
    }

    @Override
    public int compareTo(ReleaseInfo another) {
        int artistNameComparison = getArtists().get(0).compareTo(another.getArtists().get(0));
        if (artistNameComparison != 0) {
            return artistNameComparison;
        }

        if (getDate() == null && another.getDate() == null) {
            return 0;
        } else if (getDate() == null) {
            return 1;
        } else if (another.getDate() == null) {
            return -1;
        } else {
            return getDate().compareTo(another.getDate());
        }
    }

}

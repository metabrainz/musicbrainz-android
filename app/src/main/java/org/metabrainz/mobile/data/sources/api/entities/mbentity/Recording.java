package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.ReleaseEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class Recording {
    @SerializedName("id")
    private String mbid;
    private String title;
    private String disambiguation;
    private long length;

    //TODO: Implement correct wrapper JSON
    @SerializedName("artist-credit")
    private ArrayList<ArtistCredit> artistCredits = new ArrayList<>();
    private ArrayList<Release> releases = new ArrayList<>();
    @SerializedName("track-count")
    private int trackCount;
    private ArrayList<Media> media = new ArrayList<>();
    @SerializedName("release-group")
    private ArrayList<ReleaseGroup> releaseGroups = new ArrayList<>();
    @SerializedName("release-events")
    private ArrayList<ReleaseEvent> releaseEvents = new ArrayList<>();


    @Override
    public String toString() {
        return "Recording{" +
                "mbid='" + mbid + '\'' +
                ", name='" + title + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", length=" + length +
                ", artistCredits=" + artistCredits +
                ", releases=" + releases +
                ", trackCount=" + trackCount +
                ", media=" + media +
                ", releaseGroups=" + releaseGroups +
                ", releaseEvents=" + releaseEvents +
                '}';
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getdisambiguation() {
        return disambiguation;
    }

    public void setdisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public ArrayList<ArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(ArrayList<ArtistCredit> artistCredits) {
        this.artistCredits = artistCredits;
    }

    public ArrayList<Release> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<Release> releases) {
        this.releases = releases;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public ArrayList<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(ArrayList<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }

    public ArrayList<ReleaseEvent> getReleaseEvents() {
        return releaseEvents;
    }

    public void setReleaseEvents(ArrayList<ReleaseEvent> releaseEvents) {
        this.releaseEvents = releaseEvents;
    }

    public String getDisplayArtist() {
        StringBuilder builder = new StringBuilder();
        Iterator<ArtistCredit> iterator = artistCredits.iterator();
        while (iterator.hasNext()) {
            ArtistCredit credit = iterator.next();
            builder.append(credit.getName());
            if (iterator.hasNext())
                builder.append(credit.getJoinphrase());
        }
        return builder.toString();
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getDuration() {
        StringBuilder builder = new StringBuilder();
        long seconds = length / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        builder.append(minutes).append(':');
        if (seconds < 10) builder.append('0');
        builder.append(seconds);
        return builder.toString();
    }
}

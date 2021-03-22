package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.ArrayList;
import java.util.List;

public class Recording extends MBEntity {
    private String title;
    private long length;

    @SerializedName("artist-credit")
    private final List<ArtistCredit> artistCredits = new ArrayList<>();
    @SerializedName("track-count")
    private int trackCount;
    private int score;

    private final List<Link> relations = new ArrayList<>();
    private final List<Release> releases = new ArrayList<>();

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases.addAll(releases);
    }

    public List<Link> getRelations() {
        return relations;
    }

    public void setRelations(List<Link> relations) {
        this.relations.addAll(relations);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public List<ArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(List<ArtistCredit> artistCredits) {
        this.artistCredits.addAll(artistCredits);
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
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

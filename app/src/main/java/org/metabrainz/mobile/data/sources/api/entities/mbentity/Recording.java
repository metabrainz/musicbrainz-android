package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;

import java.util.ArrayList;
import java.util.Iterator;

public class Recording extends MBEntity {
    private String title;
    private long length;

    @SerializedName("artist-credit")
    private ArrayList<ArtistCredit> artistCredits = new ArrayList<>();
    private ArrayList<Release> releases = new ArrayList<>();
    @SerializedName("track-count")
    private int trackCount;
    private int score;

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

    public String getDisplayArtist() {
        StringBuilder builder = new StringBuilder();
        Iterator<ArtistCredit> iterator = artistCredits.iterator();
        while (iterator.hasNext()) {
            ArtistCredit credit = iterator.next();
            String name = "";
            if (credit.getName() != null && !credit.getName().isEmpty())
                name = credit.getName();
            else if (credit.getArtist() != null && credit.getArtist().getName() != null
                    && !credit.getArtist().getName().isEmpty())
                name = credit.getArtist().getName();
            builder.append(name);
            if (iterator.hasNext())
                builder.append(credit.getJoinphrase());
        }
        return builder.toString();
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

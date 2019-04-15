package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Media {
    @SerializedName("track-count")
    private int trackCount;
    private String format;
    private String title;
    private int position;
    private List<Track> tracks = new ArrayList<>();

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

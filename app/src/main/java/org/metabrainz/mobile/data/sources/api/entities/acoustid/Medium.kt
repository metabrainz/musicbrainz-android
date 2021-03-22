
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Medium {

    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("track_count")
    @Expose
    private int trackCount;
    @SerializedName("tracks")
    @Expose
    private List<Track> tracks = new ArrayList<>();

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

}

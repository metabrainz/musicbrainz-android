package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

public class Track {
    @SerializedName("id")
    private String mbid;
    private String title;
    private int position;
    private Recording recording;
    private long length;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
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

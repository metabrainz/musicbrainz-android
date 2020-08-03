package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class ListenTrackMetadata {

    @SerializedName("artist_name")
    String artist;

    @SerializedName("track_name")
    String track;

    @SerializedName("release_name")
    String release;

    public String getArtist() {
            return artist;
        }

    public void setArtist(String artist) {
            this.artist = artist;
        }

    public String getTrack() {
            return track;
        }

    public void setTrack(String track) {
            this.track = track;
        }

    public String getRelease() {
            return release;
        }

    public void setRelease(String release) {
            this.release = release;
        }

    @NotNull
    @Override
    public String toString() {
        return "ListenTrackMetadata{" +
                "artist='" + artist + '\'' +
                ", track='" + track + '\'' +
                ", release='" + release + '\'' +
                '}';
    }
}


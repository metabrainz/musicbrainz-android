package org.metabrainz.mobile.api.data.search.entity;

public class ArtistCredit {
    private String name;
    private String joinphrase;
    private Artist artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinphrase() {
        return joinphrase;
    }

    public void setJoinphrase(String joinphrase) {
        this.joinphrase = joinphrase;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}

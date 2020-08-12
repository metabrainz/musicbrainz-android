package org.metabrainz.mobile.data.sources.api.entities;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistCredit)) return false;
        ArtistCredit that = (ArtistCredit) o;
        return getName().equals(that.getName()) &&
                getJoinphrase().equals(that.getJoinphrase()) &&
                getArtist().equals(that.getArtist());
    }

}

package org.musicbrainz.android.api.data;

/**
 * Artist name and MBID pair for release.
 */
public class ArtistNameMbid {

    private String mbid;
    private String name;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
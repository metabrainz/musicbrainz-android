package org.musicbrainz.android.api.data;

/**
 * Partial artist data to select between similar artists in a list of search
 * results.
 */
public class ArtistSearchResult {

    private String mbid;
    private String name;
    private String disambiguation;

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

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

}

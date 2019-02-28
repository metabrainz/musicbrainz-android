package org.metabrainz.mobile.api.data;

import com.google.gson.annotations.SerializedName;

/**
 * Partial artist data to select between similar artists in a list of search
 * results.
 */
public class ArtistSearchResult {

    @SerializedName("id")
    private String mbid;
    private String name;
    private String disambiguation;

    @Override
    public String toString() {
        return "ArtistSearchResult{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}';
    }

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

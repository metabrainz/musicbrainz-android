
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Track {

    @SerializedName("artists")
    @Expose
    private List<Artist> artists = new ArrayList<>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("position")
    @Expose
    private int position;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}

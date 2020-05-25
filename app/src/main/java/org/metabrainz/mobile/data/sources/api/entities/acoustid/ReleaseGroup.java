
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroup {

    @SerializedName("artists")
    @Expose
    private List<Artist> artists = new ArrayList<>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("releases")
    @Expose
    private List<Release> releases = new ArrayList<>();
    @SerializedName("secondarytypes")
    @Expose
    private List<String> secondarytypes = new ArrayList<>();
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;

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

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    public List<String> getSecondarytypes() {
        return secondarytypes;
    }

    public void setSecondarytypes(List<String> secondarytypes) {
        this.secondarytypes = secondarytypes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

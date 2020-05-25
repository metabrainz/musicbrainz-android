
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recording {

    @SerializedName("artists")
    @Expose
    private List<Artist> artists = new ArrayList<>();
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("releasegroups")
    @Expose
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();
    @SerializedName("title")
    @Expose
    private String title;

    private int sources;

    public int getSources() {
        return sources;
    }

    public void setSources(int sources) {
        this.sources = sources;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

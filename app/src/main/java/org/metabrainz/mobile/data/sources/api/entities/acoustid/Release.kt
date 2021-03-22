
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Release {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("medium_count")
    @Expose
    private int mediumCount;
    @SerializedName("mediums")
    @Expose
    private List<Medium> mediums = new ArrayList<>();
    @SerializedName("releaseEvents")
    @Expose
    private List<ReleaseEvent> releaseEvents = new ArrayList<>();
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("track_count")
    @Expose
    private int trackCount;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(int mediumCount) {
        this.mediumCount = mediumCount;
    }

    public List<Medium> getMediums() {
        return mediums;
    }

    public void setMediums(List<Medium> mediums) {
        this.mediums = mediums;
    }

    public List<ReleaseEvent> getReleaseEvents() {
        return releaseEvents;
    }

    public void setReleaseEvents(List<ReleaseEvent> releaseEvents) {
        this.releaseEvents = releaseEvents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

}

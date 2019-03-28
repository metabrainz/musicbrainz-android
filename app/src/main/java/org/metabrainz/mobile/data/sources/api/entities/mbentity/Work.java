package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

public class Work {
    @SerializedName("id")
    private String mbid;
    private String title;
    //TODO: Implement Relations

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

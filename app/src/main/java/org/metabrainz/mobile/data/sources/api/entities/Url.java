package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

public class Url {
    @SerializedName("id")
    private String mbid;
    private String resource;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}

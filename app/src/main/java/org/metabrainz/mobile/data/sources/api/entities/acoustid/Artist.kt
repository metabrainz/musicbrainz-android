
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("joinphrase")
    @Expose
    private String joinphrase;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoinphrase() {
        return joinphrase;
    }

    public void setJoinphrase(String joinphrase) {
        this.joinphrase = joinphrase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

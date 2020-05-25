
package org.metabrainz.mobile.data.sources.api.entities.acoustid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("recordings")
    @Expose
    private List<Recording> recordings = new ArrayList<>();
    @SerializedName("score")
    @Expose
    private double score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}

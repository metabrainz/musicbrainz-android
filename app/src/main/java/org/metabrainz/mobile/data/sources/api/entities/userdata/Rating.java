package org.metabrainz.mobile.data.sources.api.entities.userdata;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("votes-count")
    private
    int count;

    private float value;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

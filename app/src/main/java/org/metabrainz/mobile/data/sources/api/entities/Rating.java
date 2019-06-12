package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("votes-count")
    int count;

    float value;
}

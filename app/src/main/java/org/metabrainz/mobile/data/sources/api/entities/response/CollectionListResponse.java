package org.metabrainz.mobile.data.sources.api.entities.response;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionListResponse {

    @SerializedName("collection-offset")
    private
    String offset;

    @SerializedName("collection-limit")
    private
    String limit;

    private List<Collection> collections = new ArrayList<>();

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}

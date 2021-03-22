package org.metabrainz.mobile.data.sources.api.entities.response;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;

import java.util.List;

public class SearchResponse<T extends MBEntity> {
    @SerializedName("created")
    private String timestamp;
    private int count;
    private int offset;
    private List<T> items;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

package org.metabrainz.mobile.api.data.search.response;

import com.google.gson.annotations.SerializedName;

public class JSONResponse {
    @SerializedName("created")
    private String timestamp;
    private int count;
    private int offset;

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
}

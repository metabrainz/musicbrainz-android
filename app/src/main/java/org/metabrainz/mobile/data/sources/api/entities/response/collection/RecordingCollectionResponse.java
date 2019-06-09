package org.metabrainz.mobile.data.sources.api.entities.response.collection;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

import java.util.ArrayList;
import java.util.List;

public class RecordingCollectionResponse {

    @SerializedName("recording-count")
    public int count;

    @SerializedName("recording-offset")
    public int offset;

    public List<Recording> recordings = new ArrayList<>();

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

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }
}

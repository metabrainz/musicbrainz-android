package org.metabrainz.mobile.data.sources.api.entities.response.collection;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroupCollectionResponse {

    @SerializedName("release-group-count")
    private int count;

    @SerializedName("release-group-offset")
    private int offset;

    @SerializedName("release-groups")
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();

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

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }
}

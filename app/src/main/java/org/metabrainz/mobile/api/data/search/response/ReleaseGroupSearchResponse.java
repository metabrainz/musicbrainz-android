package org.metabrainz.mobile.api.data.search.response;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.api.data.search.entity.ReleaseGroup;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroupSearchResponse extends JSONResponse {
    @SerializedName("release-groups")
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }
}

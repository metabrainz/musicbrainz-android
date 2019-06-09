package org.metabrainz.mobile.data.sources.api.entities.response.search;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroupSearchResponse extends JSONSearchResponse {
    @SerializedName("release-groups")
    private List<ReleaseGroup> releaseGroups = new ArrayList<>();

    public List<ReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<ReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }
}

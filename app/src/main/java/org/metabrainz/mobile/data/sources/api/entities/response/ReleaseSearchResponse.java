package org.metabrainz.mobile.data.sources.api.entities.response;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

public class ReleaseSearchResponse extends JSONResponse {

    private List<Release> releases;

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }
}

package org.metabrainz.mobile.data.sources.api.entities.response;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;

public class BarcodeReleaseResponse {

    private String created;
    private int count;
    private int offset;
    private List<Release> releases = new ArrayList<>();

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }
}

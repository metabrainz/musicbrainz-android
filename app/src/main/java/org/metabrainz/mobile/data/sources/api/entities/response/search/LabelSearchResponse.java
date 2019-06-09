package org.metabrainz.mobile.data.sources.api.entities.response.search;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;

import java.util.List;

public class LabelSearchResponse extends JSONSearchResponse {

    private List<Label> labels;

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}

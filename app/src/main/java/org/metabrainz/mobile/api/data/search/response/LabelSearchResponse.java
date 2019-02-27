package org.metabrainz.mobile.api.data.search.response;

import org.metabrainz.mobile.api.data.search.entity.Label;

import java.util.List;

public class LabelSearchResponse extends JSONResponse {

    private List<Label> labels;

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}

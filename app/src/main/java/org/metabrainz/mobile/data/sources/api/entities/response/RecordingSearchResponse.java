package org.metabrainz.mobile.data.sources.api.entities.response;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

import java.util.List;

public class RecordingSearchResponse extends SearchResponse {

    private List<Recording> recordings;

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }
}

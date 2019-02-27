package org.metabrainz.mobile.api.data.search.response;

import org.metabrainz.mobile.api.data.search.entity.Recording;

import java.util.List;

public class RecordingSearchResponse extends JSONResponse {

    private List<Recording> recordings;

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }
}

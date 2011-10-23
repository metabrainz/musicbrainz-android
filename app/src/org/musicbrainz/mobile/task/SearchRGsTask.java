package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.SearchActivity;

public class SearchRGsTask extends SearchTask {
    
    private LinkedList<ReleaseGroupStub> results;
    
    public SearchRGsTask(SearchActivity activity) {
        super(activity);
    }
    
    @Override
    protected Void run(String... term) throws Exception {
        WebClient client = new WebClient(userAgent);
        results = client.searchReleaseGroup(term[0]);
        return null;
    }

    public LinkedList<ReleaseGroupStub> getReleaseGroupResults() {
        return results;
    }

}

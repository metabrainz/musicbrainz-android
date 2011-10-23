package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.SearchActivity;

public class SearchAllTask extends IgnitedAsyncTask<SearchActivity, String, Void, Void> {

    String userAgent;
    LinkedList<ArtistStub> artistResults;
    LinkedList<ReleaseGroupStub> releaseGroupResults;
    
    public SearchAllTask(SearchActivity activity) {
        super(activity);
    }
    
    @Override
    protected void onStart(SearchActivity activity) {
        userAgent = activity.getUserAgent();
    }

    @Override
    protected Void run(String... term) throws Exception {
        WebClient client = new WebClient(userAgent);
        artistResults = client.searchArtists(term[0]);
        releaseGroupResults = client.searchReleaseGroup(term[0]);
        return null;
    }

    @Override
    protected void onSuccess(SearchActivity activity, Void v) {
        completed(activity);
    }

    @Override
    protected void onError(SearchActivity activity, Exception e) {
        completed(activity);
    }
    
    private void completed(SearchActivity activity) {
        if (activity != null) {
            activity.onSearchTaskFinished();
        }
    }
    
    public LinkedList<ArtistStub> getArtistResults() {
        return artistResults;
    }
    
    public LinkedList<ReleaseGroupStub> getReleaseGroupResults() {
        return releaseGroupResults;
    }
    
}

package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.SearchActivity;

public class SearchAllTask extends SearchTask {

    private LinkedList<ArtistStub> artistResults;
    private LinkedList<ReleaseGroupStub> releaseGroupResults;
    
    public SearchAllTask(SearchActivity activity) {
        super(activity);
    }

    @Override
    protected Void run(String... term) throws Exception {
        WebClient client = new WebClient(userAgent);
        artistResults = client.searchArtists(term[0]);
        releaseGroupResults = client.searchReleaseGroup(term[0]);
        return null;
    }
    
    public LinkedList<ArtistStub> getArtistResults() {
        return artistResults;
    }
    
    public LinkedList<ReleaseGroupStub> getReleaseGroupResults() {
        return releaseGroupResults;
    }
    
}

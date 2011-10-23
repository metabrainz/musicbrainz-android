package org.musicbrainz.mobile.task;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.SearchActivity;

public class SearchArtistsTask extends SearchTask {

    private LinkedList<ArtistStub> results;

    public SearchArtistsTask(SearchActivity activity) {
        super(activity);
    }

    @Override
    protected Void run(String... term) throws Exception {
        WebClient client = new WebClient(userAgent);
        results = client.searchArtists(term[0]);
        return null;
    }
    
    public LinkedList<ArtistStub> getArtistResults() {
        return results;
    }
    
}

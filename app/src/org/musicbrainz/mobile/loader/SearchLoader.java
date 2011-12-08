package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SearchLoader extends AsyncTaskLoader<AsyncResult<SearchResults>> {
    
    private String userAgent;
    private String term;

    public SearchLoader(Context context, String userAgent, String term) {
        super(context);
        this.userAgent = userAgent;
        this.term = term;
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<SearchResults> loadInBackground() {
        try {
            WebClient client = new WebClient(userAgent);
            SearchResults results = new SearchResults(client.searchArtists(term), client.searchReleaseGroup(term));
            return new AsyncResult<SearchResults>(LoaderStatus.SUCCESS, results);
        } catch (IOException e) {
            return new AsyncResult<SearchResults>(LoaderStatus.EXCEPTION, e);
        }
    }

}

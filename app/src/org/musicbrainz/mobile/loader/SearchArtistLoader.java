package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.loader.SearchResults.SearchType;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SearchArtistLoader extends AsyncTaskLoader<AsyncResult<SearchResults>> {

    private String userAgent;
    private String term;

    public SearchArtistLoader(Context context, String userAgent, String searchTerm) {
        super(context);
        this.userAgent = userAgent;
        this.term = searchTerm;
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
            SearchResults results = new SearchResults(SearchType.ARTIST, client.searchArtists(term));
            return new AsyncResult<SearchResults>(LoaderStatus.SUCCESS, results);
        } catch (IOException e) {
            return new AsyncResult<SearchResults>(LoaderStatus.EXCEPTION, e);
        }
    }
    
}

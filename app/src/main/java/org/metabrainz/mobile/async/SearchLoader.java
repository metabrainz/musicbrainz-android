package org.metabrainz.mobile.async;

import java.io.IOException;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;
import org.metabrainz.mobile.async.result.SearchResults;

public class SearchLoader extends PersistingAsyncTaskLoader<AsyncResult<SearchResults>> {

    private MusicBrainz client;
    private String term;

    public SearchLoader(String term) {
        client = App.getWebClient();
        this.term = term;
    }

    @Override
    public AsyncResult<SearchResults> loadInBackground() {
        try {
            SearchResults results = new SearchResults(client.searchArtist(term), client.searchReleaseGroup(term));
            data = new AsyncResult<SearchResults>(LoaderStatus.SUCCESS, results);
            return data;
        } catch (IOException e) {
            return new AsyncResult<SearchResults>(LoaderStatus.EXCEPTION, e);
        }
    }
}

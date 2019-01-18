package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;
import org.metabrainz.android.async.result.SearchResults;
import org.metabrainz.android.async.result.SearchResults.SearchType;

public class SearchArtistLoader extends PersistingAsyncTaskLoader<AsyncResult<SearchResults>> {

    private MusicBrainz client;
    private String term;

    public SearchArtistLoader(String term) {
        client = App.getWebClient();
        this.term = term;
    }

    @Override
    public AsyncResult<SearchResults> loadInBackground() {
        try {
            SearchResults results = new SearchResults(SearchType.ARTIST, client.searchArtist(term));
            data = new AsyncResult<SearchResults>(LoaderStatus.SUCCESS, results);
            return data;
        } catch (IOException e) {
            return new AsyncResult<SearchResults>(LoaderStatus.EXCEPTION, e);
        }
    }
}

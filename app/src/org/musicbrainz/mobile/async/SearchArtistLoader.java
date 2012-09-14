package org.musicbrainz.mobile.async;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;
import org.musicbrainz.mobile.async.result.SearchResults;
import org.musicbrainz.mobile.async.result.SearchResults.SearchType;

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

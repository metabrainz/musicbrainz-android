package org.metabrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.data.obsolete.ReleaseSearchResult;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

public class SearchReleaseLoader extends PersistingAsyncTaskLoader<AsyncResult<List<ReleaseSearchResult>>> {

    private MusicBrainz client;
    private String term;

    public SearchReleaseLoader(String term) {
        client = App.getWebClient();
        this.term = term;
    }

    @Override
    public AsyncResult<List<ReleaseSearchResult>> loadInBackground() {
        try {
            data = new AsyncResult<List<ReleaseSearchResult>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseSearchResult>>(LoaderStatus.EXCEPTION, e);
        }
    }
}

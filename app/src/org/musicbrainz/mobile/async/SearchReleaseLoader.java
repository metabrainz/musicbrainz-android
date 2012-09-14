package org.musicbrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

public class SearchReleaseLoader extends PersistingAsyncTaskLoader<AsyncResult<List<ReleaseStub>>> {

    private MusicBrainz client;
    private String term;

    public SearchReleaseLoader(String term) {
        client = App.getWebClient();
        this.term = term;
    }

    @Override
    public AsyncResult<List<ReleaseStub>> loadInBackground() {
        try {
            data = new AsyncResult<List<ReleaseStub>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }
}

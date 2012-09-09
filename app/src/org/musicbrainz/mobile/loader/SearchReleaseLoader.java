package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

public class SearchReleaseLoader extends PersistingAsyncTaskLoader<AsyncResult<List<ReleaseStub>>> {

    private String term;

    public SearchReleaseLoader(String term) {
        this.term = term;
    }

    @Override
    public AsyncResult<List<ReleaseStub>> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(App.getUserAgent());
            data = new AsyncResult<List<ReleaseStub>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }
}

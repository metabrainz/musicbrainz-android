package org.metabrainz.android.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.data.ReleaseInfo;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

public class SearchReleaseLoader extends PersistingAsyncTaskLoader<AsyncResult<List<ReleaseInfo>>> {

    private MusicBrainz client;
    private String term;

    public SearchReleaseLoader(String term) {
        client = App.getWebClient();
        this.term = term;
    }

    @Override
    public AsyncResult<List<ReleaseInfo>> loadInBackground() {
        try {
            data = new AsyncResult<List<ReleaseInfo>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseInfo>>(LoaderStatus.EXCEPTION, e);
        }
    }
}

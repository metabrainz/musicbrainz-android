package org.metabrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.data.ReleaseSearchResult;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

public class ReleaseGroupReleasesLoader extends AsyncTaskLoader<AsyncResult<List<ReleaseSearchResult>>> {

    private MusicBrainz client;
    private String mbid;

    public ReleaseGroupReleasesLoader(String mbid) {
        super(App.getContext());
        client = App.getWebClient();
        this.mbid = mbid;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<List<ReleaseSearchResult>> loadInBackground() {
        try {
            return new AsyncResult<List<ReleaseSearchResult>>(LoaderStatus.SUCCESS, client.browseReleases(mbid));
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseSearchResult>>(LoaderStatus.EXCEPTION, e);
        }
    }

}

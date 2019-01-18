package org.metabrainz.android.async;

import java.io.IOException;
import java.util.List;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.data.ReleaseInfo;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

public class ReleaseGroupReleasesLoader extends AsyncTaskLoader<AsyncResult<List<ReleaseInfo>>> {

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
    public AsyncResult<List<ReleaseInfo>> loadInBackground() {
        try {
            return new AsyncResult<List<ReleaseInfo>>(LoaderStatus.SUCCESS, client.browseReleases(mbid));
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseInfo>>(LoaderStatus.EXCEPTION, e);
        }
    }

}

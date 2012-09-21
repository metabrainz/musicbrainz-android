package org.musicbrainz.mobile.async;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

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

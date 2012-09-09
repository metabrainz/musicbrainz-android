package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.List;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class ReleaseGroupStubsLoader extends AsyncTaskLoader<AsyncResult<List<ReleaseStub>>> {

    private String mbid;

    public ReleaseGroupStubsLoader(String mbid) {
        super(App.getContext());
        this.mbid = mbid;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<List<ReleaseStub>> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(App.getUserAgent());
            return new AsyncResult<List<ReleaseStub>>(LoaderStatus.SUCCESS, client.browseReleases(mbid));
        } catch (IOException e) {
            return new AsyncResult<List<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}

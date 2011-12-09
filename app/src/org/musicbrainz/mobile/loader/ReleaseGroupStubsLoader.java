package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ReleaseGroupStubsLoader extends AsyncTaskLoader<AsyncResult<LinkedList<ReleaseStub>>> {

    private String userAgent;
    private String mbid;

    public ReleaseGroupStubsLoader(Context context, String userAgent, String mbid) {
        super(context);
        this.userAgent = userAgent;
        this.mbid = mbid;
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<LinkedList<ReleaseStub>> loadInBackground() {
        try {
            WebClient client = new WebClient(userAgent);
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.SUCCESS, client.browseReleases(mbid));
        } catch (IOException e) {
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

}

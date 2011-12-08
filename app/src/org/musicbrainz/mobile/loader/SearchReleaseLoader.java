package org.musicbrainz.mobile.loader;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SearchReleaseLoader extends AsyncTaskLoader<AsyncResult<LinkedList<ReleaseStub>>> {

    private String userAgent;
    private String term;

    AsyncResult<LinkedList<ReleaseStub>> data;

    public SearchReleaseLoader(Context context, String userAgent, String term) {
        super(context);
        this.userAgent = userAgent;
        this.term = term;
    }

    @Override
    public AsyncResult<LinkedList<ReleaseStub>> loadInBackground() {
        try {
            WebClient client = new WebClient(userAgent);
            data = new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.SUCCESS, client.searchRelease(term));
            return data;
        } catch (IOException e) {
            return new AsyncResult<LinkedList<ReleaseStub>>(LoaderStatus.EXCEPTION, e);
        }
    }

    @Override
    public void deliverResult(AsyncResult<LinkedList<ReleaseStub>> data) {
        if (isReset()) {
            return;
        }
        this.data = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }
        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        data = null;
    }

}

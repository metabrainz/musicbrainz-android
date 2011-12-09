package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ReleaseLoader extends AsyncTaskLoader<AsyncEntityResult<Release>> {
    
    private Credentials creds;
    private String userAgent;
    private String mbid;
    
    private AsyncEntityResult<Release> data;

    public ReleaseLoader(Context context, Credentials creds, String mbid) {
        super(context);
        this.creds = creds;
        this.mbid = mbid;
    }
    
    public ReleaseLoader(Context context, String userAgent, String mbid) {
        super(context);
        this.userAgent = userAgent;
        this.mbid = mbid;
    }

    @Override
    public AsyncEntityResult<Release> loadInBackground() {
        try {
            return getAvailableData();
        } catch (Exception e) {
            return new AsyncEntityResult<Release>(LoaderStatus.EXCEPTION, e);
        }
    }

    private AsyncEntityResult<Release> getAvailableData() throws IOException {
        if (creds == null) {
            return getRelease();
        } else {
            return getReleaseWithUserData();
        }
    }

    private AsyncEntityResult<Release> getRelease() throws IOException {
        WebClient client = new WebClient(userAgent);
        Release release = client.lookupRelease(mbid);
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, release);
        return data;
    }
    
    private AsyncEntityResult<Release> getReleaseWithUserData() throws IOException {
        WebClient client = new WebClient(creds);
        Release release = client.lookupRelease(mbid);
        UserData userData = client.getUserData(MBEntity.RELEASE_GROUP, mbid);
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, release, userData);
        return data;
    }
    
    @Override
    public void deliverResult(AsyncEntityResult<Release> data) {
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

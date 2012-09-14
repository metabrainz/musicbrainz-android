package org.musicbrainz.mobile.async;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.SimpleUser;
import org.musicbrainz.android.api.User;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class LoginLoader extends AsyncTaskLoader<AsyncResult<Boolean>> {

    private String username;
    private String password;

    public LoginLoader(Context appContext, String username, String password) {
        super(App.getContext());
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<Boolean> loadInBackground() {
        User credentials = new SimpleUser(username, password);
        MusicBrainz client = new MusicBrainzWebClient(credentials, App.getUserAgent(), App.getClientId());
        try {
            return new AsyncResult<Boolean>(LoaderStatus.SUCCESS, client.autenticateCredentials());
        } catch (IOException e) {
            return new AsyncResult<Boolean>(LoaderStatus.EXCEPTION, e);
        }
    }

}

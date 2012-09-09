package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class LoginLoader extends AsyncTaskLoader<AsyncResult<Boolean>> {

    private String username;
    private String password;

    public LoginLoader(Context appContext, String username, String password) {
        super(MusicBrainzApp.getContext());
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
        Credentials creds = new Credentials(MusicBrainzApp.getUserAgent(), username, password, MusicBrainzApp.getClientId());
        MusicBrainz client = new MusicBrainzWebClient(creds);
        try {
            return new AsyncResult<Boolean>(LoaderStatus.SUCCESS, client.autenticateUserCredentials());
        } catch (IOException e) {
            return new AsyncResult<Boolean>(LoaderStatus.EXCEPTION, e);
        }
    }

}

package org.metabrainz.android.async;

import java.io.IOException;

import org.metabrainz.android.api.MusicBrainz;
import org.metabrainz.android.api.SimpleUser;
import org.metabrainz.android.api.User;
import org.metabrainz.android.api.webservice.MusicBrainzWebClient;
import org.metabrainz.android.App;
import org.metabrainz.android.async.result.AsyncResult;
import org.metabrainz.android.async.result.LoaderStatus;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

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

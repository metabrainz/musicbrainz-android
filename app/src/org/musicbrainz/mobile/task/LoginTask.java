package org.musicbrainz.mobile.task;

import java.io.IOException;

import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.activity.LoginActivity.AuthenticationTask;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.util.Secrets;
import org.musicbrainz.mobile.util.SimpleEncrypt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;

public class LoginTask extends IgnitedAsyncTask<LoginActivity, Void, Void, Boolean> {

    private ProgressDialog pd;

    protected void onStart(LoginActivity context) {
        pd = new ProgressDialog(context) {
            public void cancel() {
                super.cancel();
                LoginTask.this.cancel(true);
            }
        };
        pd.setMessage(context.getString(R.string.pd_authenticating));
        pd.setCancelable(true);
        pd.show();
    }

    @Override
    protected Boolean run(Void... params) {

        //WebClient client = new WebClient(username, password, Config.USER_AGENT, getVersion());
        //return client.autenticateUserCredentials();
        return null;
    }

    protected void onSuccess(LoginActivity context, Boolean loginOkay) {

        if (loginOkay) {

        } else {

        }
    }

    @Override
    protected void onError(LoginActivity context, Exception e) {
        
    }

}

package org.metabrainz.mobile.presentation.features.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.util.Log;

public class LoginActivity extends MusicBrainzActivity {
    private LoginViewModel loginViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getAccessTokenLiveData().observe(this, this::saveOAuthToken);

        findViewById(R.id.login_btn).setOnClickListener(view -> startLogin());
    }

    @Override
    protected void onResume() {
        Uri callbackUri = getIntent().getData();
        if (callbackUri != null &&
                callbackUri.toString().startsWith(MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)) {

            String code = callbackUri.getQueryParameter("code");
            if (code != null) loginViewModel.fetchAccessToken(code);

        }
        super.onResume();
    }

    private void startLogin() {
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(MusicBrainzServiceGenerator.AUTH_URL
                            + "?response_type=code"
                            + "&client_id=" + MusicBrainzServiceGenerator.CLIENT_ID
                            + "&redirect_uri=" + MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI
                            + "&scope=profile%20collection"));
            startActivity(intent);
        } else Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
    }

    private void saveOAuthToken(AccessToken accessToken) {
        if (accessToken != null) {

            Log.d(accessToken.getAccessToken());
            Toast.makeText(getApplicationContext(),
                    "Access Token: " + accessToken.getAccessToken(),
                    Toast.LENGTH_LONG).show();

            LoginSharedPreferences.saveOAuthToken(accessToken);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Failed to obtain access token ",
                    Toast.LENGTH_LONG).show();
        }
    }
}
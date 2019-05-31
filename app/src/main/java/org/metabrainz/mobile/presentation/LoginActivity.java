package org.metabrainz.mobile.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.sources.api.LoginService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MusicBrainzActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.login_btn).setOnClickListener(view -> {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(MusicBrainzServiceGenerator.AUTH_URL
                            + "?response_type=code"
                            + "&client_id=" + MusicBrainzServiceGenerator.CLIENT_ID
                            + "&redirect_uri=" + MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI
                            + "&scope=profile%20collection"));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        Uri callbackUri = getIntent().getData();
        if (callbackUri != null &&
                callbackUri.toString().startsWith(MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI)) {
            String code = callbackUri.getQueryParameter("code");
            if (code != null) {
                LoginService musicBrainzService = MusicBrainzServiceGenerator
                        .createService(LoginService.class,
                                LoginSharedPreferences.getPreferences(), false);

                Call<AccessToken> accessTokenCall = musicBrainzService
                        .getAccessToken(code,
                                "authorization_code",
                                MusicBrainzServiceGenerator.CLIENT_ID,
                                MusicBrainzServiceGenerator.CLIENT_SECRET,
                                MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI);

                accessTokenCall.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        AccessToken accessToken = response.body();
                        if (accessToken != null) {
                            Log.d(accessToken.getAccessToken());
                            Toast.makeText(getApplicationContext(),
                                    "Access Token: " + accessToken.getAccessToken(),
                                    Toast.LENGTH_LONG).show();
                            saveOAuthToken(accessToken);

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Failed to obtain access token ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.e(t.getMessage());
                        Toast.makeText(getApplicationContext(),
                                t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        super.onResume();
    }

    private void saveOAuthToken(AccessToken accessToken) {
        LoginSharedPreferences.saveOAuthToken(LoginSharedPreferences.getPreferences(), accessToken);
    }
}
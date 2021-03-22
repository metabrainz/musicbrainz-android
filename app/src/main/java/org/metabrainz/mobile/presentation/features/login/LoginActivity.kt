package org.metabrainz.mobile.presentation.features.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserInfo;
import org.metabrainz.mobile.databinding.ActivityLoginBinding;
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.util.Log;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends MusicBrainzActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getAccessTokenLiveData().observe(this, this::saveOAuthToken);
        loginViewModel.getUserInfoLiveData().observe(this, this::saveUserInfo);

        if(LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN){
            binding.loginPromptId.setText(R.string.logout_prompt);
            binding.loginBtn.setText(R.string.logout);
            binding.loginBtn.setOnClickListener(v -> logoutUser());
        }
        else
            binding.loginBtn.setOnClickListener(v -> startLogin());
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
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(MusicBrainzServiceGenerator.AUTH_BASE_URL
                            + "authorize"
                            + "?response_type=code"
                            + "&client_id=" + MusicBrainzServiceGenerator.CLIENT_ID
                            + "&redirect_uri=" + MusicBrainzServiceGenerator.OAUTH_REDIRECT_URI
                            + "&scope=profile%20collection%20tag%20rating"));
            startActivity(intent);
    }


    private void saveOAuthToken(AccessToken accessToken) {
        if (accessToken != null) {
            Log.d(accessToken.getAccessToken());
            LoginSharedPreferences.saveOAuthToken(accessToken);
            loginViewModel.fetchUserInfo();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Failed to obtain access token ",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserInfo(UserInfo userInfo) {
        if (userInfo != null &&
                LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            LoginSharedPreferences.saveUserInfo(userInfo);
            Toast.makeText(getApplicationContext(),
                    "Login successful. " + userInfo.getUsername() + " is now logged in.",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, DashboardActivity.class));
            Log.d(userInfo.getUsername());
            finish();
        }
    }

    private void logoutUser() {
        LoginSharedPreferences.logoutUser();
        Toast.makeText(getApplicationContext(),
                "User has successfully logged out.",
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_open_website).setVisible(false);
        menu.findItem(R.id.menu_login).setVisible(false);
        return true;
    }

}
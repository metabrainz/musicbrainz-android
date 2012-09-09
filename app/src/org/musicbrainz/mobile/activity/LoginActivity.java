package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.LoginFragment.LoginCallback;

import android.os.Bundle;

public class LoginActivity extends MusicBrainzActivity implements LoginCallback {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onLoggedIn() {
        finish();
    }
    
}
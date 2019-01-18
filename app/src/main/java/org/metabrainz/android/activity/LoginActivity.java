package org.metabrainz.android.activity;

import org.metabrainz.android.R;
import org.metabrainz.android.fragment.LoginFragment.LoginCallback;

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
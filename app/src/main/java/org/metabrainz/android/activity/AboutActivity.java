package org.metabrainz.android.activity;

import org.metabrainz.android.R;

import android.os.Bundle;

import android.view.Menu;

public class AboutActivity extends MusicBrainzActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

}

package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;

import android.os.Bundle;

import com.actionbarsherlock.view.Menu;

public class AboutActivity extends MusicBrainzActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

}

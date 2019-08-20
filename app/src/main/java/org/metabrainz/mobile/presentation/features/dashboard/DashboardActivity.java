package org.metabrainz.mobile.presentation.features.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.about.AboutActivity;
import org.metabrainz.mobile.presentation.features.barcode.BarcodeActivity;
import org.metabrainz.mobile.presentation.features.collection.CollectionActivity;
import org.metabrainz.mobile.presentation.features.tagger.TaggerActivity;
import org.metabrainz.mobile.presentation.view.DashTileView;

import java.util.Objects;

public class DashboardActivity extends MusicBrainzActivity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(false);
        setupTiles();
    }

    private void setupTiles() {
        setupTile(R.id.dash_scan, R.drawable.dash_scan, R.string.dash_scan);
        setupTile(R.id.dash_collections, R.drawable.dash_collections, R.string.dash_collections);
        setupTile(R.id.dash_donate, R.drawable.dash_donate, R.string.dash_donate);
        setupTile(R.id.dash_about, R.drawable.dash_about, R.string.dash_about);
        setupTile(R.id.dash_tag, R.drawable.dash_tag, R.string.dash_tag);
    }

    private void setupTile(int tileId, int iconId, int stringId) {
        DashTileView scanTile = findViewById(tileId);
        scanTile.setIcon(iconId);
        scanTile.setText(stringId);
        scanTile.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dash_scan:
                startActivity(new Intent(this, BarcodeActivity.class));
                break;
            case R.id.dash_collections:
                startActivity(new Intent(this, CollectionActivity.class));
                break;
            case R.id.dash_donate:
                startActivity(IntentFactory.getDonate(getApplicationContext()));
                break;
            case R.id.dash_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.dash_tag:
                startActivity(new Intent(this, TaggerActivity.class));
        }
    }

}
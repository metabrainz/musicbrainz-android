package org.metabrainz.mobile.presentation.features.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.databinding.ActivityDashBinding;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.about.AboutActivity;
import org.metabrainz.mobile.presentation.features.barcode.BarcodeActivity;
import org.metabrainz.mobile.presentation.features.collection.CollectionActivity;
import org.metabrainz.mobile.presentation.features.tagger.TaggerActivity;
import org.metabrainz.mobile.presentation.features.taggerkotlin.KotlinTaggerAcitivty;
import org.metabrainz.mobile.presentation.view.DashTileView;

import java.util.Objects;

public class DashboardActivity extends MusicBrainzActivity implements OnClickListener {

    private ActivityDashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(false);
        setupTiles();
    }

    private void setupTiles() {
        setupTile(binding.dashButtons.dashScan, R.drawable.dash_scan, R.string.dash_scan);
        setupTile(binding.dashButtons.dashCollections, R.drawable.dash_collections, R.string.dash_collections);
        setupTile(binding.dashButtons.dashDonate, R.drawable.dash_donate, R.string.dash_donate);
        setupTile(binding.dashButtons.dashAbout, R.drawable.dash_about, R.string.dash_about);
        setupTile(binding.dashButtons.dashTag, R.drawable.dash_tag, R.string.dash_tag);
    }

    private void setupTile(DashTileView scanTile, int iconId, int stringId) {
        scanTile.setIcon(iconId);
        scanTile.setText(stringId);
        scanTile.setOnClickListener(this);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.EMPTY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        menu.findItem(R.id.menu_open_website).setVisible(false);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dash_scan) {
            startActivity(new Intent(this, BarcodeActivity.class));
        } else if (id == R.id.dash_collections) {
            startActivity(new Intent(this, CollectionActivity.class));
        } else if (id == R.id.dash_donate) {
            startActivity(IntentFactory.getDonate(getApplicationContext()));
        } else if (id == R.id.dash_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.dash_tag) {
            startActivity(new Intent(this, KotlinTaggerAcitivty.class));
        }
    }

}
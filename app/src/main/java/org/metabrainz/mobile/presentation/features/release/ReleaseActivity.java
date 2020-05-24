package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;

import java.util.Objects;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ReleaseActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugReleaseInfo";

    private ReleaseViewModel releaseViewModel;

    private String mbid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        releaseViewModel = new ViewModelProvider(this).get(ReleaseViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.RELEASE_MBID);
        if (mbid != null && !mbid.isEmpty()) releaseViewModel.setMBID(mbid);

        releaseViewModel.initializeData().observe(this, this::setRelease);
        releaseViewModel.fetchData();
    }

    private void setRelease(MBEntity entity) {
        if (entity instanceof Release) {
            Release release = (Release) entity;
            //if (release != null) getSupportActionBar().setTitle(release.getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

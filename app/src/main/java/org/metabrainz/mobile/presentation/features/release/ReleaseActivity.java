package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.intent.IntentFactory;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        releaseViewModel = ViewModelProviders.of(this).get(ReleaseViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.RELEASE_MBID);
        if (mbid != null && !mbid.isEmpty()) releaseViewModel.setMBID(mbid);

        releaseViewModel.initializeReleaseData().observe(this, this::setRelease);
        releaseViewModel.getReleaseData();
    }

    private void setRelease(Release release) {
        //if (release != null) getSupportActionBar().setTitle(release.getTitle());
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

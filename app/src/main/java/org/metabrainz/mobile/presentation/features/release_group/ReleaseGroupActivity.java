package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;

import java.util.Objects;

public class ReleaseGroupActivity extends MusicBrainzActivity {


    public static final String LOG_TAG = "DebugReleaseGroupInfo";

    private ReleaseGroupViewModel releaseGroupViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_group);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        releaseGroupViewModel = new ViewModelProvider(this).get(ReleaseGroupViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) releaseGroupViewModel.setMBID(mbid);

        releaseGroupViewModel.getData().observe(this, this::setReleaseGroup);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setReleaseGroup(ReleaseGroup releaseGroup) {
        if (releaseGroup != null)
            Objects.requireNonNull(getSupportActionBar()).setTitle(releaseGroup.getTitle());
    }
}

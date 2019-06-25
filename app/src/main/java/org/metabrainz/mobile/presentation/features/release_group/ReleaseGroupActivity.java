package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

public class ReleaseGroupActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DebugReleaseGroupInfo";

    private ReleaseGroupViewModel releaseGroupViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_group);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        releaseGroupViewModel = ViewModelProviders.of(this).get(ReleaseGroupViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.RELEASE_GROUP);
        if (mbid != null && !mbid.isEmpty()) releaseGroupViewModel.setMBID(mbid);

        releaseGroupViewModel.initializeReleaseGroupData().observe(this, this::setReleaseGroup);
        releaseGroupViewModel.getReleaseGroupData(LoginSharedPreferences.getLoginStatus()
                == LoginSharedPreferences.STATUS_LOGGED_IN);
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
        // if (releaseGroup != null) getSupportActionBar().setTitle(releaseGroup.getName());
    }
}

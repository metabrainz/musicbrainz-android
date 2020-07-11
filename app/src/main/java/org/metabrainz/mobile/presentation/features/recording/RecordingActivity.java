package org.metabrainz.mobile.presentation.features.recording;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.databinding.ActivityRecordingBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;

import java.util.Objects;

public class RecordingActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugRecordingInfo";

    private ActivityRecordingBinding binding;

    private RecordingViewModel recordingViewModel;
    private UserViewModel userViewModel;
    private ReleaseListViewModel releaseListViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recordingViewModel = new ViewModelProvider(this).get(RecordingViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) recordingViewModel.setMBID(mbid);

        recordingViewModel.getData().observe(this, this::setRecording);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecording(Recording recording) {
        if (recording != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(recording.getTitle());
            userViewModel.setUserData(recording);
            if (recording.getReleases() != null)
                releaseListViewModel.setData(recording.getReleases());
        }
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "recording/" + mbid);
    }
}

package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.Constants;
=======
import org.metabrainz.mobile.R;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.databinding.ActivityRecordingBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
<<<<<<< HEAD
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
=======
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

import java.util.Objects;

public class RecordingActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugRecordingInfo";

    private ActivityRecordingBinding binding;

    private RecordingViewModel recordingViewModel;
    private UserViewModel userViewModel;

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

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) recordingViewModel.setMBID(mbid);

<<<<<<< HEAD
<<<<<<< HEAD
        recordingViewModel.getData().observe(this, this::setRecording);
=======
        recordingViewModel.initializeData().observe(this, this::setRecording);
        recordingViewModel.fetchData();
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
        recordingViewModel.getData().observe(this, this::setRecording);
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
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
<<<<<<< HEAD
        if (recording != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(recording.getTitle());
            userViewModel.setUserData(recording);
        }
=======
        if (recording != null)
            Objects.requireNonNull(getSupportActionBar()).setTitle(recording.getTitle());
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }
}

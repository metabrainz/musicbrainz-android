package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;

import java.util.Objects;

public class RecordingActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugRecordingInfo";

    private RecordingViewModel recordingViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recordingViewModel = new ViewModelProvider(this).get(RecordingViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.RECORDING);
        if (mbid != null && !mbid.isEmpty()) recordingViewModel.setMBID(mbid);

        recordingViewModel.initializeData().observe(this, this::setRecording);
        recordingViewModel.fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecording(MBEntity entity) {
        if (entity instanceof Recording) {
            Recording recording = (Recording) entity;
            //if (recording != null) getSupportActionBar().setTitle(recording.getTitle());
        }
    }
}

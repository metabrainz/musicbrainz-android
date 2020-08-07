package org.metabrainz.mobile.presentation.features.recording;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.databinding.ActivityRecordingBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
import org.metabrainz.mobile.util.Resource;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecordingActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugRecordingInfo";

    private ActivityRecordingBinding binding;

    private RecordingViewModel recordingViewModel;
    private UserViewModel userViewModel;
    private LinksViewModel linksViewModel;
    private ReleaseListViewModel releaseListViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        recordingViewModel = new ViewModelProvider(this).get(RecordingViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) recordingViewModel.setMBID(mbid);

        binding.noResult.getRoot().setVisibility(View.GONE);
        binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);
        binding.dataFragments.setVisibility(View.GONE);
        recordingViewModel.getData().observe(this, this::setRecording);
    }

    private void setRecording(Resource<Recording> resource) {
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.dataFragments.setVisibility(View.VISIBLE);

            Recording recording = resource.getData();
            Objects.requireNonNull(getSupportActionBar()).setTitle(recording.getTitle());
            userViewModel.setUserData(recording);
            linksViewModel.setData(recording.getRelations());
            releaseListViewModel.setData(recording.getReleases());
        } else
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "recording/" + mbid);
    }
}

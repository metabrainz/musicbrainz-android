package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.databinding.FragmentRecordingInfoBinding;

import retrofit2.http.HEAD;

public class RecordingInfoFragment extends Fragment {

    private FragmentRecordingInfoBinding binding;
    private RecordingViewModel recordingViewModel;

    public static RecordingInfoFragment newInstance() {
        return new RecordingInfoFragment();
    }

    @Override
<<<<<<< HEAD
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordingInfoBinding.inflate(inflater, container, false);
        recordingViewModel = new ViewModelProvider(requireActivity()).get(RecordingViewModel.class);
        recordingViewModel.getData().observe(getViewLifecycleOwner(), this::setRecordingInfo);
        return binding.getRoot();
=======
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recording_info, container, false);
<<<<<<< HEAD
        recordingViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RecordingViewModel.class);
        recordingViewModel.initializeData().observe(getViewLifecycleOwner(), this::setRecordingInfo);
=======
        recordingViewModel = new ViewModelProvider(requireActivity()).get(RecordingViewModel.class);
        recordingViewModel.getData().observe(getViewLifecycleOwner(), this::setRecordingInfo);
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
        findViews(layout);
        return layout;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecordingInfo(MBEntity entity) {
        if (entity instanceof Recording) {
            Recording recording = (Recording) entity;
            String duration, artist;
            binding.recordingTitle.setText(recording.getTitle());
            duration = recording.getDuration();
            artist = recording.getDisplayArtist();
            if (duration != null) binding.recordingDuration.setText(duration);
            if (artist != null) binding.recordingArtist.setText(artist);
        }
    }
}

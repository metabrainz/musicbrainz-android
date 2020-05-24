package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

public class RecordingUserDataFragment extends UserDataFragment {

    private RecordingViewModel recordingViewModel;

    public static RecordingUserDataFragment newInstance() {
        return new RecordingUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recordingViewModel = new ViewModelProvider(requireActivity()).get(RecordingViewModel.class);
        recordingViewModel.getData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

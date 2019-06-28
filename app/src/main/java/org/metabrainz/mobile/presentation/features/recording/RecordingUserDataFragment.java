package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

import java.util.Objects;

public class RecordingUserDataFragment extends UserDataFragment {

    private RecordingViewModel recordingViewModel;

    public static RecordingUserDataFragment newInstance() {
        return new RecordingUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recordingViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RecordingViewModel.class);
        recordingViewModel.initializeRecordingData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

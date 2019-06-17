package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

public class ReleaseUserDataFragment extends UserDataFragment {

    private ReleaseViewModel releaseViewModel;

    public static ReleaseUserDataFragment newInstance() {
        return new ReleaseUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        releaseViewModel = ViewModelProviders.of(getActivity()).get(ReleaseViewModel.class);
        releaseViewModel.initializeReleaseData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

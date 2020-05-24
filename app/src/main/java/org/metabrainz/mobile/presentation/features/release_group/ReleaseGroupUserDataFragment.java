package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

import java.util.Objects;

public class ReleaseGroupUserDataFragment extends UserDataFragment {

    private ReleaseGroupViewModel releaseGroupViewModel;

    public static ReleaseGroupUserDataFragment newInstance() {
        return new ReleaseGroupUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        releaseGroupViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.initializeData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

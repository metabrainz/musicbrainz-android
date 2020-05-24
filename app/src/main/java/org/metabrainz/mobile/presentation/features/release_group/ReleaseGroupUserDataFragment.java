package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

public class ReleaseGroupUserDataFragment extends UserDataFragment {

    private ReleaseGroupViewModel releaseGroupViewModel;

    public static ReleaseGroupUserDataFragment newInstance() {
        return new ReleaseGroupUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        releaseGroupViewModel = new ViewModelProvider(requireActivity()).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.getData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

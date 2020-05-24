package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

import java.util.Objects;

public class LabelUserDataFragment extends UserDataFragment {

    private LabelViewModel labelViewModel;

    public static LabelUserDataFragment newInstance() {
        return new LabelUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        labelViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(LabelViewModel.class);
        labelViewModel.initializeData().observe(getViewLifecycleOwner(), this::updateData);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

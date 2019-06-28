package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

import java.util.Objects;

public class ArtistUserDataFragment extends UserDataFragment {

    private ArtistViewModel artistViewModel;

    public static ArtistUserDataFragment newInstance() {
        return new ArtistUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        artistViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ArtistViewModel.class);
        artistViewModel.initializeArtistData().observe(getViewLifecycleOwner(), this::updateData);
        View view = inflater.inflate(R.layout.fragment_artist_user_data, container, false);
        bindViews(view);
        return view;
    }
}

package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

public class ArtistUserDataFragment extends UserDataFragment {

    private ArtistViewModel artistViewModel;

    public static ArtistUserDataFragment newInstance() {
        return new ArtistUserDataFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        artistViewModel = new ViewModelProvider(this).get(ArtistViewModel.class);
        artistViewModel.initializeData().observe(getViewLifecycleOwner(), this::updateData);
        View view = inflater.inflate(R.layout.fragment_artist_user_data, container, false);
        bindViews(view);
        return view;
    }
}

package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArtistBioFragment extends Fragment {
    
    public static ArtistBioFragment newInstance() {
        return new ArtistBioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        return layout;
    }
    
}

package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.Track;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseTrackAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TracksFragment extends ContractListFragment<TracksFragment.Callback> {
    
    public static TracksFragment newInstance() {
        return new TracksFragment();
    }
    
    public interface Callback {
        List<Track> getTracks();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracklist, container, false);
    }
    
    public void update() {
        setListAdapter(new ReleaseTrackAdapter(getActivity(), getContract().getTracks()));
    }

}

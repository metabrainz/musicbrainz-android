package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.Track;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseTrackAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TracksFragment extends ContractFragment<TracksFragment.Callback> {
    
    private ListView trackList;

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }
    
    public interface Callback {
        List<Track> getTracks();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tracklist, container, false);
        trackList = (ListView) layout.findViewById(R.id.release_tracks);
        return layout;
    }
    
    public void update() {
        trackList.setAdapter(new ReleaseTrackAdapter(getActivity(), getContract().getTracks()));
        trackList.setDrawSelectorOnTop(false);
    }

}

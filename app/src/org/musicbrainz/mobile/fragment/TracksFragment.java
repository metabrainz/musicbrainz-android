package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.Track;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseTrackAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TracksFragment extends Fragment {
    
    private ListView trackList;

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }
    
    private TracksFragmentCallback callback;
    
    public interface TracksFragmentCallback {
        List<Track> getTracks();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (TracksFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + TracksFragmentCallback.class.getSimpleName());
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tracklist, container, false);
        trackList = (ListView) layout.findViewById(R.id.release_tracks);
        return layout;
    }
    
    public void update() {
        trackList.setAdapter(new ReleaseTrackAdapter(getActivity(), callback.getTracks()));
        trackList.setDrawSelectorOnTop(false);
    }

}

package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseTrackAdapter;
import org.musicbrainz.mobile.fragment.contracts.EntityTab;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TracksFragment extends ListFragment implements EntityTab<Release> {

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracklist, container, false);
    }

    @Override
    public void update(Release release) {
        setListAdapter(new ReleaseTrackAdapter(getActivity(), release.getTrackList()));
    }

}

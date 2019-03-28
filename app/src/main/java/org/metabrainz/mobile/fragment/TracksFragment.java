package org.metabrainz.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.ListFragment;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.ReleaseTrackAdapter;
import org.metabrainz.mobile.api.data.obsolete.Release;
import org.metabrainz.mobile.fragment.contracts.EntityTab;

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

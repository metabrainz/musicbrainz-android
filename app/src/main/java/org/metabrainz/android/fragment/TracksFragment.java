package org.metabrainz.android.fragment;

import org.metabrainz.android.api.data.Release;
import org.metabrainz.android.R;
import org.metabrainz.android.adapter.list.ReleaseTrackAdapter;
import org.metabrainz.android.fragment.contracts.EntityTab;

import android.os.Bundle;
import androidx.fragment.app.ListFragment;
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

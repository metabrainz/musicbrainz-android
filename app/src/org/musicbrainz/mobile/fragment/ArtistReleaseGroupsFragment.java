package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.list.ArtistRGAdapter;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ArtistReleaseGroupsFragment extends ContractListFragment<ArtistReleaseGroupsFragment.Callback> {

    public static ArtistReleaseGroupsFragment newInstance() {
        return new ArtistReleaseGroupsFragment();
    }

    public interface Callback {
        List<ReleaseGroupStub> getReleaseGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_release_groups, container, false);
    }

    public void update() {
        List<ReleaseGroupStub> releaseGroups = getContract().getReleaseGroups();
        setListAdapter(new ArtistRGAdapter(getActivity(), releaseGroups));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ReleaseGroupStub rg = (ReleaseGroupStub) getListAdapter().getItem(position);
        startReleaseActivity(rg.getMbid());
    }

    private void startReleaseActivity(String mbid) {
        Intent releaseIntent = new Intent(App.getContext(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, mbid);
        startActivity(releaseIntent);
    }

}

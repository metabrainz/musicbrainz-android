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
import android.widget.AdapterView;
import android.widget.ListView;

public class ArtistReleaseGroupsFragment extends ContractFragment<ArtistReleaseGroupsFragment.Callback> implements
        ListView.OnItemClickListener {

    private ListView releaseGroupList;
    private List<ReleaseGroupStub> releaseGroups;

    public static ArtistReleaseGroupsFragment newInstance() {
        return new ArtistReleaseGroupsFragment();
    }

    public interface Callback {
        List<ReleaseGroupStub> getReleaseGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_artist_release_groups, container, false);
        releaseGroupList = (ListView) layout.findViewById(R.id.artist_releases);
        return layout;
    }

    public void update() {
        releaseGroups = getContract().getReleaseGroups();
        releaseGroupList.setAdapter(new ArtistRGAdapter(getActivity(), releaseGroups));
        releaseGroupList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ReleaseGroupStub rg = releaseGroups.get(position);
        Intent releaseIntent = new Intent(App.getContext(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, rg.getMbid());
        startActivity(releaseIntent);
    }

}

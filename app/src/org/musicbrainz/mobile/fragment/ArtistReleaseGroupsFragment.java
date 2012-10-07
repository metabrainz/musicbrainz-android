package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ReleaseGroupInfo;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.list.ArtistRGAdapter;
import org.musicbrainz.mobile.fragment.contracts.EntityTab;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ArtistReleaseGroupsFragment extends ListFragment implements EntityTab<Artist> {

    public static ArtistReleaseGroupsFragment newInstance() {
        return new ArtistReleaseGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_release_groups, container, false);
    }

    @Override
    public void update(Artist artist) {
        setListAdapter(new ArtistRGAdapter(getActivity(), artist.getReleaseGroups()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ReleaseGroupInfo rg = (ReleaseGroupInfo) getListAdapter().getItem(position);
        startReleaseActivity(rg.getMbid());
    }

    private void startReleaseActivity(String mbid) {
        Intent releaseIntent = new Intent(App.getContext(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, mbid);
        startActivity(releaseIntent);
    }

}

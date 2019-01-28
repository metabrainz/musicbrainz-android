package org.metabrainz.android.fragment;

import org.metabrainz.android.api.data.Artist;
import org.metabrainz.android.api.data.ReleaseGroupSearchResult;
import org.metabrainz.android.App;
import org.metabrainz.android.R;
import org.metabrainz.android.activity.ReleaseActivity;
import org.metabrainz.android.adapter.list.ArtistRGAdapter;
import org.metabrainz.android.fragment.contracts.EntityTab;
import org.metabrainz.android.intent.IntentFactory.Extra;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.ListFragment;
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
        ReleaseGroupSearchResult rg = (ReleaseGroupSearchResult) getListAdapter().getItem(position);
        startReleaseActivity(rg.getMbid());
    }

    private void startReleaseActivity(String mbid) {
        Intent releaseIntent = new Intent(App.getContext(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, mbid);
        startActivity(releaseIntent);
    }

}

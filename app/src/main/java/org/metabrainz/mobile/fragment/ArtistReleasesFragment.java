package org.metabrainz.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.ReleaseActivity;
import org.metabrainz.mobile.adapter.list.ArtistRGAdapter;
import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.ReleaseGroupSearchResult;
import org.metabrainz.mobile.fragment.contracts.EntityTab;
import org.metabrainz.mobile.intent.IntentFactory.Extra;

public class ArtistReleasesFragment extends ListFragment implements EntityTab<Artist> {

    public static ArtistReleasesFragment newInstance() {
        return new ArtistReleasesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_releases, container, false);
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

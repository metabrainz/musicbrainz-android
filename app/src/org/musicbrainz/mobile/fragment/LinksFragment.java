package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.WebLink;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.WeblinkAdapter;
import org.musicbrainz.mobile.fragment.contracts.EntityTab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LinksFragment extends ListFragment implements EntityTab<Artist> {

    public static LinksFragment newInstance() {
        return new LinksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_links, container, false);
    }
    
    @Override
    public void update(Artist artist) {
        setListAdapter(new WeblinkAdapter(getActivity(), artist.getLinks()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        WebLink link = (WebLink) getListAdapter().getItem(position);
        startBrowserLink(link.getUrl());
    }

    private void startBrowserLink(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}

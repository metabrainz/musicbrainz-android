package org.metabrainz.mobile.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.list.WeblinkAdapter;
import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.WebLink;
import org.metabrainz.mobile.fragment.contracts.EntityTab;

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

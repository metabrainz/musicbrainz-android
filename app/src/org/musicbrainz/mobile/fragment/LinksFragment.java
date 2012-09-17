package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.WebLink;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.WeblinkAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LinksFragment extends ContractListFragment<LinksFragment.Callback> {

    public static LinksFragment newInstance() {
        return new LinksFragment();
    }

    public interface Callback {
        List<WebLink> getLinks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_links, container, false);
    }

    public void update() {
        List<WebLink> links = getContract().getLinks();
        setListAdapter(new WeblinkAdapter(getActivity(), links));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        WebLink link = (WebLink) getListAdapter().getItem(position);
        startBrowserLink(link.getUrl());
    }

    private void startBrowserLink(String url) {
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(urlIntent);
    }

}

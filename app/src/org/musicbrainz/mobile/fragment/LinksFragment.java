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
import android.widget.AdapterView;
import android.widget.ListView;

public class LinksFragment extends ContractFragment<LinksFragment.Callback> implements ListView.OnItemClickListener {

    private ListView linksList;
    private List<WebLink> links;

    public static LinksFragment newInstance() {
        return new LinksFragment();
    }

    public interface Callback {
        List<WebLink> getLinks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_links, container, false);
        linksList = (ListView) layout.findViewById(R.id.artist_links);
        return layout;
    }

    public void update() {
        links = getContract().getLinks();
        linksList.setAdapter(new WeblinkAdapter(getActivity(), links));
        linksList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String link = links.get(position).getUrl();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(urlIntent);
    }

}

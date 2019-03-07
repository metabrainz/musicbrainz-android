package org.metabrainz.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.list.ArtistReleaseAdapter;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistReleasesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistReleaseAdapter adapter;
    private List<Release> releaseList;
    private ArtistViewModel artistViewModel;

    public static ArtistReleasesFragment newInstance() {
        return new ArtistReleasesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        releaseList = new ArrayList<>();
        adapter = new ArtistReleaseAdapter(releaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_releases, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = ViewModelProviders.of(getActivity()).get(ArtistViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(artistViewModel.getArtist() != null && artistViewModel.getArtist().getReleases() != null){
            releaseList.clear();
            releaseList.addAll(artistViewModel.getArtist().getReleases());
            adapter.notifyDataSetChanged();
        }
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ReleaseGroupSearchResult rg = (ReleaseGroupSearchResult) getListAdapter().getItem(position);
        startReleaseActivity(rg.getMbid());
    }

    private void startReleaseActivity(String mbid) {
        Intent releaseIntent = new Intent(App.getContext(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, mbid);
        startActivity(releaseIntent);
    }*/

}

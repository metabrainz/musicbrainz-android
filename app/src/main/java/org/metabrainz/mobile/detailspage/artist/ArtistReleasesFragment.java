package org.metabrainz.mobile.detailspage.artist;

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
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Release;

import java.util.ArrayList;
import java.util.List;

public class ArtistReleasesFragment extends Fragment {

    private RecyclerView releasesRecyclerView;
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
        adapter = new ArtistReleaseAdapter(getActivity(), releaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_releases, container, false);
        releasesRecyclerView = view.findViewById(R.id.recycler_view);
        releasesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        releasesRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = ViewModelProviders.of(getActivity()).get(ArtistViewModel.class);
        artistViewModel.initializeArtistData().observe(this, this::setReleases);
    }

    private void setReleases(Artist artist){
        // TODO: Observe artistData LiveData, instead of requesting the artist sync
        // TODO: Use DiffUtil to avoid overheads
        if(artist != null && artist.getReleases() != null){
            releaseList.clear();
            releaseList.addAll(artist.getReleases());
            adapter.notifyDataSetChanged();
        }
    }

}
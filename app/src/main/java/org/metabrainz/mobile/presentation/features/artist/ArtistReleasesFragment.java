package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(releasesRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        releasesRecyclerView.addItemDecoration(itemDecoration);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ArtistViewModel.class);
        artistViewModel.initializeData().observe(getViewLifecycleOwner(), this::setReleases);
    }

    private void setReleases(Artist artist) {
        // TODO: Observe artistData LiveData, instead of requesting the artist sync
        // TODO: Use DiffUtil to avoid overheads
        if (artist != null && artist.getReleases() != null) {
            releaseList.clear();
            releaseList.addAll(artist.getReleases());
            adapter.notifyDataSetChanged();
        }
    }

}
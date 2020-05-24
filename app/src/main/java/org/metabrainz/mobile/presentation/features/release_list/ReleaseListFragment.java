package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

<<<<<<< HEAD
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
=======
>>>>>>> Use view binding in place of findViewById.
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.FragmentArtistReleasesBinding;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistReleasesFragment extends Fragment {

    private FragmentArtistReleasesBinding binding;
    private ReleaseListAdapter adapter;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistReleasesBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(binding.getRoot().getContext(),
                DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ArtistViewModel.class);
<<<<<<< HEAD:app/src/main/java/org/metabrainz/mobile/presentation/features/release_list/ReleaseListFragment.java
        artistViewModel.initializeArtistData().observe(getViewLifecycleOwner(), this::setReleases);
=======
        artistViewModel.initializeData().observe(getViewLifecycleOwner(), this::setReleases);
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.:app/src/main/java/org/metabrainz/mobile/presentation/features/artist/ArtistReleasesFragment.java
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setReleases(List<Release> releases) {
        // TODO: Observe artistData LiveData, instead of requesting the artist sync
        // TODO: Use DiffUtil to avoid overheads
        if (artist != null && artist.getReleases() != null) {
            releaseList.clear();
            releaseList.addAll(artist.getReleases());
            adapter.notifyDataSetChanged();
        }
    }

}
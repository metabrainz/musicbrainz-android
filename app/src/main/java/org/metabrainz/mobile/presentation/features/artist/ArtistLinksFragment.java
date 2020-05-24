package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.databinding.FragmentLinksBinding;

import java.util.ArrayList;
import java.util.List;

public class ArtistLinksFragment extends Fragment {

    private FragmentLinksBinding binding;
    private ArtistLinkAdapter linkAdapter;
    private List<Link> linkList;
    private ArtistViewModel artistViewModel;

    public static ArtistLinksFragment newInstance() {
        return new ArtistLinksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkList = new ArrayList<>();
        linkAdapter = new ArtistLinkAdapter(getActivity(), linkList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLinksBinding.inflate(inflater, container, false);
        binding.linksList.setAdapter(linkAdapter);
        binding.linksList.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
<<<<<<< HEAD
        artistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
        artistViewModel.getData().observe(getViewLifecycleOwner(), this::setLinks);
=======
        artistViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ArtistViewModel.class);
        artistViewModel.initializeData().observe(getViewLifecycleOwner(), this::setLinks);
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
    }

    private void setLinks(Artist artist) {
        if (artist != null && artist.getRelations() != null) {
            linkList.clear();
            linkList.addAll(artist.getRelations());
            linkAdapter.notifyDataSetChanged();
        }
    }
}

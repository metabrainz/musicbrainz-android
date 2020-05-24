package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistLinksFragment extends Fragment {

    private RecyclerView linksRecyclerView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_links, container, false);
        linksRecyclerView = view.findViewById(R.id.links_list);
        linksRecyclerView.setAdapter(linkAdapter);
        linksRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
        artistViewModel.getData().observe(getViewLifecycleOwner(), this::setLinks);
    }

    private void setLinks(Artist artist) {
        if (artist != null && artist.getRelations() != null) {
            linkList.clear();
            linkList.addAll(artist.getRelations());
            linkAdapter.notifyDataSetChanged();
        }
    }
}

package org.metabrainz.mobile.presentation.features.links;

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
import org.metabrainz.mobile.databinding.FragmentLinksBinding;

import java.util.ArrayList;
import java.util.List;

public class LinksFragment extends Fragment {

    private FragmentLinksBinding binding;
    private LinksAdapter linkAdapter;
    private List<Link> linkList;
    private LinksViewModel linksViewModel;

    public static LinksFragment newInstance() {
        return new LinksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkList = new ArrayList<>();
        linkAdapter = new LinksAdapter(getActivity(), linkList);
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
        linksViewModel = new ViewModelProvider(requireActivity()).get(LinksViewModel.class);
        linksViewModel.getData().observe(getViewLifecycleOwner(), this::setLinks);
    }

    private void setLinks(List<Link> links) {
        if (links != null) {
            linkList.clear();
            linkList.addAll(links);
            linkAdapter.notifyDataSetChanged();
        }
    }
}

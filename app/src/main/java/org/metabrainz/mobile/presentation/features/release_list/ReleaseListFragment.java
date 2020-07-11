package org.metabrainz.mobile.presentation.features.release_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.FragmentArtistReleasesBinding;

import java.util.ArrayList;
import java.util.List;

public class ReleaseListFragment extends Fragment {

    private FragmentArtistReleasesBinding binding;
    private ReleaseListAdapter adapter;
    private List<Release> releaseList;
    private ReleaseListViewModel viewModel;

    public static ReleaseListFragment newInstance() {
        return new ReleaseListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        releaseList = new ArrayList<>();
        adapter = new ReleaseListAdapter(getActivity(), releaseList);
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
        viewModel = new ViewModelProvider(requireActivity()).get(ReleaseListViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), this::setReleases);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setReleases(List<Release> releases) {
        // TODO: Observe artistData LiveData, instead of requesting the artist sync
        // TODO: Use DiffUtil to avoid overheads
        if (releases != null) {
            releaseList.clear();
            releaseList.addAll(releases);
            adapter.notifyDataSetChanged();
        }
    }


}
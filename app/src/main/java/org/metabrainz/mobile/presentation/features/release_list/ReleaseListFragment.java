package org.metabrainz.mobile.presentation.features.release_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;

public class ReleaseListFragment extends Fragment {

    private RecyclerView releasesRecyclerView;
    private ReleaseListAdapter adapter;
    private List<Release> releaseList;
    private CoverArtViewModel viewModel;

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
        viewModel = new ViewModelProvider(requireActivity()).get(CoverArtViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), this::setReleases);
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
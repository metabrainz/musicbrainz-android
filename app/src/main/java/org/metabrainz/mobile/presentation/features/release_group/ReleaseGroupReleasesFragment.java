package org.metabrainz.mobile.presentation.features.release_group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReleaseGroupReleasesFragment extends Fragment {

    private RecyclerView releasesRecyclerView;
    private ReleaseGroupReleaseAdapter adapter;
    private List<Release> releaseList;
    private ReleaseGroupViewModel releaseGroupViewModel;

    public static ReleaseGroupReleasesFragment newInstance() {
        return new ReleaseGroupReleasesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        releaseList = new ArrayList<>();
        adapter = new ReleaseGroupReleaseAdapter(getActivity(), releaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label_releases, container, false);
        releasesRecyclerView = view.findViewById(R.id.recycler_view);
        releasesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        releasesRecyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(releasesRecyclerView, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        releaseGroupViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ReleaseGroupViewModel.class);
        releaseGroupViewModel.initializeReleaseGroupData().observe(getViewLifecycleOwner(), this::setReleases);
    }

    private void setReleases(ReleaseGroup releaseGroup) {
        // TODO: Observe releaseGroupData LiveData, instead of requesting the releaseGroup sync
        // TODO: Use DiffUtil to avoid overheads
        if (releaseGroup != null && releaseGroup.getReleases() != null) {
            releaseList.clear();
            releaseList.addAll(releaseGroup.getReleases());
            adapter.notifyDataSetChanged();
        }
    }

}
package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;

public class LabelReleasesFragment extends Fragment {

    private RecyclerView releasesRecyclerView;
    private LabelReleaseAdapter adapter;
    private List<Release> releaseList;
    private LabelViewModel labelViewModel;

    public static LabelReleasesFragment newInstance() {
        return new LabelReleasesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        releaseList = new ArrayList<>();
        adapter = new LabelReleaseAdapter(getActivity(), releaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label_releases, container, false);
        releasesRecyclerView = view.findViewById(R.id.recycler_view);
        releasesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        releasesRecyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(releasesRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        releasesRecyclerView.addItemDecoration(itemDecoration);
        ViewCompat.setNestedScrollingEnabled(releasesRecyclerView, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        labelViewModel = new ViewModelProvider(this).get(LabelViewModel.class);
        labelViewModel.initializeData().observe(getViewLifecycleOwner(), this::setReleases);
    }

    private void setReleases(MBEntity entity) {
        // TODO: Observe labelData LiveData, instead of requesting the label sync
        // TODO: Use DiffUtil to avoid overheads
        if (entity instanceof Label) {
            Label label = (Label) entity;
            if (label.getReleases() != null) {
                releaseList.clear();
                releaseList.addAll(label.getReleases());
                adapter.notifyDataSetChanged();
            }
        }
    }

}
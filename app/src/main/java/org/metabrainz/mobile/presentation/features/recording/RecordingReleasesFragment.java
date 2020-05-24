package org.metabrainz.mobile.presentation.features.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordingReleasesFragment extends Fragment {

    private RecyclerView releasesRecyclerView;
    private RecordingReleaseAdapter adapter;
    private List<Release> releaseList;
    private RecordingViewModel recordingViewModel;

    public static RecordingReleasesFragment newInstance() {
        return new RecordingReleasesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        releaseList = new ArrayList<>();
        adapter = new RecordingReleaseAdapter(releaseList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recording_releases, container, false);
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
        recordingViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RecordingViewModel.class);
        recordingViewModel.initializeData().observe(getViewLifecycleOwner(), this::setReleases);
    }

    private void setReleases(Recording recording) {
        // TODO: Observe recordingData LiveData, instead of requesting the recording sync
        // TODO: Use DiffUtil to avoid overheads
        if (recording != null && recording.getReleases() != null) {
            releaseList.clear();
            releaseList.addAll(recording.getReleases());
            adapter.notifyDataSetChanged();
        }
    }

}
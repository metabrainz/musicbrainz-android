package org.metabrainz.mobile.presentation.features.release;

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
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.Track;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;
import java.util.List;

public class ReleaseTracksFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReleaseViewModel viewModel;
    private ReleaseTrackAdapter adapter;
    private List<Track> tracks;

    public static ReleaseTracksFragment newInstance() {
        return new ReleaseTracksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracks = new ArrayList<>();
        adapter = new ReleaseTrackAdapter(tracks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracklist, container, false);
        recyclerView = view.findViewById(R.id.track_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ReleaseViewModel.class);
        viewModel.initializeReleaseData().observe(this, this::setTracks);
    }

    private void setTracks(Release release) {
        if (release != null && release.getMedia() != null && !release.getMedia().isEmpty()
                && release.getMedia().get(0).getTracks() != null &&
                !release.getMedia().get(0).getTracks().isEmpty()) {
            tracks.clear();
            tracks.addAll(release.getMedia().get(0).getTracks());
            adapter.notifyDataSetChanged();
        }
    }
}
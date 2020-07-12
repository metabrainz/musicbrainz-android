package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.FragmentTracklistBinding;

import java.util.ArrayList;
import java.util.List;

public class ReleaseTracksFragment extends Fragment {

    private FragmentTracklistBinding binding;
    private ReleaseViewModel viewModel;
    private ReleaseTrackAdapter adapter;
    private List<Media> mediaList;

    public static ReleaseTracksFragment newInstance() {
        return new ReleaseTracksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaList = new ArrayList<>();
        adapter = new ReleaseTrackAdapter(mediaList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTracklistBinding.inflate(inflater, container, false);
        binding.trackList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(binding.getRoot().getContext(),
                DividerItemDecoration.VERTICAL);
        binding.trackList.addItemDecoration(itemDecoration);
        binding.trackList.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(binding.trackList, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ReleaseViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), this::setTracks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setTracks(Release release) {
        if (release != null && release.getMedia() != null && !release.getMedia().isEmpty()) {
            mediaList.clear();
            mediaList.addAll(release.getMedia());
            adapter.notifyDataSetChanged();
        }
    }
}

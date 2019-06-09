package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends MusicBrainzActivity {

    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private CollectionListAdapter adapter;
    private List<Collection> collections;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        collections = new ArrayList<>();

        adapter = new CollectionListAdapter(collections);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getCollectionData().observe(this, data -> {
            collections.clear();
            collections.addAll(data);
            adapter.notifyDataSetChanged();
        });

        viewModel.fetchCollections("amCap1712", true);
    }
}

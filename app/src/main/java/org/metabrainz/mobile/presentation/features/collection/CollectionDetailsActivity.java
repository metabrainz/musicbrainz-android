package org.metabrainz.mobile.presentation.features.collection;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.ActivityCollectionDetailsBinding;
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity;
import org.metabrainz.mobile.util.Resource;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity to display a list of collection results to the user and support intents
 * to info Activity types based on the selection.
 */
@AndroidEntryPoint
public class CollectionDetailsActivity extends MusicBrainzActivity {

    private ActivityCollectionDetailsBinding binding;

    private CollectionViewModel viewModel;
    private ResultAdapter adapter;
    private String id;
    private MBEntityType entity;
    private List<ResultItem> collectionResults;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        binding.noResult.getRoot().setVisibility(View.GONE);
        binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);

        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);
        id = getIntent().getStringExtra(Constants.MBID);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collectionResults = new ArrayList<>();
        adapter = new ResultAdapter(collectionResults, entity);
        adapter.resetAnimation();

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setVisibility(View.GONE);

        viewModel.fetchCollectionDetails(entity, id).observe(this, this::setResults);
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        checkHasResults();
    }

    private void checkHasResults() {
        if (adapter.getItemCount() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noResult.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_open_website).setVisible(false);
        return true;
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.EMPTY;
    }

    private void setResults(Resource<List<ResultItem>> resource) {
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            collectionResults.clear();
            collectionResults.addAll(resource.getData());
        }
        refresh();
    }
}

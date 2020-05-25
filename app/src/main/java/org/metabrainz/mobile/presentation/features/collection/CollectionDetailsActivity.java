package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to display a list of collection results to the user and support intents
 * to info Activity types based on the selection.
 */
public class CollectionDetailsActivity extends MusicBrainzActivity {

    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private TextView noRes;
    private String entity, id;
    private List<ResultItem> collectionResults;
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        entity = getIntent().getStringExtra(IntentFactory.Extra.TYPE);
        id = getIntent().getStringExtra(IntentFactory.Extra.COLLECTION_MBID);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collectionResults = new ArrayList<>();
        adapter = new ResultAdapter(collectionResults, entity);
        adapter.resetAnimation();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchCollectionDetails(entity, id).observe(this,
                results -> {
                    collectionResults.clear();
                    collectionResults.addAll(results);
                    refresh();
                });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        checkHasResults();
    }


    private void checkHasResults() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            noRes.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noRes.setVisibility(View.GONE);
        }
    }
}

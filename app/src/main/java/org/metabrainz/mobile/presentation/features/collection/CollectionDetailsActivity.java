package org.metabrainz.mobile.presentation.features.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.ActivityCollectionBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.login.LoginActivity;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;
import org.metabrainz.mobile.presentation.features.login.LogoutActivity;
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to display a list of collection results to the user and support intents
 * to info Activity types based on the selection.
 */
public class CollectionDetailsActivity extends MusicBrainzActivity {

    private ActivityCollectionBinding binding;

    private CollectionViewModel viewModel;
    private ResultAdapter adapter;
    private String id;
    private MBEntityType entity;
    private List<ResultItem> collectionResults;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.noResult.setVisibility(View.GONE);
        binding.progressSpinner.setIndeterminate(true);
        binding.progressSpinner.setVisibility(View.GONE);

        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);
        id = getIntent().getStringExtra(Constants.MBID);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collectionResults = new ArrayList<>();
        adapter = new ResultAdapter(collectionResults, entity);
        adapter.resetAnimation();

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setVisibility(View.GONE);

        binding.progressSpinner.setVisibility(View.VISIBLE);
        viewModel.fetchCollectionDetails(entity, id).observe(this,
                results -> {
                    collectionResults.clear();
                    collectionResults.addAll(results);
                    refresh();
                });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        binding.progressSpinner.setVisibility(View.GONE);
        checkHasResults();
    }


    private void checkHasResults() {
        if (adapter.getItemCount() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noResult.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noResult.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_login: {
                if(LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT)
                    startActivity(new Intent(this, LoginActivity.class));
                else
                    startActivity(new Intent(this, LogoutActivity.class));
                return true;
            }
            case R.id.menu_preferences:  startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:     return super.onOptionsItemSelected(item);

        }
    }
}

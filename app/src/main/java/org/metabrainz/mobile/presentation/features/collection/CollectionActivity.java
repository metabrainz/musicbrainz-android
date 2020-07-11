package org.metabrainz.mobile.presentation.features.collection;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.databinding.ActivityCollectionBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionActivity extends MusicBrainzActivity {


    private ActivityCollectionBinding binding;
    private CollectionViewModel viewModel;

    private CollectionListAdapter adapter;
    private List<Collection> collections;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collections = new ArrayList<>();

        adapter = new CollectionListAdapter(collections);
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN) {
            binding.loginRequired.setVisibility(View.GONE);
            binding.noResult.setVisibility(View.GONE);
            binding.progressSpinner.setIndeterminate(true);
            binding.progressSpinner.setVisibility(View.GONE);

            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            binding.recyclerView.addItemDecoration(itemDecoration);

            binding.progressSpinner.setVisibility(View.VISIBLE);
            boolean getPrivateCollections =
                    LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN
                            && UserPreferences.getPrivateCollectionsPreference();
            viewModel.fetchCollectionData(LoginSharedPreferences.getUsername(),
                    getPrivateCollections).observe(this, data -> {
                CollectionUtils.removeCollections(data);
                collections.clear();
                collections.addAll(data);
                adapter.notifyDataSetChanged();
                checkHasResults();
            });

        } else {
            binding.noResult.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.progressSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            collections.clear();
            checkHasResults();
            binding.noResult.setVisibility(View.GONE);
        }
    }

    private void checkHasResults() {
        binding.progressSpinner.setVisibility(View.GONE);
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
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_open_website).setVisible(false);
        return true;
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.EMPTY;
    }
}

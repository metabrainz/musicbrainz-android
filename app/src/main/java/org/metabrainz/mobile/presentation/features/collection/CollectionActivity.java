package org.metabrainz.mobile.presentation.features.collection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.databinding.ActivityCollectionBinding;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.presentation.features.login.LoginActivity;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;
import org.metabrainz.mobile.util.Resource;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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

        setupToolbar(binding);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collections = new ArrayList<>();

        adapter = new CollectionListAdapter(collections);
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN) {
            binding.loginRequired.setVisibility(View.GONE);
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);

            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            binding.recyclerView.addItemDecoration(itemDecoration);
            binding.recyclerView.setVisibility(View.GONE);

            boolean getPrivateCollections =
                    LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN
                            && UserPreferences.getPrivateCollectionsPreference();
            viewModel.fetchCollectionData(LoginSharedPreferences.getUsername(),
                    getPrivateCollections).observe(this, this::setCollections);
        } else {
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.progressSpinner.getRoot().setVisibility(View.GONE);
            binding.loginRequired.setVisibility(View.GONE);
            callAlert();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            collections.clear();
            checkHasResults();
            binding.noResult.getRoot().setVisibility(View.GONE);
        }
    }

    private void checkHasResults() {
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noResult.getRoot().setVisibility(View.GONE);
        }
    }

    public void callAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.loginrequiredDialog);
        builder.setTitle("Login Required");
        builder.setMessage("You need to log in to see your collections");
        builder.setPositiveButton("Login", (dialog, which) -> {
            startActivity(new Intent(CollectionActivity.this, LoginActivity.class));
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            startActivity(new Intent(CollectionActivity.this, DashboardActivity.class));
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        menu.findItem(R.id.menu_open_website).setVisible(false);
        return true;
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.EMPTY;
    }

    private void setCollections(Resource<List<Collection>> resource) {
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            List<Collection> data = resource.getData();
            CollectionUtils.removeCollections(data);
            collections.clear();
            collections.addAll(data);
            adapter.notifyDataSetChanged();
        }
        checkHasResults();
    }
}

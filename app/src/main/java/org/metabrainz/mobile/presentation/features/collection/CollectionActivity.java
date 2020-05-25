package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionActivity extends MusicBrainzActivity {

    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private CollectionListAdapter adapter;
    private List<Collection> collections;

    private TextView noRes;
    private ProgressBar progressBar;
    private TextView loginRequiredView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collections = new ArrayList<>();

        noRes = findViewById(R.id.no_result);
        progressBar = findViewById(R.id.progress_spinner);
        adapter = new CollectionListAdapter(collections);
        recyclerView = findViewById(R.id.recycler_view);
        loginRequiredView = findViewById(R.id.login_required);

        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_IN) {
            loginRequiredView.setVisibility(View.GONE);
            noRes.setVisibility(View.GONE);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);

            progressBar.setVisibility(View.VISIBLE);
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
            noRes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginSharedPreferences.getLoginStatus() == LoginSharedPreferences.STATUS_LOGGED_OUT) {
            collections.clear();
            checkHasResults();
            noRes.setVisibility(View.GONE);
        }
    }

    private void checkHasResults() {
        progressBar.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            noRes.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noRes.setVisibility(View.GONE);
        }
    }
}

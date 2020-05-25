package org.metabrainz.mobile.presentation.features.search;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends MusicBrainzActivity implements SearchView.OnQueryTextListener {

    private static SearchViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ResultAdapter adapter;
    private TextView noRes;
    private List<ResultItem> results;
    private String query;
    private MBEntityType entity;
    private SuggestionHelper suggestionHelper;
    private CursorAdapter suggestionAdapter;
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recycler_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        query = getIntent().getStringExtra(SearchManager.QUERY);
        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);

        results = new ArrayList<>();

        adapter = new ResultAdapter(results, entity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        viewModel.getResultData(entity).observe(this, items -> {
            results.clear();
            results.addAll(items);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            checkHasResults();
        });

        doSearch(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQuery(query, false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        suggestionHelper = new SuggestionHelper(this);
        suggestionAdapter = suggestionHelper.getAdapter();
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) suggestionAdapter.getItem(position);
                String query = cursor.getString(cursor.getColumnIndexOrThrow("display1"));
                searchView.setQuery(query, false);
                return true;
            }
        });
        return true;
    }

    private void doSearch(String query) {
        saveSearchSuggestion(query);
        progressBar.setVisibility(View.VISIBLE);
        adapter.resetAnimation();
        viewModel.search(query);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(searchView.getQuery().toString());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        suggestionAdapter.changeCursor(suggestionHelper.getMatchingEntries(newText));
        return false;
    }

    private void saveSearchSuggestion(String query) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

}

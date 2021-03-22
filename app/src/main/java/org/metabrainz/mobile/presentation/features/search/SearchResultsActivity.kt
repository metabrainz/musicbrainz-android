package org.metabrainz.mobile.presentation.features.search;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.ActivitySearchResultsBinding;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemComparator;
import org.metabrainz.mobile.presentation.features.adapters.ResultPagingAdapter;
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchResultsActivity extends MusicBrainzActivity implements SearchView.OnQueryTextListener {

    private ActivitySearchResultsBinding binding;
    private SearchViewModel viewModel;
    private SearchView searchView;
    private ResultPagingAdapter adapter;
    private String query;
    private MBEntityType entity;
    private SuggestionHelper suggestionHelper;
    private CursorAdapter suggestionAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        query = getIntent().getStringExtra(SearchManager.QUERY);
        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);
        adapter = new ResultPagingAdapter(new ResultItemComparator(), entity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        doSearch();
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

    private void doSearch() {
        saveSearchSuggestion(query);
        adapter.resetAnimation();
        viewModel.search(entity, query)
                .observe(this, pagingData -> adapter.submitData(getLifecycle(), pagingData));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!this.query.equalsIgnoreCase(query)) {
            this.query = query;
            doSearch();
        }
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

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "search?type=" + entity.name + "&query=" + query);
    }
}

package org.metabrainz.mobile.presentation.features.search;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.R;
<<<<<<< HEAD
<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.ActivitySearchBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
<<<<<<< HEAD
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
=======
import org.metabrainz.mobile.presentation.features.adapters.ArtistAdapter;
import org.metabrainz.mobile.presentation.features.adapters.EventAdapter;
import org.metabrainz.mobile.presentation.features.adapters.InstrumentAdapter;
import org.metabrainz.mobile.presentation.features.adapters.LabelAdapter;
import org.metabrainz.mobile.presentation.features.adapters.RecordingAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseGroupAdapter;
import org.metabrainz.mobile.presentation.features.adapters.TypeAdapter;
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
=======
import org.metabrainz.mobile.presentation.IntentFactory;
=======
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
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

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private SearchView searchView;
<<<<<<< HEAD
<<<<<<< HEAD
    private ResultAdapter adapter;
    private List<ResultItem> results;
    private String query;
    private MBEntityType entity;
=======
    private TypeAdapter adapter;
=======
    private ResultAdapter adapter;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
    private TextView noRes;
    private List<ResultItem> results;
<<<<<<< HEAD
    private String query, entity;
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
=======
    private String query;
    private MBEntityType entity;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
    private SuggestionHelper suggestionHelper;
    private CursorAdapter suggestionAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.noResult.setVisibility(View.GONE);
        binding.progressSpinner.setIndeterminate(true);
        binding.progressSpinner.setVisibility(View.GONE);

        query = getIntent().getStringExtra(SearchManager.QUERY);
        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);

        results = new ArrayList<>();

<<<<<<< HEAD
<<<<<<< HEAD
        adapter = new ResultAdapter(results, entity);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setVisibility(View.GONE);
=======
        adapter = new ResultAdapter(results, entity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        viewModel.getResultData(entity).observe(this, items -> {
            results.clear();
            results.addAll(items);
            adapter.notifyDataSetChanged();
<<<<<<< HEAD
            binding.progressSpinner.setVisibility(View.GONE);
            checkHasResults();
        });
=======
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
>>>>>>> 90e6cee... Extract common code out of type adapters to base search adapter.
=======
            progressBar.setVisibility(View.GONE);
            checkHasResults();
        });
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

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

<<<<<<< HEAD
<<<<<<< HEAD
=======
    private void chooseAdapter() {
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                adapter = new ReleaseAdapter(releaseSearchResults);
                break;
            case IntentFactory.Extra.LABEL:
                adapter = new LabelAdapter(labelSearchResults);
                break;
            case IntentFactory.Extra.RECORDING:
                adapter = new RecordingAdapter(recordingSearchResults);
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                adapter = new ReleaseGroupAdapter(releaseGroupSearchResults);
                break;
            case IntentFactory.Extra.EVENT:
                adapter = new EventAdapter(eventSearchResults);
                break;
            case IntentFactory.Extra.INSTRUMENT:
                adapter = new InstrumentAdapter(instrumentSearchResults);
                break;
            default:
                adapter = new ArtistAdapter(artistSearchResults);
        }
    }

>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
=======
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
    private void doSearch(String query) {
        saveSearchSuggestion(query);
        binding.progressSpinner.setVisibility(View.VISIBLE);
        adapter.resetAnimation();
        viewModel.search(query);
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

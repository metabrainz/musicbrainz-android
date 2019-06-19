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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends MusicBrainzActivity implements SearchView.OnQueryTextListener {

    private static SearchViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchAdapter adapter;
    private TextView noRes;
    private List<Artist> artistSearchResults = new ArrayList<>();
    private List<Release> releaseSearchResults = new ArrayList<>();
    private List<Label> labelSearchResults = new ArrayList<>();
    private List<Recording> recordingSearchResults = new ArrayList<>();
    private List<ReleaseGroup> releaseGroupSearchResults = new ArrayList<>();
    private List<Instrument> instrumentSearchResults = new ArrayList<>();
    private List<Event> eventSearchResults = new ArrayList<>();
    private String query, entity;
    private SuggestionHelper suggestionHelper;
    private CursorAdapter suggestionAdapter;
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_search);
        recyclerView = findViewById(R.id.recycler_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        query = getIntent().getStringExtra(SearchManager.QUERY);
        entity = getIntent().getStringExtra(IntentFactory.Extra.TYPE);

        chooseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        viewModel.prepareSearch(query, entity);
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

    private void chooseAdapter() {
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                adapter = new SearchAdapterRelease(releaseSearchResults);
                break;
            case IntentFactory.Extra.LABEL:
                adapter = new SearchAdapterLabel(labelSearchResults);
                break;
            case IntentFactory.Extra.RECORDING:
                adapter = new SearchAdapterRecording(recordingSearchResults);
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                adapter = new SearchAdapterReleaseGroup(releaseGroupSearchResults);
                break;
            case IntentFactory.Extra.EVENT:
                adapter = new SearchAdapterEvent(eventSearchResults);
                break;
            case IntentFactory.Extra.INSTRUMENT:
                adapter = new SearchAdapterInstrument(instrumentSearchResults);
                break;
            default:
                adapter = new SearchAdapterArtist(artistSearchResults);
        }
    }

    private void doSearch(String query) {
        saveSearchSuggestion(query);
        progressBar.setVisibility(View.VISIBLE);
        adapter.resetAnimation();
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                viewModel.getReleaseSearchResponse(query).observe(this,
                        (List<Release> releaseSearchProperties) -> {
                            releaseSearchResults.clear();
                            releaseSearchResults.addAll(releaseSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            case IntentFactory.Extra.LABEL:
                viewModel.getLabelSearchResponse(query).observe(this,
                        (List<Label> labelSearchProperties) -> {
                            labelSearchResults.clear();
                            labelSearchResults.addAll(labelSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            case IntentFactory.Extra.RECORDING:
                viewModel.getRecordingSearchResponse(query).observe(this,
                        (List<Recording> recordingSearchProperties) -> {
                            recordingSearchResults.clear();
                            recordingSearchResults.addAll(recordingSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            case IntentFactory.Extra.EVENT:
                viewModel.getEventSearchResponse(query).observe(this,
                        (List<Event> eventSearchProperties) -> {
                            eventSearchResults.clear();
                            eventSearchResults.addAll(eventSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            case IntentFactory.Extra.INSTRUMENT:
                viewModel.getInstrumentSearchResponse(query).observe(this,
                        (List<Instrument> instrumentSearchProperties) -> {
                            instrumentSearchResults.clear();
                            instrumentSearchResults.addAll(instrumentSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                viewModel.getReleaseGroupSearchResponse(query).observe(this,
                        (List<ReleaseGroup> releaseGroupSearchProperties) -> {
                            releaseGroupSearchResults.clear();
                            releaseGroupSearchResults.addAll(releaseGroupSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
                break;
            default:
                viewModel.getArtistSearchResponse(query)
                        .observe(this, (List<Artist> artistSearchProperties) -> {
                            artistSearchResults.clear();
                            artistSearchResults.addAll(artistSearchProperties);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkHasResults();
                        });
        }
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

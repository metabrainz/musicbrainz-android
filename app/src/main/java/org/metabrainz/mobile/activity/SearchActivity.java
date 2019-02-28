package org.metabrainz.mobile.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.list.SearchAdapter;
import org.metabrainz.mobile.adapter.list.SearchAdapterArtist;
import org.metabrainz.mobile.adapter.list.SearchAdapterEvent;
import org.metabrainz.mobile.adapter.list.SearchAdapterInstrument;
import org.metabrainz.mobile.adapter.list.SearchAdapterLabel;
import org.metabrainz.mobile.adapter.list.SearchAdapterRecording;
import org.metabrainz.mobile.adapter.list.SearchAdapterRelease;
import org.metabrainz.mobile.adapter.list.SearchAdapterReleaseGroup;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Event;
import org.metabrainz.mobile.api.data.search.entity.Instrument;
import org.metabrainz.mobile.api.data.search.entity.Label;
import org.metabrainz.mobile.api.data.search.entity.Recording;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.api.data.search.entity.ReleaseGroup;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.suggestion.SuggestionHelper;
import org.metabrainz.mobile.suggestion.SuggestionProvider;
import org.metabrainz.mobile.viewmodel.SearchViewModel;

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
    private View error;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_search);
        recyclerView = findViewById(R.id.recycler_view);


        query = getIntent().getStringExtra(SearchManager.QUERY);
        entity = getIntent().getStringExtra(IntentFactory.Extra.TYPE);

        chooseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        viewModel.prepareSearch(query, entity);
        doSearch(query);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noRes = findViewById(R.id.noresults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    /*private void displayArtistResultsView() {

        artistResultsView.setAdapter(new ArtistSearchAdapter(SearchActivity.this, artistSearchResults));
        //artistResultsView.setOnItemClickListener(new ArtistItemClickListener());
        artistResultsView.setVisibility(View.VISIBLE);

        if (artistSearchResults.isEmpty())
            noRes.setVisibility(View.VISIBLE);
        else {
            noRes.setVisibility(View.GONE);
            //saveQueryAsSuggestion();
        }
    }

    private void displayRGResultsView() {
        ListView rgResultsView = (ListView) findViewById(R.id.searchres_rg_list);
        rgResultsView.setAdapter(new RGSearchAdapter(SearchActivity.this, rgSearchResults));
        //rgResultsView.setOnItemClickListener(new RGItemClickListener());
        rgResultsView.setVisibility(View.VISIBLE);

        if (rgSearchResults.isEmpty()) {
            noRes.setVisibility(View.VISIBLE);
        } else {
            noRes.setVisibility(View.GONE);
            //saveQueryAsSuggestion();
        }
    }*/

    /* private void saveQueryAsSuggestion() {
        if (App.getUser().isSearchSuggestionsEnabled()) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchTerm, null);
        }
    }*/

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
                return false;
            }
        });
        return true;
    }

    private void showConnectionWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = error.findViewById(R.id.retry_button);
        retry.setOnClickListener(view -> error.setVisibility(View.GONE));
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
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                viewModel.getReleaseSearchResponse(query).observe(this,
                        (List<Release> releaseSearchProperties) -> {
                            releaseSearchResults.clear();
                            releaseSearchResults.addAll(releaseSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            case IntentFactory.Extra.LABEL:
                viewModel.getLabelSearchResponse(query).observe(this,
                        (List<Label> labelSearchProperties) -> {
                            labelSearchResults.clear();
                            labelSearchResults.addAll(labelSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            case IntentFactory.Extra.RECORDING:
                viewModel.getRecordingSearchResponse(query).observe(this,
                        (List<Recording> recordingSearchProperties) -> {
                            recordingSearchResults.clear();
                            recordingSearchResults.addAll(recordingSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            case IntentFactory.Extra.EVENT:
                viewModel.getEventSearchResponse(query).observe(this,
                        (List<Event> eventSearchProperties) -> {
                            eventSearchResults.clear();
                            eventSearchResults.addAll(eventSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            case IntentFactory.Extra.INSTRUMENT:
                viewModel.getInstrumentSearchResponse(query).observe(this,
                        (List<Instrument> instrumentSearchProperties) -> {
                            instrumentSearchResults.clear();
                            instrumentSearchResults.addAll(instrumentSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                viewModel.getReleaseGroupSearchResponse(query).observe(this,
                        (List<ReleaseGroup> releaseGroupSearchProperties) -> {
                            releaseGroupSearchResults.clear();
                            releaseGroupSearchResults.addAll(releaseGroupSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
                break;
            default:
                viewModel.getArtistSearchResponse(query)
                        .observe(this, (List<Artist> artistSearchProperties) -> {
                            artistSearchResults.clear();
                            artistSearchResults.addAll(artistSearchProperties);
                            adapter.notifyDataSetChanged();
                        });
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

    private class ArtistItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startArtistActivity(position);
        }

        private void startArtistActivity(int position) {
            Intent artistIntent = new Intent(SearchActivity.this, ArtistActivity.class);
            Artist artist = artistSearchResults.get(position);
            artistIntent.putExtra(IntentFactory.Extra.ARTIST_MBID, artist.getMbid());
            startActivity(artistIntent);
        }
    }
    /*
    private class RGItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ReleaseGroupSearchResult rg = rgSearchResults.get(position);
            if (rg.getNumberOfReleases() == 1) {
                startReleaseActivity(rg.getReleaseMbids().getFirst());
            } else {
                startReleaseGroupActivity(rg.getMbid());
            }
        }

        private void startReleaseGroupActivity(String mbid) {
            Intent releaseIntent = new Intent(SearchActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra(Extra.RG_MBID, mbid);
            startActivity(releaseIntent);
        }

        private void startReleaseActivity(String mbid) {
            Intent releaseIntent = new Intent(SearchActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra(Extra.RELEASE_MBID, mbid);
            startActivity(releaseIntent);
        }
    }*/

}

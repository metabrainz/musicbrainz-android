package org.musicbrainz.mobile.activity;

import java.util.List;

import org.musicbrainz.android.api.data.ArtistSearchResult;
import org.musicbrainz.android.api.data.ReleaseGroupInfo;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ArtistSearchAdapter;
import org.musicbrainz.mobile.adapter.list.RGSearchAdapter;
import org.musicbrainz.mobile.adapter.pager.SearchPagerAdapter;
import org.musicbrainz.mobile.async.SearchArtistLoader;
import org.musicbrainz.mobile.async.SearchLoader;
import org.musicbrainz.mobile.async.SearchReleaseGroupLoader;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.SearchResults;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends MusicBrainzActivity implements LoaderCallbacks<AsyncResult<SearchResults>> {

    private static final int SEARCH_ARTIST_LOADER = 0;
    private static final int SEARCH_RELEASE_GROUP_LOADER = 1;
    private static final int SEARCH_LOADER = 2;

    private String searchTerm;
    private View error;

    private List<ArtistSearchResult> artistSearchResults;
    private List<ReleaseGroupInfo> rgSearchResults;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        configureSearch();
    }

    private void configureSearch() {
        setContentView(R.layout.activity_search);
        error = findViewById(R.id.error);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            searchTerm = getIntent().getStringExtra(SearchManager.QUERY);
            getIntent().putExtra(Extra.TYPE, Extra.ALL);
        } else {
            searchTerm = getIntent().getStringExtra(Extra.QUERY);
        }
        doSearch();
    }

    private void doSearch() {
        String intentType = getIntent().getStringExtra(Extra.TYPE);
        if (intentType.equals(Extra.ARTIST)) {
            setTitle(getString(R.string.search_bar_artists), searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_ARTIST_LOADER, null, this);
        } else if (intentType.equals(Extra.RELEASE_GROUP)) {
            setTitle(getString(R.string.search_bar_rgs), searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_RELEASE_GROUP_LOADER, null, this);
        } else {
            setTitle(searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_LOADER, null, this);
        }
    }
    
    private void setTitle(String title) {
        getSupportActionBar().setSubtitle(null);
        getSupportActionBar().setTitle(title);
    }
    
    private void setTitle(String title, String subtitle) {
        setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
    }

    private void enableResultTabs() {
        setContentView(R.layout.activity_search_all);
        SearchPagerAdapter adapter = new SearchPagerAdapter(this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    private void displayArtistResultsView() {
        ListView artistResultsView = (ListView) findViewById(R.id.searchres_artist_list);
        artistResultsView.setAdapter(new ArtistSearchAdapter(SearchActivity.this, artistSearchResults));
        artistResultsView.setOnItemClickListener(new ArtistItemClickListener());
        artistResultsView.setVisibility(View.VISIBLE);

        if (artistSearchResults.isEmpty()) {
            RelativeLayout artistResults = (RelativeLayout) findViewById(R.id.artist_results_container);
            TextView noRes = (TextView) artistResults.findViewById(R.id.noresults);
            noRes.setVisibility(View.VISIBLE);
        } else {
            saveQueryAsSuggestion();
        }
    }

    private void displayRGResultsView() {
        ListView rgResultsView = (ListView) findViewById(R.id.searchres_rg_list);
        rgResultsView.setAdapter(new RGSearchAdapter(SearchActivity.this, rgSearchResults));
        rgResultsView.setOnItemClickListener(new RGItemClickListener());
        rgResultsView.setVisibility(View.VISIBLE);

        if (rgSearchResults.isEmpty()) {
            RelativeLayout rgResults = (RelativeLayout) findViewById(R.id.rg_results_container);
            TextView noResults = (TextView) rgResults.findViewById(R.id.noresults);
            noResults.setVisibility(View.VISIBLE);
        } else {
            saveQueryAsSuggestion();
        }
    }

    private void saveQueryAsSuggestion() {
        if (App.getUser().isSearchSuggestionsEnabled()) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchTerm, null);
        }
    }

    private void restartLoader() {
        String intentType = getIntent().getStringExtra(Extra.TYPE);
        if (intentType.equals(Extra.ARTIST)) {
            getSupportLoaderManager().restartLoader(SEARCH_ARTIST_LOADER, null, this);
        } else if (intentType.equals(Extra.RELEASE_GROUP)) {
            getSupportLoaderManager().restartLoader(SEARCH_RELEASE_GROUP_LOADER, null, this);
        } else {
            getSupportLoaderManager().restartLoader(SEARCH_LOADER, null, this);
        }
    }

    private void toggleLoading() {
        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        if (loading.getVisibility() == View.GONE) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private class ArtistItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startArtistActivity(position);
        }

        private void startArtistActivity(int position) {
            Intent artistIntent = new Intent(SearchActivity.this, ArtistActivity.class);
            ArtistSearchResult artist = artistSearchResults.get(position);
            artistIntent.putExtra(Extra.ARTIST_MBID, artist.getMbid());
            startActivity(artistIntent);
        }
    }

    private class RGItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ReleaseGroupInfo rg = rgSearchResults.get(position);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public Loader<AsyncResult<SearchResults>> onCreateLoader(int id, Bundle args) {
        switch (id) {
        case SEARCH_ARTIST_LOADER:
            return new SearchArtistLoader(searchTerm);
        case SEARCH_RELEASE_GROUP_LOADER:
            return new SearchReleaseGroupLoader(searchTerm);
        case SEARCH_LOADER:
            return new SearchLoader(searchTerm);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<SearchResults>> loader, AsyncResult<SearchResults> result) {
        toggleLoading();
        switch (result.getStatus()) {
        case SUCCESS:
            handleResults(result.getData());
            break;
        case EXCEPTION:
            showConnectionWarning();
        }
    }

    private void showConnectionWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = (Button) error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                restartLoader();
                toggleLoading();
                error.setVisibility(View.GONE);
            }
        });
    }

    private void handleResults(SearchResults results) {
        switch (results.getType()) {
        case ARTIST:
            artistSearchResults = results.getArtistResults();
            displayArtistResultsView();
            break;
        case RELEASE_GROUP:
            rgSearchResults = results.getReleaseGroupResults();
            displayRGResultsView();
            break;
        case ALL:
            // TODO Figure out why result lists are null here without destroying.
            getSupportLoaderManager().destroyLoader(SEARCH_LOADER);
            artistSearchResults = results.getArtistResults();
            rgSearchResults = results.getReleaseGroupResults();
            enableResultTabs();
            displayArtistResultsView();
            displayRGResultsView();
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<SearchResults>> loader) {
        loader.reset();
    }

}

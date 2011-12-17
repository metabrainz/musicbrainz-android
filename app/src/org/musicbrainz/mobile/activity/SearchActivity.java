/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.activity;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.SearchArtistAdapter;
import org.musicbrainz.mobile.adapter.list.SearchReleaseGroupAdapter;
import org.musicbrainz.mobile.adapter.pager.SearchPagerAdapter;
import org.musicbrainz.mobile.loader.SearchArtistLoader;
import org.musicbrainz.mobile.loader.SearchLoader;
import org.musicbrainz.mobile.loader.SearchReleaseGroupLoader;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.SearchResults;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.Menu;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends MusicBrainzActivity implements LoaderCallbacks<AsyncResult<SearchResults>> {

    private static final int SEARCH_ARTIST_LOADER = 0;
    private static final int SEARCH_RELEASE_GROUP_LOADER = 1;
    private static final int SEARCH_LOADER = 2;

    private static final int DIALOG_CONNECTION_FAILURE = 0;

    private String searchTerm;

    private LinkedList<ArtistStub> artistSearchResults;
    private LinkedList<ReleaseGroupStub> rgSearchResults;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        configureSearch();
    }

    private void configureSearch() {
        setContentView(R.layout.activity_search);
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
            setTitle(getString(R.string.search_bar_artists) + " " + searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_ARTIST_LOADER, null, this);
        } else if (intentType.equals(Extra.RELEASE_GROUP)) {
            setTitle(getString(R.string.search_bar_rgs) + " " + searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_RELEASE_GROUP_LOADER, null, this);
        } else {
            setTitle(searchTerm);
            getSupportLoaderManager().initLoader(SEARCH_LOADER, null, this);
        }
    }
    
    private void setTitle(String title) {
        getSupportActionBar().setTitle(title);
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
        artistResultsView.setAdapter(new SearchArtistAdapter(SearchActivity.this, artistSearchResults));
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
        rgResultsView.setAdapter(new SearchReleaseGroupAdapter(SearchActivity.this, rgSearchResults));
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
        if (shouldProvideSearchSuggestions()) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchTerm, null);
        }
    }

    protected Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new ErrorPositiveListener());
        builder.setNegativeButton(getString(R.string.err_neg), new ErrorNegativeListener());
        return builder.create();
    }

    private class ErrorPositiveListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            
            restartLoader();
            // TODO
            //doSearch();
            dialog.cancel();
            toggleLoading();
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

    private class ErrorNegativeListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SearchActivity.this.finish();
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
            ArtistStub stub = artistSearchResults.get(position);
            artistIntent.putExtra(Extra.ARTIST_MBID, stub.getMbid());
            startActivity(artistIntent);
        }
    }

    private class RGItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ReleaseGroupStub rg = rgSearchResults.get(position);
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
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_CONNECTION_FAILURE:
            return createConnectionErrorDialog();
        }
        return null;
    }

    @Override
    public Loader<AsyncResult<SearchResults>> onCreateLoader(int id, Bundle args) {
        switch (id) {
        case SEARCH_ARTIST_LOADER:
            return new SearchArtistLoader(this, getUserAgent(), searchTerm);
        case SEARCH_RELEASE_GROUP_LOADER:
            return new SearchReleaseGroupLoader(this, getUserAgent(), searchTerm);
        case SEARCH_LOADER:
            return new SearchLoader(this, getUserAgent(), searchTerm);
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
            showDialog(DIALOG_CONNECTION_FAILURE);
        }
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

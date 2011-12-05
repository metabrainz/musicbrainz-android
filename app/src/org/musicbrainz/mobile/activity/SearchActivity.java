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
import org.musicbrainz.mobile.activity.base.DataQueryActivity;
import org.musicbrainz.mobile.adapter.SearchArtistAdapter;
import org.musicbrainz.mobile.adapter.SearchReleaseGroupAdapter;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;
import org.musicbrainz.mobile.task.SearchAllTask;
import org.musicbrainz.mobile.task.SearchArtistsTask;
import org.musicbrainz.mobile.task.SearchRGsTask;
import org.musicbrainz.mobile.task.MusicBrainzTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends DataQueryActivity {

    private String searchQuery;
    private SearchType searchType;

    private LinkedList<ArtistStub> artistSearchResults;
    private LinkedList<ReleaseGroupStub> rgSearchResults;

    private MusicBrainzTask searchTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        setContentView(R.layout.activity_searchres);
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            getIntent().putExtra(Extra.TYPE, Extra.ALL);
        } else {
            searchQuery = getIntent().getStringExtra(Extra.QUERY);
        }
        setHeaderText();
        doSearch();
    }

    private void setHeaderText() {
        TextView resultsText = (TextView) findViewById(R.id.searchres_text);
        resultsText.setText(searchQuery);
    }

    private void doSearch() {
        configureSearch();
        switch (searchType) {
        case ARTIST:
            // TODO set title
            //actionBar.setTitle(R.string.search_bar_artists);
            searchTask = new SearchArtistsTask(this);
            break;
        case RELEASE_GROUP:
            // TODO set title
            //actionBar.setTitle(R.string.search_bar_rgs);
            searchTask = new SearchRGsTask(this);
            break;
        case ALL:
            searchTask = new SearchAllTask(this);
        }
        searchTask.execute(searchQuery);
    }

    private void configureSearch() {
        String intentType = getIntent().getStringExtra(Extra.TYPE);
        if (intentType.equals(Extra.ARTIST)) {
            searchType = SearchType.ARTIST;
        } else if (intentType.equals(Extra.RELEASE_GROUP)) {
            searchType = SearchType.RELEASE_GROUP;
        } else {
            searchType = SearchType.ALL;
        }
    }
    
    public void onTaskFinished() {
        toggleLoading();
        if (searchTask.failed()) {
            showDialog(DIALOG_CONNECTION_FAILURE);
        } else {
            handleResults();
        }
    }
    
    private void handleResults() {
        if (searchType == SearchType.ARTIST) {
            SearchArtistsTask task = (SearchArtistsTask) searchTask;
            artistSearchResults = task.getArtistResults();
            displayArtistResultsView();
        } else if (searchType == SearchType.RELEASE_GROUP) {
            SearchRGsTask task = (SearchRGsTask) searchTask;
            rgSearchResults = task.getReleaseGroupResults();
            displayRGResultsView();
        } else {
            SearchAllTask task = (SearchAllTask) searchTask;
            artistSearchResults = task.getArtistResults();
            rgSearchResults = task.getReleaseGroupResults();
            //enableTypeNavigation();
            displayArtistResultsView();
            displayRGResultsView();
        }
    }

    // TODO ????
//    private void enableTypeNavigation() {
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        SpinnerAdapter listAdapter = ArrayAdapter.createFromResource(SearchActivity.this, R.array.searchBar,
//                R.layout.actionbar_list_dropdown_item);
//        actionBar.setListNavigationCallbacks(listAdapter, new ActionBarListListener());
//    }
//
//    private class ActionBarListListener implements ActionBar.OnNavigationListener {
//
//        @Override
//        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//
//            RelativeLayout artistResults = (RelativeLayout) findViewById(R.id.artist_results_container);
//            RelativeLayout rgResults = (RelativeLayout) findViewById(R.id.rg_results_container);
//            if (itemPosition == 0) {
//                artistResults.setVisibility(View.VISIBLE);
//                rgResults.setVisibility(View.GONE);
//            } else if (itemPosition == 1) {
//                rgResults.setVisibility(View.VISIBLE);
//                artistResults.setVisibility(View.GONE);
//            }
//            return true;
//        }
//    }

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
            suggestions.saveRecentQuery(searchQuery, null);
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
            doSearch();
            dialog.cancel();
            toggleLoading();
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

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL
    }

}

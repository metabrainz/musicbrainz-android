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

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.SearchArtistAdapter;
import org.musicbrainz.mobile.adapter.SearchReleaseGroupAdapter;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Log;

import com.markupartist.android.widget.ActionBar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
public class SearchActivity extends SuperActivity {

    public static final String INTENT_TYPE = "type";
    public static final String INTENT_QUERY = "term";
    public static final String INTENT_ARTIST = "artist";
    public static final String INTENT_RELEASE_GROUP = "rg";
    public static final String INTENT_ALL = "all";

    private String searchQuery;
    private SearchType searchType;

    private ActionBar actionBar;

    private LinkedList<ArtistStub> artistSearchResults;
    private LinkedList<ReleaseGroupStub> rgSearchResults;

    private WebClient webService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebClient(Config.USER_AGENT);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        setContentView(R.layout.activity_searchres);
        actionBar = setupActionBarWithHome();
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            getIntent().putExtra(INTENT_TYPE, INTENT_ALL);
        } else {
            searchQuery = getIntent().getStringExtra(INTENT_QUERY);
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
            actionBar.setTitle(R.string.search_bar_artists);
            new ArtistSearchTask().execute();
            break;
        case RELEASE_GROUP:
            actionBar.setTitle(R.string.search_bar_rgs);
            new RGSearchTask().execute();
            break;
        case ALL:
            new MultiSearchTask().execute();
        }
    }

    private void configureSearch() {
        String intentType = getIntent().getStringExtra(INTENT_TYPE);
        if (intentType.equals(INTENT_ARTIST)) {
            searchType = SearchType.ARTIST;
        } else if (intentType.equals(INTENT_RELEASE_GROUP)) {
            searchType = SearchType.RELEASE_GROUP;
        } else {
            searchType = SearchType.ALL;
        }
    }

    private class ArtistSearchTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;
            try {
                artistSearchResults = webService.searchArtists(searchQuery);
            } catch (IOException e) {
                success = false;
            }
            return success;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                findViewById(R.id.topline).setVisibility(View.VISIBLE);
                displayArtistResultsView();
            } else {
                displayErrorDialog();
            }
            toggleLoading();
        }
    }

    private class RGSearchTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;
            try {
                rgSearchResults = webService.searchReleaseGroup(searchQuery);
            } catch (IOException e) {
                success = false;
            }
            return success;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                findViewById(R.id.topline).setVisibility(View.VISIBLE);
                displayRGResultsView();
            } else {
                displayErrorDialog();
            }
            toggleLoading();
        }
    }

    private class MultiSearchTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean success = true;
            try {
                artistSearchResults = webService.searchArtists(searchQuery);
                rgSearchResults = webService.searchReleaseGroup(searchQuery);
            } catch (IOException e) {
                success = false;
            }
            return success;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                SpinnerAdapter listAdapter = ArrayAdapter.createFromResource(SearchActivity.this, R.array.searchBar,
                        R.layout.actionbar_list_dropdown_item);
                actionBar.setListNavigationCallbacks(listAdapter, new ActionBarListListener());
                displayMultiResults();
            } else {
                displayErrorDialog();
            }
            toggleLoading();
        }

        private void displayMultiResults() {
            displayArtistResultsView();
            displayRGResultsView();
        }
    }

    private class ActionBarListListener implements ActionBar.OnNavigationListener {

        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {

            RelativeLayout artistResults = (RelativeLayout) findViewById(R.id.artist_results_container);
            RelativeLayout rgResults = (RelativeLayout) findViewById(R.id.rg_results_container);
            if (itemPosition == 0) {
                artistResults.setVisibility(View.VISIBLE);
                rgResults.setVisibility(View.GONE);
            } else if (itemPosition == 1) {
                rgResults.setVisibility(View.VISIBLE);
                artistResults.setVisibility(View.GONE);
            }
            return true;
        }
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
            suggestions.saveRecentQuery(searchQuery, null);
        }
    }

    private void displayErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new ErrorPositiveListener());
        builder.setNegativeButton(getString(R.string.err_neg), new ErrorNegativeListener());
        try {
            Dialog conError = builder.create();
            conError.show();
        } catch (Exception e) {
            Log.v("Connection timed out but Activity has closed anyway");
        }
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
        RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
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
            ArtistStub stub = (ArtistStub) artistSearchResults.get(position);
            artistIntent.putExtra(ArtistActivity.INTENT_MBID, stub.getMbid());
            startActivity(artistIntent);
        }
    }

    private class RGItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ReleaseGroupStub rg = (ReleaseGroupStub) rgSearchResults.get(position);
            if (rg.getNumberOfReleases() == 1) {
                startReleaseActivity(rg.getReleaseMbids().getFirst());
            } else {
                startRGActivity(rg.getMbid());
            }
        }

        private void startRGActivity(String mbid) {
            Intent releaseIntent = new Intent(SearchActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra(ReleaseActivity.INTENT_RG_MBID, mbid);
            startActivity(releaseIntent);
        }

        private void startReleaseActivity(String mbid) {
            Intent releaseIntent = new Intent(SearchActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra(ReleaseActivity.INTENT_RELEASE_MBID, mbid);
            startActivity(releaseIntent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL
    }

}

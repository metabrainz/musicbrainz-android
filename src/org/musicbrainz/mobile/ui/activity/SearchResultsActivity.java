/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.ui.activity;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.ArtistStub;
import org.musicbrainz.mobile.data.ReleaseGroup;
import org.musicbrainz.mobile.ui.util.ArtistSearchAdapter;
import org.musicbrainz.mobile.ui.util.RGSearchAdapter;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.ws.WebService;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class SearchResultsActivity extends SuperActivity {
	
	public static final String INTENT_TYPE = "type";
	public static final String INTENT_QUERY = "term";
	public static final String INTENT_ARTIST = "artist";
	public static final String INTENT_RELEASE_GROUP = "rg";
	public static final String INTENT_ALL = "all";
	
	private String searchTerm;
	private SearchType searchType;
	
	private LinkedList<ArtistStub> artistSearchResults;
	private LinkedList<ReleaseGroup> rgSearchResults;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_searchres);
        setupActionBarWithHome();
   
        searchTerm = getIntent().getStringExtra(INTENT_QUERY);
        configureSearch();
        doSearch();
	}

	private void configureSearch() {
		String intentType = getIntent().getStringExtra(INTENT_TYPE);
        if (intentType.equals(INTENT_ARTIST)) {
        	searchType = SearchType.ARTIST;
        	setHeaderText(R.string.search_artist);
        } else if (intentType.equals(INTENT_RELEASE_GROUP)) {
        	searchType = SearchType.RELEASE_GROUP;
        	setHeaderText(R.string.search_release);
        } else if (intentType.equals(INTENT_ALL)) {
        	searchType = SearchType.ALL;
        	setHeaderText(R.string.search_all);
        }
	}
	
	private void setHeaderText(int searchNameResource) {
		TextView resultsText = (TextView) findViewById(R.id.searchres_text);
		resultsText.setText(getString(searchNameResource) + " '" + searchTerm + "'");
	}
	
	private void doSearch() {
		switch (searchType) {
		case ARTIST:
			new ArtistSearchTask().execute();
			break;
		case RELEASE_GROUP:
			new RGSearchTask().execute();
			break;
		case ALL:
			new MultiSearchTask().execute();
		}
	}
	
	private class ArtistSearchTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean success = true;
			try {
				artistSearchResults = WebService.searchArtist(searchTerm);
			} catch (IOException e) {
				success = false;
			} catch (SAXException e) {
				success = false;
			}
			return success;
		}
		
		protected void onPostExecute(Boolean success) {
			if (success) {
				findViewById(R.id.topline).setVisibility(View.VISIBLE);
				displayArtistResultsView(R.id.searchres_list, R.id.noresults);
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
				rgSearchResults = WebService.searchReleaseGroup(searchTerm);
			} catch (IOException e) {
				success = false;
			} catch (SAXException e) {
				success = false;
			}
			return success;
		}
		
		protected void onPostExecute(Boolean success) {
			if (success) {
				findViewById(R.id.topline).setVisibility(View.VISIBLE);
				displayRGResultsView(R.id.searchres_list, R.id.noresults);
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
				artistSearchResults = WebService.searchArtist(searchTerm);
				rgSearchResults = WebService.searchReleaseGroup(searchTerm);
			} catch (IOException e) {
				success = false;
			} catch (SAXException e) {
				success = false;
			}
			return success;
		}
		
		protected void onPostExecute(Boolean success) {
			if (success) {
				displayMultiResults();
			} else {
				displayErrorDialog();
			}
			toggleLoading();
		}
		
		private void displayMultiResults() {
			setupTabs();
			displayArtistResultsView(R.id.searchres_artist_list, R.id.no_artists);
			displayRGResultsView(R.id.searchres_rg_list, R.id.no_rgs);
		}
		
		private void setupTabs() {
			LinearLayout allResults = (LinearLayout) findViewById(R.id.all_results);
			allResults.setVisibility(View.VISIBLE);
			TabHost host = (TabHost) findViewById(R.id.searchres_tabhost);
			host.setup();
			setupArtistsTab(host);
			setupRGsTab(host);
		}

		private void setupRGsTab(TabHost tabs) {
			TabSpec rgsTab = tabs.newTabSpec("rgs");
			final TextView rgsIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
			rgsIndicator.setText(R.string.tab_rgs);
			rgsTab.setIndicator(rgsIndicator);
			rgsTab.setContent(R.id.rgs_tab);
			tabs.addTab(rgsTab);
		}

		private void setupArtistsTab(TabHost tabs) {
			TabSpec artistsTab = tabs.newTabSpec("artists");
			final TextView artistsIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
			artistsIndicator.setText(R.string.tab_artists);
			artistsTab.setIndicator(artistsIndicator);
			artistsTab.setContent(R.id.artists_tab);
			tabs.addTab(artistsTab);
		}
		
	}
	
	private void displayArtistResultsView(int listViewId, int noResultsId) {
		ListView artistResultsView = (ListView) findViewById(listViewId);
		artistResultsView.setAdapter(new ArtistSearchAdapter(SearchResultsActivity.this, artistSearchResults));
		artistResultsView.setOnItemClickListener(new ArtistItemClickListener());
		artistResultsView.setVisibility(View.VISIBLE);
		
		if (artistSearchResults.isEmpty()) {
			TextView noRes = (TextView) findViewById(noResultsId);
			noRes.setVisibility(View.VISIBLE);
		}
	}
	
	private void displayRGResultsView(int listViewId, int noResultsId) {
		ListView rgResultsView = (ListView) findViewById(listViewId);
		rgResultsView.setAdapter(new RGSearchAdapter(SearchResultsActivity.this, rgSearchResults));
		rgResultsView.setOnItemClickListener(new RGItemClickListener());
		rgResultsView.setVisibility(View.VISIBLE);

		if (rgSearchResults.isEmpty()) {
			TextView noResults = (TextView) findViewById(noResultsId);
			noResults.setVisibility(View.VISIBLE);
		}
	}
	
	private void displayErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);
		builder.setMessage(getString(R.string.err_text));
		builder.setCancelable(false);
		builder.setPositiveButton(getString(R.string.err_pos), new ErrorPositiveListener());
		builder.setNegativeButton(getString(R.string.err_neg), new ErrorNegativeListener());
		try {
			Dialog conError = builder.create();
			conError.show();
		} catch (Exception e) {
			Log.e("Connection timed out but Activity has closed anyway");
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
			SearchResultsActivity.this.finish();
		}
	}
	
	private void toggleLoading() {
		LinearLayout loading = (LinearLayout) findViewById(R.id.loading);
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
			Intent artistIntent = new Intent (SearchResultsActivity.this, ArtistActivity.class);
			ArtistStub stub = (ArtistStub) artistSearchResults.get(position);
			artistIntent.putExtra(ArtistActivity.INTENT_MBID, stub.getMbid());
			startActivity(artistIntent);
		}
		
	}
	
	private class RGItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ReleaseGroup rg = (ReleaseGroup) rgSearchResults.get(position);
			if (rg.isSingleRelease()) {
				startReleaseActivity(rg.getSingleReleaseMbid());
			} else {
				startRGActivity(rg.getMbid());
			}
		}

		private void startRGActivity(String mbid) {
			Intent releaseIntent = new Intent (SearchResultsActivity.this, ReleaseActivity.class);
			releaseIntent.putExtra(ReleaseActivity.INTENT_RG_MBID, mbid);
			startActivity(releaseIntent);
		}

		private void startReleaseActivity(String mbid) {
			Intent releaseIntent = new Intent (SearchResultsActivity.this, ReleaseActivity.class);
			releaseIntent.putExtra(ReleaseActivity.INTENT_RELEASE_MBID, mbid);
			startActivity(releaseIntent);
		}
		
	}
	
	@Override
	public boolean onSearchRequested() {
		startActivity(HomeActivity.createIntent(this));
	    return false;
	}
	
	public enum SearchType {
		ARTIST,
		RELEASE_GROUP,
		ALL
	}

}

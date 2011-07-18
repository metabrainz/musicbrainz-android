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

package org.musicbrainz.mobile.ui.activities;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.ArtistStub;
import org.musicbrainz.mobile.data.ReleaseGroup;
import org.musicbrainz.mobile.ui.util.ArtistSearchAdapter;
import org.musicbrainz.mobile.ui.util.ReleaseSearchAdapter;
import org.musicbrainz.mobile.ws.WebService;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class SearchResultsActivity extends SuperActivity implements ListView.OnItemClickListener {
	
	private String searchTerm;
	private SearchType searchType;
	
	private ListView results;
	
	private LinkedList<?> searchResults;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_searchres);
        
        int type = getIntent().getIntExtra("type", 0);
        searchTerm = getIntent().getStringExtra("term");
        
        TextView resultsText = (TextView) findViewById(R.id.searchres_text);
        results = (ListView) findViewById(R.id.searchres_list);
        
        // spinner selection position as search type code
        switch (type) {
        case 0:
        	searchType = SearchType.ARTIST;
        	resultsText.setText(getString(R.string.search_artist) + " '" + searchTerm + "'");
        	break;
        case 1:
        	searchType = SearchType.RELEASE_GROUP;
        	resultsText.setText(getString(R.string.search_release) + " '" + searchTerm + "'");

        }
        
        new SearchTask().execute();
	}
	
	/**
	 * Task to perform search through the webservice and display results.
	 */
	private class SearchTask extends AsyncTask<Void, Void, Integer> {
		
		private static final int ARTIST_RESULTS = 0;
		private static final int RG_RESULTS = 1;
		private static final int ERROR = 2;
		
		ProgressDialog pd;

		protected void onPreExecute() {
	        pd = new ProgressDialog(SearchResultsActivity.this) {
	        	public void cancel() {
	        		super.cancel();
	        		SearchTask.this.cancel(true);
	        		SearchResultsActivity.this.finish();
	        	}
	        };
	        pd.setMessage(getText(R.string.pd_searching));
	        pd.setCancelable(true);
	        pd.show();	
		}
		
		protected Integer doInBackground(Void... params) {

			try {
				switch (searchType) {
				case ARTIST:
					searchResults = WebService.searchArtist(searchTerm);
					return ARTIST_RESULTS;
				case RELEASE_GROUP:
					searchResults = WebService.searchReleaseGroup(searchTerm);
					return RG_RESULTS;
				}
			} catch (IOException e) {
				return ERROR;
			} catch (SAXException e) {
				return ERROR;
			}
			return ERROR;
		}
		
		protected void onPostExecute(Integer resultCode) {
			
			switch (resultCode) {
			case ARTIST_RESULTS:
				// result returned
				@SuppressWarnings("unchecked")
				LinkedList<ArtistStub> rs = (LinkedList<ArtistStub>) searchResults;
				
				results.setAdapter(new ArtistSearchAdapter(SearchResultsActivity.this, rs));
				results.setOnItemClickListener(SearchResultsActivity.this);
				results.setVisibility(View.VISIBLE);
				
				if (rs.isEmpty()) {
					LinearLayout noRes = (LinearLayout) findViewById(R.id.noresults);
					noRes.setVisibility(View.VISIBLE);
				}
				pd.dismiss();
				break;
			case RG_RESULTS:
				
				@SuppressWarnings("unchecked")
				LinkedList<ReleaseGroup> rgs = (LinkedList<ReleaseGroup>) searchResults;
				
				results.setAdapter(new ReleaseSearchAdapter(SearchResultsActivity.this, rgs));
				results.setOnItemClickListener(SearchResultsActivity.this);
				results.setVisibility(View.VISIBLE);
				
				if (rgs.isEmpty()) {
					LinearLayout noRes = (LinearLayout) findViewById(R.id.noresults);
					noRes.setVisibility(View.VISIBLE);
				}
				pd.dismiss();
				break;
			case ERROR:
				// error or connection timed out - retry dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchResultsActivity.this);
				builder.setMessage(
						getString(R.string.err_text))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.err_pos),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// new search thread
										new SearchTask().execute();
										dialog.cancel();
									}
								})
						.setNegativeButton(getString(R.string.err_neg),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// finish activity
										SearchResultsActivity.this.finish();
									}
								});
				Dialog conError = builder.create();
				pd.dismiss();
				conError.show();
			}
		}
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		switch (searchType) {
		case ARTIST:
			Intent artistIntent = new Intent (SearchResultsActivity.this, ArtistActivity.class);
			
			ArtistStub stub = (ArtistStub) searchResults.get(position);
			artistIntent.putExtra("mbid", stub.getMbid());
			startActivity(artistIntent);
			break;
		case RELEASE_GROUP:
			ReleaseGroup r = (ReleaseGroup) searchResults.get(position);
			if (r.getNumberReleases() == 1) {
				// only one release in group - intent with release ID
				String releaseID = r.getSingleReleaseMbid();
				Intent releaseIntent = new Intent (SearchResultsActivity.this, ReleaseActivity.class);
				releaseIntent.putExtra("r_id", releaseID);
				startActivity(releaseIntent);
			} else {
				// multiple releases - intent with rg ID
				Intent releaseIntent = new Intent (SearchResultsActivity.this, ReleaseActivity.class);
				releaseIntent.putExtra("rg_id", r.getMbid());
				startActivity(releaseIntent);
			}
		}
	}
	
	private enum SearchType {
		ARTIST,
		RELEASE_GROUP
	}

}

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

package org.musicbrainz.mobile.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.ws.BarcodeNotFoundException;
import org.musicbrainz.android.api.ws.MBEntity;
import org.musicbrainz.android.api.ws.UserService;
import org.musicbrainz.android.api.ws.WebService;
import org.musicbrainz.android.api.ws.WebServiceUtils;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.ReleaseTrackAdapter;
import org.musicbrainz.mobile.dialog.BarcodeResultDialog;
import org.musicbrainz.mobile.dialog.ReleaseSelectionDialog;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.widget.FocusTextView;
import org.xml.sax.SAXException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * Activity which retrieves and displays information about a release.
 * 
 * This Activity initiates lookups given three sources that generally result in
 * display of release information. An intent will contain either a barcode, a
 * release MBID or a release group MBID.
 */
public class ReleaseActivity extends SuperActivity implements View.OnClickListener {
		
	public static final String INTENT_RELEASE_MBID = "r_id";
	public static final String INTENT_RG_MBID = "rg_id";
	public static final String INTENT_BARCODE = "barcode";

	private Release data;
	
	// query data
	private LookupSource src;
	private String releaseMbid;
	private String releaseGroupMbid;
	private LinkedList<ReleaseStub> stubs;
	private String barcode;
	
	private ActionBar actionBar;
	
	// refreshables
	private FocusTextView tags;
	private RatingBar rating;
	
	// input
	private EditText tagInput;
	private RatingBar ratingInput;
	
	// edit buttons
	private Button tagBtn;
	private Button rateBtn;
	
	private UserService user;
	private UserData userData;
	
	// status
	private boolean doingTag = false;
	private boolean doingRate = false;
	
	private WebService webService;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebService();
        releaseMbid = getIntent().getStringExtra(INTENT_RELEASE_MBID);
        releaseGroupMbid = getIntent().getStringExtra(INTENT_RG_MBID);
        barcode = getIntent().getStringExtra(INTENT_BARCODE);
        
        new LookupTask().execute();
        setContentView(R.layout.loading);
        setupActionBarWithHome();
    }
    
	/**
	 * Set the content view and options associated with the information mode.
	 */
	private void populate() {
		
		setContentView(R.layout.activity_release);
		actionBar = setupActionBarWithHome();
		addActionBarShare();

		FocusTextView artist = (FocusTextView) findViewById(R.id.release_artist);
		FocusTextView title = (FocusTextView) findViewById(R.id.release_release);
		FocusTextView labels = (FocusTextView) findViewById(R.id.release_label);
		TextView releaseDate = (TextView) findViewById(R.id.release_date);
		
		rating = (RatingBar) findViewById(R.id.release_rating);
		tags = (FocusTextView) findViewById(R.id.release_tags);
        
		Boolean provideArtistAction = true;
		
		ArrayList<ReleaseArtist> releaseArtists = data.getArtists();
		if (releaseArtists.size() > 1) {
			provideArtistAction = false;
		} else {
			ReleaseArtist singleArtist = releaseArtists.get(0);
	        for (String id : Artist.SPECIAL_PURPOSE) {
	        	if (singleArtist.getMbid().equals(id)) {
	        		provideArtistAction = false;
	        	}
	        }
		}
        
        if (provideArtistAction) {
        	addActionBarArtist();
        }
        
		artist.setText(data.getFormattedArtist());
		title.setText(data.getTitle());
		
		tags.setText(data.getReleaseGroupTags());
		rating.setRating(data.getReleaseGroupRating());
		
		ListView trackList = (ListView) findViewById(R.id.release_tracks);
		trackList.setAdapter(new ReleaseTrackAdapter(this, data.getTrackList()));
		trackList.setDrawSelectorOnTop(false);
		
		setupTabs();
		
		tagInput = (EditText) findViewById(R.id.tag_input);
		tagBtn = (Button) findViewById(R.id.tag_btn);
		tagBtn.setOnClickListener(this);
		
		ratingInput = (RatingBar) findViewById(R.id.rating_input);
		rateBtn = (Button) findViewById(R.id.rate_btn);
		rateBtn.setOnClickListener(this);
		
		labels.setText(data.getFormattedLabels());
		releaseDate.setText(data.getDate());
		
		if (data.getReleaseGroupTags() == "") {
			tags.setText(getText(R.string.no_tags));
		}

		if (!loggedIn) {
			disableEditFields();
			findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
		}
	}

	private void disableEditFields() {
		tagInput.setEnabled(false);
		tagInput.setFocusable(false);
		
		ratingInput.setEnabled(false);
		ratingInput.setFocusable(false);
		
		tagBtn.setEnabled(false);
		tagBtn.setFocusable(false);
		
		rateBtn.setEnabled(false);
		rateBtn.setFocusable(false);
	}
	
	private void addActionBarShare() {
		Action share = actionBar.newAction();
    	share.setIcon(R.drawable.ic_actionbar_share);
    	share.setIntent(createShareIntent());
    	actionBar.addAction(share);
	}
	
    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.musicbrainz.org/release/" + releaseMbid);
        return Intent.createChooser(intent, getString(R.string.share));
    }
	
	private void addActionBarArtist() {
		Action artist = actionBar.newAction();
    	artist.setIcon(R.drawable.ic_actionbar_artist);
    	artist.setIntent(createArtistIntent());
    	actionBar.addAction(artist);
	}
	
    private Intent createArtistIntent() {
    	final Intent releaseIntent = new Intent(this, ArtistActivity.class);
		releaseIntent.putExtra("mbid", data.getArtists().get(0).getMbid());
		return releaseIntent;
    }
	
	private void setupTabs() {
		TabHost tabs = (TabHost) findViewById(R.id.release_tabhost);
		tabs.setup();
	
		TabSpec tracksTab = tabs.newTabSpec("tracks");
		final TextView tracksIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
		tracksIndicator.setText(R.string.tab_tracks);
		tracksTab.setIndicator(tracksIndicator);
		tracksTab.setContent(R.id.tracks_tab);
		tabs.addTab(tracksTab);
		
		TabSpec editsTab = tabs.newTabSpec("edit");
		final TextView editIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
		editIndicator.setText(R.string.tab_edits);
		editsTab.setIndicator(editIndicator);
		editsTab.setContent(R.id.edit_tab);
		tabs.addTab(editsTab);
	}
	
	private void updateProgressStatus() {
		if (doingTag || doingRate) {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		} else {
			actionBar.setProgressBarVisibility(View.GONE);
		}
	}
	
	private class LookupTask extends AsyncTask<Void, Void, Integer> {
		
		/*
		 * Result codes:
		 * 
		 * 0: Release data retrieved from ID or barcode
		 * 1: Barcode not found
		 * 2: Release group retrieved
		 * 3: Problem with request - typically IOException
		 */
		private static final int LOADED = 0;
		private static final int BARCODE_NOT_FOUND = 1;
		private static final int RG_LOADED = 2;
		private static final int ERROR = 3;
		
		protected void onPreExecute() {
	        
	        if (releaseMbid != null) {
	        	src = LookupSource.RELEASE_MBID;
	        } else if (releaseGroupMbid != null) {
	        	src = LookupSource.RG_MBID;
	        } else {
	        	src = LookupSource.BARCODE;
	        }
		}
		
		protected Integer doInBackground(Void... params) {
			
			try {
				switch (src) {
				case RELEASE_MBID:
					doReleaseLookup();
					return LOADED;
				case BARCODE:
					try {
						data = webService.lookupReleaseFromBarcode(barcode);
						releaseMbid = data.getMbid();
						if (loggedIn) {
							user = getUser();
							userData = user.getUserData(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
							user.shutdownConnectionManager();
						}
						return LOADED;
					} catch (BarcodeNotFoundException e) {
						return BARCODE_NOT_FOUND;
					}
				case RG_MBID:
					stubs = webService.browseReleases(releaseGroupMbid);
					
					// lookup release if single release in release group
					if (stubs.size() == 1) {
						ReleaseStub r = stubs.getFirst();				
						releaseMbid = r.getReleaseMbid();
						doReleaseLookup();
						return LOADED;
					} else {
						return RG_LOADED;
					}
				}
			} catch (IOException e) {
				return ERROR;
			} catch (SAXException e) {
				return ERROR;
			}
			return ERROR;
		}

		private void doReleaseLookup() throws IOException, SAXException {
			data = webService.lookupRelease(releaseMbid);
			if (loggedIn) {
				user = getUser();
				userData = user.getUserData(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
				user.shutdownConnectionManager();
			}
		}
		
		protected void onPostExecute(Integer resultCode) {
			
			switch (resultCode) {
			case LOADED:
				displayReleaseData();
				break;
			case BARCODE_NOT_FOUND:
				displayBarcodeResultDialog();
				break;
			case RG_LOADED:
				displayReleaseSelectionDialog();
				break;
			case ERROR:
				displayErrorDialog();
			}
		}
		
		private void displayReleaseData() {
			populate();
			if (loggedIn) {
				tagInput.setText(userData.getTagString());
				ratingInput.setRating(userData.getRating());
			}
		}
		
		private void displayBarcodeResultDialog() {
			try {
				BarcodeResultDialog bcode = new BarcodeResultDialog(ReleaseActivity.this, loggedIn, barcode);
				bcode.setCancelable(true);
				bcode.show();
			} catch (Exception e) {
				Log.e("Barcode not found but Activity closed anyway");
			}
		}

		private void displayReleaseSelectionDialog() {
			try {
				ReleaseSelectionDialog rsd = new ReleaseSelectionDialog(ReleaseActivity.this, stubs);
				rsd.show();
			} catch (Exception e) {
				Log.e("Release groups loaded but Activity has closed");
			}
		}

		private void displayErrorDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ReleaseActivity.this);
			builder.setMessage(
					getString(R.string.err_text))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.err_pos),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// restart search thread
									new LookupTask().execute();
									dialog.cancel();
								}
							})
					.setNegativeButton(getString(R.string.err_neg),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// finish activity
									ReleaseActivity.this.finish();
								}
							});
			try{
				Dialog conError = builder.create();
				conError.show();
			} catch (Exception e) {
				Log.e("Connection timed out but Activity has closed anyway");
			}
		}
		
	}
	
	private class TagTask extends AsyncTask<String, Void, Boolean> {
		
		protected void onPreExecute() {
			doingTag = true;
			updateProgressStatus();
			tagBtn.setEnabled(false);
		}

		protected Boolean doInBackground(String... tags) {
			
			Collection<String> processedTags = WebServiceUtils.sanitiseCommaSeparatedTags(tags[0]);
			
			try {
				submitThenRefreshTags(processedTags);
			} catch (IOException e) {
				return false;
			} catch (SAXException e) {
				return true;
			}
			return true;
		}

		private void submitThenRefreshTags(Collection<String> processedTags) throws IOException, SAXException {
			user = getUser();
			user.submitTags(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid(), processedTags);
			data.setReleaseGroupTags(webService.lookupTags(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid()));
			user.shutdownConnectionManager();
		}
		
		protected void onPostExecute(Boolean success) {
			
			tags.setText(data.getReleaseGroupTags());
			doingTag = false;
			updateProgressStatus();
			tagBtn.setEnabled(true);
			if (success) {
				Toast.makeText(ReleaseActivity.this, R.string.toast_tag, Toast.LENGTH_SHORT); 
			} else {
				Toast.makeText(ReleaseActivity.this, R.string.toast_tag_fail, Toast.LENGTH_LONG);
			}
		}
		
	}

	private class RatingTask extends AsyncTask<Integer, Void, Boolean> {
		
		protected void onPreExecute() {
			doingRate = true;
			updateProgressStatus();
			rateBtn.setEnabled(false);
		}

		protected Boolean doInBackground(Integer... rating) {
		
			try {
				submitThenRefreshRating(rating);
			} catch (IOException e) {
				return false;
			} catch (SAXException e) {
				return true;
			}
			return true;
		}

		private void submitThenRefreshRating(Integer... rating) throws IOException, SAXException {
			user = getUser();
			user.submitRating(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid(), rating[0]);
			float newRating = webService.lookupRating(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
			data.setReleaseGroupRating(newRating);
			user.shutdownConnectionManager();
		}
		
		protected void onPostExecute(Boolean success) {
			
			rating.setRating(data.getReleaseGroupRating());
			doingRate = false;
			updateProgressStatus();
			rateBtn.setEnabled(true);
			if (success) {
				Toast.makeText(ReleaseActivity.this, R.string.toast_rate, Toast.LENGTH_SHORT).show(); 
			} else {
				Toast.makeText(ReleaseActivity.this, R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
			}
		}

	}
	
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.tag_btn:
			
			String tagString = tagInput.getText().toString();
			if (tagString.length() == 0) {
				Toast tagMessage = Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT);
				tagMessage.show();
			} else {
				new TagTask().execute(tagString);	
			}
			break;
		case R.id.rate_btn:
			
			int rating = (int) ratingInput.getRating();
			new RatingTask().execute(rating);
		}
		
	}

	private enum LookupSource {
		RELEASE_MBID,
		RG_MBID,
		BARCODE
	}

}

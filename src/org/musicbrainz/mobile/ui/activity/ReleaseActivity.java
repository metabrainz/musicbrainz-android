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
import java.util.Collection;
import java.util.LinkedList;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.Artist;
import org.musicbrainz.mobile.data.Release;
import org.musicbrainz.mobile.data.ReleaseStub;
import org.musicbrainz.mobile.data.Track;
import org.musicbrainz.mobile.data.UserData;
import org.musicbrainz.mobile.ui.dialog.BarcodeResultDialog;
import org.musicbrainz.mobile.ui.dialog.ReleaseSelectionDialog;
import org.musicbrainz.mobile.ui.util.FocusTextView;
import org.musicbrainz.mobile.ws.WebService;
import org.musicbrainz.mobile.ws.WSUser;
import org.musicbrainz.mobile.ws.WebService.MBEntity;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * release ID or a release group ID.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseActivity extends SuperActivity implements View.OnClickListener {
		
	// data object
	private Release data;
	
	// query data
	private Source src;
	private String releaseID;
	private String releaseGroupID;
	private LinkedList<ReleaseStub> stubs;
	private String barcode;
	
	// refreshables
	private FocusTextView tags;
	private RatingBar rating;
	
	// input
	private EditText tagInput;
	private RatingBar ratingInput;
	
	// edit buttons
	private Button tagBtn;
	private Button rateBtn;
	
	private WSUser user;
	private UserData userData;
	
	// status
	private boolean doingTag = false;
	private boolean doingRate = false;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // recover ID or barcode from intent
        releaseID = getIntent().getStringExtra("r_id");
        releaseGroupID = getIntent().getStringExtra("rg_id");
        barcode = getIntent().getStringExtra("barcode");
        
        new LookupTask().execute();
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
        
        setContentView(R.layout.blank);
    }
    
	/**
	 * Set the content view and options associated with the information mode.
	 */
	private void populate() {
		
		setContentView(R.layout.activity_release);
		
		// info header
		
		FocusTextView artist = (FocusTextView) findViewById(R.id.release_artist);
		FocusTextView title = (FocusTextView) findViewById(R.id.release_release);
		FocusTextView labels = (FocusTextView) findViewById(R.id.release_label);
		TextView releaseDate = (TextView) findViewById(R.id.release_date);
		
		rating = (RatingBar) findViewById(R.id.release_rating);
		tags = (FocusTextView) findViewById(R.id.release_tags);
		
		ImageButton artistInfo = (ImageButton) findViewById(R.id.release_artist_btn);
        artistInfo.setOnClickListener(this);
        
        // disable artist button for ignored artists (e.g. VA arist)
        for (String id : Artist.IGNORE_LIST)
        	if (data.getArtistMbid().equals(id)) {
        		artistInfo.setEnabled(false);
        		artistInfo.setFocusable(false);
        	}
		
		artist.setText(data.getArtistName());
		title.setText(data.getTitle());
		
		tags.setText(data.getTags());
		rating.setRating(data.getRating());
		
		ListView trackList = (ListView) findViewById(R.id.release_tracks);
		trackList.setAdapter(new TrackAdapter());
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
		
		// hide release data row if empty
		if (data.getFormattedLabels() == "" && data.getDate() == "") {
			labels.setVisibility(View.GONE);
			releaseDate.setVisibility(View.GONE);
		}
		
		// notify user if no tags are returned
		if (data.getTags() == "")
			tags.setText(getText(R.string.no_tags));
		
		// disable edit options if not logged in
		if (!loggedIn) {
			
			tagInput.setEnabled(false);
			tagInput.setFocusable(false);
			
			ratingInput.setEnabled(false);
			ratingInput.setFocusable(false);
			
			tagBtn.setEnabled(false);
			tagBtn.setFocusable(false);
			
			rateBtn.setEnabled(false);
			rateBtn.setFocusable(false);
			
			findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
		}
	}
	
	/*
	 * Create and add tabs.
	 */
	private void setupTabs() {
		
		TabHost tabs = (TabHost) this.findViewById(R.id.release_tabhost);
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
	
	/*
	 * Refresh the background task status indicator.
	 */
	private void updateProgress() {
		if (doingTag || doingRate)
			setProgressBarIndeterminateVisibility(true);
		else
			setProgressBarIndeterminateVisibility(false);
	}
	
	/**
	 * Task to submit user tags and refresh page tags list.
	 */
	private class TagTask extends AsyncTask<String, Void, Boolean> {
		
		protected void onPreExecute() {
			doingTag = true;
			updateProgress();
			tagBtn.setEnabled(false);
		}

		protected Boolean doInBackground(String... tags) {
			
			Collection<String> processedTags = WSUser.processTags(tags[0]);
			
			user = getUser();
			try {
				user.submitTags(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid(), processedTags);
				data.setTags(WebService.refreshTags(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid()));
				user.shutdown();
			} catch (IOException e) {
				return false;
			} catch (SAXException e) {
				return true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean success) {
			
			tags.setText(data.getTags());
			
			doingTag = false;
			updateProgress();
			tagBtn.setEnabled(true);
			
			Toast tagMessage;
			if (success) 
				tagMessage = Toast.makeText(ReleaseActivity.this, R.string.toast_tag, Toast.LENGTH_SHORT); 
			else 
				tagMessage = Toast.makeText(ReleaseActivity.this, R.string.toast_tag_fail, Toast.LENGTH_SHORT);
			tagMessage.show();
		}
		
	}
	
	/**
	 * Task to submit user rating and refresh page rating.
	 */
	private class RatingTask extends AsyncTask<Integer, Void, Boolean> {
		
		protected void onPreExecute() {
			doingRate = true;
			updateProgress();
			rateBtn.setEnabled(false);
		}

		protected Boolean doInBackground(Integer... rating) {
			
			user = getUser();
			try {
				user.submitRating(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid(), rating[0]);
				float newRating = WebService.refreshRating(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
				data.setRating(newRating);
				user.shutdown();
			} catch (IOException e) {
				return false;
			} catch (SAXException e) {
				return true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean success) {
			
			rating.setRating(data.getRating());
			
			doingRate = false;
			updateProgress();
			rateBtn.setEnabled(true);
			
			Toast rateMessage;
			if (success) 
				rateMessage = Toast.makeText(ReleaseActivity.this, R.string.toast_rate, Toast.LENGTH_SHORT); 
			else
				rateMessage = Toast.makeText(ReleaseActivity.this, R.string.toast_rate_fail, Toast.LENGTH_SHORT);
			
			rateMessage.show();
		}

	}
	
	/**
	 * Task to retrieve data from webservice and populate page.
	 */
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
		private static final int NOT_FOUND = 1;
		private static final int RG_LOADED = 2;
		private static final int ERROR = 3;

		private ProgressDialog pd;
		
		protected void onPreExecute() {
	        pd = new ProgressDialog(ReleaseActivity.this) {
	        	public void cancel() {
	        		super.cancel();
	        		LookupTask.this.cancel(true);
	        		ReleaseActivity.this.finish();
	        	}
	        };
	        
	        if (releaseID != null) {
	        	src = Source.RELEASE_ID;
	        	pd.setMessage(getText(R.string.pd_loading));
	        } else if (releaseGroupID != null) {
	        	src = Source.RG_ID;
	        	pd.setMessage(getText(R.string.pd_loading));
	        } else {
	        	src = Source.BARCODE;
	    		pd.setMessage(getText(R.string.pd_searching_bc));
	        }
	        
	        pd.setCancelable(true);
	        pd.show();
		}
		
		protected Integer doInBackground(Void... params) {
			
			try {
				switch (src) {
				case RELEASE_ID:
					data = WebService.lookupRelease(releaseID);
					if (loggedIn) {
						user = getUser();
						userData = user.getUserData(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
						user.shutdown();
					}
					return LOADED;
				case BARCODE:
					data = WebService.lookupBarcode(barcode);
					if (data != null) { // barcode found
						if (loggedIn) {
							user = getUser();
							userData = user.getUserData(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
							user.shutdown();
						}
						return LOADED;
					} else {
						return NOT_FOUND;
					}
				case RG_ID:
					stubs = WebService.browseReleases(releaseGroupID);
					
					// lookup release if single release in release group
					if (stubs.size() == 1) {
						ReleaseStub r = stubs.getFirst();				
						releaseID = r.getReleaseMbid();
						data = WebService.lookupRelease(releaseID);
						if (loggedIn) {
							user = getUser();
							userData = user.getUserData(MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
							user.shutdown();
						}
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
		
		protected void onPostExecute(Integer resultCode) {
			
			switch (resultCode) {
			case LOADED:
				// display release data
				populate();
				if (loggedIn) {
					tagInput.setText(userData.getTagString());
					ratingInput.setRating(userData.getRating());
				}
				pd.dismiss();
				break;
			case NOT_FOUND:
				// barcode not found dialog
				BarcodeResultDialog bcode = new BarcodeResultDialog(ReleaseActivity.this, loggedIn, barcode);
				bcode.setCancelable(true);
				pd.dismiss();
				bcode.show();
				break;
			case RG_LOADED:
				// display release selection dialog
				ReleaseSelectionDialog rsd = new ReleaseSelectionDialog(ReleaseActivity.this, stubs);
				pd.dismiss();
				rsd.show();
				break;
			case ERROR:
				// error or connection timed out - retry dialog
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
				Dialog conError = builder.create();
				pd.dismiss();
				conError.show();
			}
		}
		
	}
    
    /**
     * Listener for edit and artist button clicks.
     */
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.release_artist_btn:
			
			Intent releaseIntent = new Intent(this, ArtistActivity.class);
			System.err.println(data.getArtistMbid());
			releaseIntent.putExtra("mbid", data.getArtistMbid());
			startActivity(releaseIntent);
			break;
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
    
	/**
	 * Adapter for list of tracks.
	 */
	private class TrackAdapter extends ArrayAdapter<Track> {
		
		TrackAdapter() {
			super(ReleaseActivity.this, R.layout.list_track, data.getTrackList());
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View track = convertView; // reuse View
			TrackListWrapper wrapper = null;
			
			if (track == null) {
				LayoutInflater inflater = getLayoutInflater();
				track = inflater.inflate(R.layout.list_track, parent, false);
				
				wrapper = new TrackListWrapper(track);
				track.setTag(wrapper);
			} else {
				wrapper = (TrackListWrapper) track.getTag();
			}
			
			Track tData = data.getTrackList().get(position);
			
			wrapper.getTrackNum().setText("" + tData.getPosition() + ".");
			wrapper.getTrackName().setText(tData.getTitle());
			wrapper.getTrackTime().setText(tData.getFormattedDuration());

			return track;
		}
		
	}
	
	/**
	 * Holder pattern minimises executions of findViewById().
	 */
	private class TrackListWrapper {
		
		View base;
		TextView trackNum = null;
		TextView trackName = null;
		TextView trackTime = null;
		
		TrackListWrapper(View base) {
			this.base = base;
		}
		
		TextView getTrackNum() {
			if (trackNum == null)
				trackNum = (TextView) base.findViewById(R.id.list_track_num);
			
			return trackNum;
		}
		
		TextView getTrackName() {
			if (trackName == null)
				trackName = (TextView) base.findViewById(R.id.list_track_name);
			
			return trackName;
		}
		
		TextView getTrackTime() {
			if (trackTime == null)
				trackTime = (TextView) base.findViewById(R.id.list_track_time);
			
			return trackTime;
		}
	}
	
	/**
	 * Type of data used to retrieve entity data.
	 */
	private enum Source {
		RELEASE_ID,
		RG_ID,
		BARCODE
	}
	
}

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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.Artist;
import org.musicbrainz.mobile.data.ReleaseGroup;
import org.musicbrainz.mobile.data.UserData;
import org.musicbrainz.mobile.data.WebLink;
import org.musicbrainz.mobile.ui.util.FocusTextView;
import org.musicbrainz.mobile.ws.WebService;
import org.musicbrainz.mobile.ws.WebServiceUser;
import org.musicbrainz.mobile.ws.WebService.MBEntity;
import org.xml.sax.SAXException;

import com.markupartist.android.widget.ActionBar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * Activity which retrieves and displays information about an artist.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ArtistActivity extends SuperActivity implements View.OnClickListener, ListView.OnItemClickListener {
	
	// data object
	private Artist data;
	
	// query data
	private String id;
	
	private ActionBar actionBar;
	
	// refreshables
	private RatingBar rating;
	private FocusTextView tags;
	
	// input
	private EditText tagInput;
	private RatingBar ratingInput;
	
	// edit buttons
	private Button tagBtn;
	private Button rateBtn;
	
	private WebServiceUser user;
	private UserData userData;
	
	// status
	private boolean doingTag = false;
	private boolean doingRate = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// recover MBID from intent
        id = getIntent().getStringExtra("mbid");
	
        new LookupTask().execute();
		
		setContentView(R.layout.blank);
    }
	
	protected void populate() {
		
		setContentView(R.layout.activity_artist);
		actionBar = setupActionBarWithHome();
		
		// info header
		FocusTextView artist = (FocusTextView) findViewById(R.id.artist_artist);
		rating = (RatingBar) findViewById(R.id.artist_rating);
		tags = (FocusTextView) findViewById(R.id.artist_tags);
		
		artist.setText(data.getName());
		rating.setRating(data.getRating());
		tags.setText(data.getTags());
		
		ListView releaseList = (ListView) findViewById(R.id.artist_releases);
		releaseList.setOnItemClickListener(this);
		releaseList.setAdapter(new ReleaseAdapter());
		
		ListView linksList = (ListView) findViewById(R.id.artist_links);
		linksList.setOnItemClickListener(this);
		linksList.setAdapter(new LinkAdapter());
		
		// setup tabs
		setupTabs();

		tagInput = (EditText) findViewById(R.id.tag_input);
		
		tagBtn = (Button) findViewById(R.id.tag_btn);
		tagBtn.setOnClickListener(this);
		
		ratingInput = (RatingBar) findViewById(R.id.rating_input);
		
		rateBtn = (Button) findViewById(R.id.rate_btn);
		rateBtn.setOnClickListener(this);
		
		// display messages for no tags, no releases or no links
		if (data.getTags() == "")
			tags.setText(getText(R.string.no_tags));
		
		if (data.getReleases().isEmpty()) {
			TextView noRes = (TextView) findViewById(R.id.noreleases);
			noRes.setVisibility(View.VISIBLE);
		}
			
		if (data.getLinks().isEmpty()) {
			TextView noRes = (TextView) findViewById(R.id.nolinks);
			noRes.setVisibility(View.VISIBLE);
		}
		
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
		TabHost tabs = (TabHost) this.findViewById(R.id.artist_tabhost);
		tabs.setup();
		
		TabSpec releaseGroupsTab = tabs.newTabSpec("RGs");
		final TextView rgIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
		rgIndicator.setText(R.string.tab_releases);
		releaseGroupsTab.setIndicator(rgIndicator);
		releaseGroupsTab.setContent(R.id.releases_tab);
		
		TabSpec linksTab = tabs.newTabSpec("links");
		final TextView linksIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
		linksIndicator.setText(R.string.tab_links);
		linksTab.setIndicator(linksIndicator);
		linksTab.setContent(R.id.links_tab);
		
		TabSpec editTab = tabs.newTabSpec("edit");
		final TextView editIndicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, null, false);
		editIndicator.setText(R.string.tab_edits);
		editTab.setIndicator(editIndicator);
		editTab.setContent(R.id.edit_tab);
		
		tabs.addTab(releaseGroupsTab);
		tabs.addTab(linksTab);
		tabs.addTab(editTab);
	}
	
	private void updateProgress() {
		if (doingTag || doingRate) {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		} else {
			actionBar.setProgressBarVisibility(View.GONE);
		}
	}
	
	/**
	 * Task to retrieve artist data from webservice and populate page.
	 */
	private class LookupTask extends AsyncTask<Void, Void, Boolean> {
		
		ProgressDialog pd;
		
		protected void onPreExecute() {
			pd = new ProgressDialog(ArtistActivity.this) {
				public void cancel() {
					super.cancel();
					LookupTask.this.cancel(true);
					ArtistActivity.this.finish();
				}
				
			};
			pd.setMessage(getText(R.string.pd_loading));
			pd.setCancelable(true);
			pd.show();
		}

		protected Boolean doInBackground(Void... params) {
			
			try {
				data = WebService.lookupArtist(id);
				if (loggedIn) {
					user = getUser();
					userData = user.getUserData(MBEntity.ARTIST, data.getMbid());
					user.shutdownConnectionManager();
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (SAXException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		protected void onPostExecute(Boolean success) {
			
			if (success) {
				populate();
				if (loggedIn) {
					tagInput.setText(userData.getTagString());
					ratingInput.setRating(userData.getRating());
				}
				pd.dismiss();
			} else {
				// error or connection timed out - retry dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ArtistActivity.this);
				builder.setMessage(getString(R.string.err_text))
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
										ArtistActivity.this.finish();
									}
								});
				Dialog conError = builder.create();
				pd.dismiss();
				conError.show();
			}
		}
		
	}
	
	/**
	 * Task to submit user tag list and refresh page tags list.
	 */
	private class TagTask extends AsyncTask<String, Void, Boolean> {
		
		protected void onPreExecute() {
			doingTag = true;
			updateProgress();
			tagBtn.setEnabled(false);
		}

		protected Boolean doInBackground(String... tags) {
			
			Collection<String> processedTags = WebServiceUser.processTags(tags[0]);
			
			user = getUser();
			try {
				user.submitTags(MBEntity.ARTIST, data.getMbid(), processedTags);
				data.setTags(WebService.refreshTags(MBEntity.ARTIST, data.getMbid()));
				user.shutdownConnectionManager();
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
			
			if (success) {
				Toast.makeText(ArtistActivity.this, R.string.toast_tag, Toast.LENGTH_SHORT).show(); 
			} else {
				Toast.makeText(ArtistActivity.this, R.string.toast_tag_fail, Toast.LENGTH_LONG).show();
			}	
		}
		
	}
	
	/**
	 * Task to submit user rating and update page rating.
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
				user.submitRating(MBEntity.ARTIST, data.getMbid(), rating[0]);
				float newRating = WebService.refreshRating(MBEntity.ARTIST, data.getMbid());
				data.setRating(newRating);
				user.shutdownConnectionManager();
			} catch (IOException e) {
				return false;
			} catch (SAXException e) {
				return true; // refresh failed but submission okay
			}
			return true;
		}
		
		protected void onPostExecute(Boolean success) {
			
			rating.setRating(data.getRating());
			
			doingRate = false;
			updateProgress();
			rateBtn.setEnabled(true);
			
			if (success) {
				Toast.makeText(ArtistActivity.this, R.string.toast_rate, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ArtistActivity.this, R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	/**
	 * Handler for tag and rate buttons.
	 */
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.tag_btn:
			
			String tagString = tagInput.getText().toString();
			if (tagString.length() == 0) {
				Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
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
	 * Release group list selection handler.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		if (parent.getId() == R.id.artist_releases) {
			ReleaseGroup rg = data.getReleases().get(position);
			
			Intent releaseIntent = new Intent (ArtistActivity.this, ReleaseActivity.class);
			releaseIntent.putExtra("rg_id", rg.getMbid());
			startActivity(releaseIntent);
		} else {
			String link = data.getLinks().get(position).getUrl();
			
			Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			startActivity(urlIntent);
		}
	}
	
	/**
	 * Adapter for list of releases.
	 */
	private class ReleaseAdapter extends ArrayAdapter<ReleaseGroup> {
		
		ReleaseAdapter() {
			super(ArtistActivity.this, R.layout.list_rg, data.getReleases());
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View release = convertView; // reuse View
			ReleaseListWrapper wrapper = null;
			
			if (release == null) {
				LayoutInflater inflater = getLayoutInflater();
				release = inflater.inflate(R.layout.list_rg, parent, false);
				
				wrapper = new ReleaseListWrapper(release);
				release.setTag(wrapper);
			} else {
				wrapper = (ReleaseListWrapper) release.getTag();
			}
			
			ReleaseGroup rData = data.getReleases().get(position);
			
			wrapper.getReleaseTitle().setText(rData.getTitle());
			wrapper.getReleaseType().setText(rData.getFormattedType(getContext()));
			
			return release;
		}
	}
	
	/**
	 * Wrapper for release list. Holder pattern minimises executions of findViewById().
	 */
	private class ReleaseListWrapper {
		
		View base;
		TextView releaseTitle = null;
		TextView releaseType = null;
		
		ReleaseListWrapper(View base) {
			this.base = base;
		}
		
		TextView getReleaseTitle() {
			if (releaseTitle == null)
				releaseTitle = (TextView) base.findViewById(R.id.list_rg_title);
			
			return releaseTitle;
		}
		
		TextView getReleaseType() {
			if (releaseType == null)
				releaseType = (TextView) base.findViewById(R.id.list_rg_type);
			
			return releaseType;
		}
	}
	
	/**
	 * Adapter for list of web links.
	 */
	private class LinkAdapter extends ArrayAdapter<WebLink> {
		
		LinkAdapter() {
			super(ArtistActivity.this, R.layout.list_link, data.getLinks());
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			 
			View link = convertView;
			LinkListWrapper wrapper = null;
			
			if (link == null) {
				LayoutInflater inflater = getLayoutInflater();
				link = inflater.inflate(R.layout.list_link, parent, false);
				
				wrapper = new LinkListWrapper(link);
				link.setTag(wrapper);
			} else {
				wrapper = (LinkListWrapper) link.getTag();
			}
			
			WebLink lData = data.getLinks().get(position);
			
			wrapper.getLinkTitle().setText(lData.getFormattedType());
			wrapper.getLink().setText(lData.getFormattedLink());
			
			return link;
		}
	}
	
	/**
	 * Wrapper for link list. Holder pattern minimises executions of findViewById().
	 */
	private class LinkListWrapper {
		
		View base;
		TextView linkTitle = null;
		TextView link = null;
		
		LinkListWrapper(View base) {
			this.base = base;
		}
		
		TextView getLinkTitle() {
			if (linkTitle == null)
				linkTitle = (TextView) base.findViewById(R.id.list_link_title);
			
			return linkTitle;
		}
		
		TextView getLink() {
			if (link == null)
				link = (TextView) base.findViewById(R.id.list_link);
			
			return link;
		}
	}
	
}

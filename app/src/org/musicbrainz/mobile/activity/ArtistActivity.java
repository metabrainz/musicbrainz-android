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

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.base.TagRateActivity;
import org.musicbrainz.mobile.adapter.ArtistReleaseGroupAdapter;
import org.musicbrainz.mobile.adapter.LinkAdapter;
import org.musicbrainz.mobile.loader.ArtistLoader;
import org.musicbrainz.mobile.loader.AsyncEntityResult;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.task.SubmitRatingTask;
import org.musicbrainz.mobile.task.SubmitTagsTask;
import org.musicbrainz.mobile.widget.FocusTextView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends TagRateActivity implements LoaderCallbacks<AsyncEntityResult<Artist>>, View.OnClickListener, ListView.OnItemClickListener {

    private static final int ARTIST_LOADER = 0;
    
    private String mbid;
    private Artist data;
    private UserData userData;

    private RatingBar rating;
    private FocusTextView tags;
    private EditText tagInput;
    private RatingBar ratingInput;
    private Button tagBtn;
    private Button rateBtn;

    private boolean doingTag, doingRate = false;

    private SubmitTagsTask tagTask;
    private SubmitRatingTask ratingTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mbid = getIntent().getStringExtra(Extra.ARTIST_MBID);
        setContentView(R.layout.layout_loading);
        getSupportLoaderManager().initLoader(ARTIST_LOADER, savedInstanceState, this);
    }

    protected void populateLayout() {
        setContentView(R.layout.activity_artist);
        findViews();
        setupTabs();
        addActionBarShare();

        FocusTextView artist = (FocusTextView) findViewById(R.id.artist_artist);
        ListView releaseList = (ListView) findViewById(R.id.artist_releases);
        ListView linksList = (ListView) findViewById(R.id.artist_links);

        artist.setText(data.getName());
        releaseList.setAdapter(new ArtistReleaseGroupAdapter(this, data.getReleaseGroups()));
        releaseList.setOnItemClickListener(this);
        linksList.setAdapter(new LinkAdapter(this, data.getLinks()));
        linksList.setOnItemClickListener(this);
        rating.setRating(data.getRating());
        tags.setText(StringFormat.commaSeparateTags(data.getTags(), this));

        displayMessagesForEmptyData();
        
        if (isUserLoggedIn()) {
            tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
            ratingInput.setRating(userData.getRating());
        } else {
            disableEditViews();
            findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
        }
    }
    
    private void findViews() {
        rating = (RatingBar) findViewById(R.id.artist_rating);
        tags = (FocusTextView) findViewById(R.id.artist_tags);
        tagInput = (EditText) findViewById(R.id.tag_input);
        tagBtn = (Button) findViewById(R.id.tag_btn);
        ratingInput = (RatingBar) findViewById(R.id.rating_input);
        rateBtn = (Button) findViewById(R.id.rate_btn);
        
        tagBtn.setOnClickListener(this);
        rateBtn.setOnClickListener(this);
    }

    private void disableEditViews() {
        tagInput.setEnabled(false);
        tagInput.setFocusable(false);
        ratingInput.setEnabled(false);
        ratingInput.setFocusable(false);
        tagBtn.setEnabled(false);
        tagBtn.setFocusable(false);
        rateBtn.setEnabled(false);
        rateBtn.setFocusable(false);
    }
    
    private void displayMessagesForEmptyData() {
        if (data.getReleases().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.noreleases);
            noRes.setVisibility(View.VISIBLE);
        }
        if (data.getLinks().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.nolinks);
            noRes.setVisibility(View.VISIBLE);
        }
    }

    private void addActionBarShare() {
          // TODO
//        Action share = actionBar.newAction();
//        share.setIcon(R.drawable.ic_actionbar_share);
//        share.setIntent(Utils.shareIntent(getApplicationContext(), Config.ARTIST_SHARE + mbid));
//        actionBar.addAction(share);
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
            // TODO start progress
        } else {
            // TODO stop progress
        }
    }

    protected Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                getSupportLoaderManager().initLoader(0, null, ArtistActivity.this);
            }
        });
        builder.setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ArtistActivity.this.finish();
            }
        });
        return builder.create();
    }

    public void onStartTagging() {
        doingTag = true;
        updateProgress();
        tagBtn.setEnabled(false);
    }

    public void onDoneTagging() {
        data.setTags(tagTask.getUpdatedTags());
        tags.setText(StringFormat.commaSeparateTags(data.getTags(), this));
        doingTag = false;
        updateProgress();
        tagBtn.setEnabled(true);
    }

    public void onStartRating() {
        doingRate = true;
        updateProgress();
        rateBtn.setEnabled(false);
    }

    public void onDoneRating() {
        data.setRating(ratingTask.getUpdatedRating());
        rating.setRating(data.getRating());
        doingRate = false;
        updateProgress();
        rateBtn.setEnabled(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.tag_btn:
            String tagString = tagInput.getText().toString();
            if (tagString.length() == 0) {
                Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
            } else {
                tagTask = new SubmitTagsTask(this, MBEntity.ARTIST, mbid);
                tagTask.execute(tagString);
            }
            break;
        case R.id.rate_btn:
            int rating = (int) ratingInput.getRating();
            ratingTask = new SubmitRatingTask(this, MBEntity.ARTIST, mbid);
            ratingTask.execute(rating);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.artist_releases) {
            showRelease(position);
        } else {
            openLink(position);
        }
    }

    private void showRelease(int position) {
        ReleaseGroupStub rg = data.getReleases().get(position);
        Intent releaseIntent = new Intent(ArtistActivity.this, ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, rg.getMbid());
        startActivity(releaseIntent);
    }

    private void openLink(int position) {
        String link = data.getLinks().get(position).getUrl();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(urlIntent);
    }

    @Override
    public void onTaskFinished() {
        // TODO REMOVE
    }

    @Override
    public Loader<AsyncEntityResult<Artist>> onCreateLoader(int id, Bundle args) {
        if (isUserLoggedIn()) {
            return new ArtistLoader(this, getCredentials(), mbid);
        } else {
            return new ArtistLoader(this, getUserAgent(), mbid);
        }
    }

    @Override
    public void onLoadFinished(Loader<AsyncEntityResult<Artist>> loader, AsyncEntityResult<Artist> container) {
        handleLoadResult(container);
    }

    private void handleLoadResult(AsyncEntityResult<Artist> result) {
        switch(result.getStatus()) {
        case SUCCESS:
            data = result.getData();
            if (result.hasUserData()) {
                userData = result.getUserData();
            }
            populateLayout();
            break;
        case EXCEPTION:
            showDialog(DIALOG_CONNECTION_FAILURE);
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncEntityResult<Artist>> loader) {
        loader.reset();
    }

}


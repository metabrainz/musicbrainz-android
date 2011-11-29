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

import java.util.ArrayList;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.base.TagRateActivity;
import org.musicbrainz.mobile.adapter.ReleaseTrackAdapter;
import org.musicbrainz.mobile.dialog.BarcodeResultDialog;
import org.musicbrainz.mobile.dialog.ReleaseSelectionDialog;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.task.LookupBarcodeTask;
import org.musicbrainz.mobile.task.LookupRGStubsTask;
import org.musicbrainz.mobile.task.LookupReleaseTask;
import org.musicbrainz.mobile.task.MusicBrainzTask;
import org.musicbrainz.mobile.task.SubmitRatingTask;
import org.musicbrainz.mobile.task.SubmitTagsTask;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Utils;
import org.musicbrainz.mobile.widget.FocusTextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
public class ReleaseActivity extends TagRateActivity implements View.OnClickListener {

    private static final int DIALOG_RELEASE_SELECTION = 0;
    private static final int DIALOG_BARCODE = 1;
    
    private Release data;
    private LinkedList<ReleaseStub> stubs;
    private UserData userData;

    private String releaseMbid;
    private String releaseGroupMbid;
    private String barcode;

    private ActionBar actionBar;

    private FocusTextView tags;
    private RatingBar rating;
    private EditText tagInput;
    private RatingBar ratingInput;
    private Button tagBtn;
    private Button rateBtn;

    private boolean doingTag = false;
    private boolean doingRate = false;
    
    private MusicBrainzTask lookupTask;
    private SubmitTagsTask tagTask;
    private SubmitRatingTask ratingTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        releaseMbid = getIntent().getStringExtra(Extra.RELEASE_MBID);
        releaseGroupMbid = getIntent().getStringExtra(Extra.RG_MBID);
        barcode = getIntent().getStringExtra(Extra.BARCODE);
        
        setContentView(R.layout.loading);
        setupActionBarWithHome();
        
        Object retained = getLastNonConfigurationInstance();
        if (retained instanceof TaskHolder) {
            TaskHolder holder = (TaskHolder) retained;
            reconnectTasks(holder);
        } else {
            newTask();
        }
    }
    
    private void newTask() {
        if (releaseMbid != null) {
            lookupTask = new LookupReleaseTask(this);
            lookupTask.execute(releaseMbid);
        } else if (releaseGroupMbid != null) {
            lookupTask = new LookupRGStubsTask(this);
            lookupTask.execute(releaseGroupMbid);
        } else if (barcode != null) {
            lookupTask = new LookupBarcodeTask(this);
            lookupTask.execute(barcode);
        } else {
            this.finish();
        }
    }
    
    private void reconnectTasks(TaskHolder holder) {
        lookupTask = (MusicBrainzTask) holder.lookupTask;
        lookupTask.connect(this);
        if (lookupTask.isFinished()) {
            onTaskFinished();
        }
        if (holder.tagTask != null) {
            tagTask = (SubmitTagsTask) holder.tagTask;
            tagTask.connect(this);
            if (tagTask.isRunning()) {
                onStartTagging();
            }
        }
        if (holder.ratingTask != null) {
            ratingTask = (SubmitRatingTask) holder.ratingTask;
            ratingTask.connect(this);
            if (ratingTask.isRunning()) {
                onStartRating();
            }
        }
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

        artist.setText(StringFormat.commaSeparateArtists(data.getArtists()));
        title.setText(data.getTitle());

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

        labels.setText(StringFormat.commaSeparate(data.getLabels()));
        releaseDate.setText(data.getDate());

        if (data.getReleaseGroupTags().isEmpty()) {
            tags.setText(getText(R.string.no_tags));
        }
        
        if (isUserLoggedIn()) {
            tags.setText(StringFormat.commaSeparateTags(data.getReleaseGroupTags()));
            rating.setRating(data.getReleaseGroupRating());
        } else {
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
        share.setIntent(Utils.shareIntent(getApplicationContext(), Config.RELEASE_SHARE + releaseMbid));
        actionBar.addAction(share);
    }

    private void addActionBarArtist() {
        Action artist = actionBar.newAction();
        artist.setIcon(R.drawable.ic_actionbar_artist);
        artist.setIntent(createArtistIntent());
        actionBar.addAction(artist);
    }

    private Intent createArtistIntent() {
        final Intent releaseIntent = new Intent(this, ArtistActivity.class);
        releaseIntent.putExtra(Extra.ARTIST_MBID, data.getArtists().get(0).getMbid());
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

    public void onClick(View view) {

        switch (view.getId()) {
        case R.id.tag_btn:
            String tagString = tagInput.getText().toString();
            if (tagString.length() == 0) {
                Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
            } else {
                tagTask = new SubmitTagsTask(this, MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
                tagTask.execute(tagString);
            }
            break;
        case R.id.rate_btn:
            int rating = (int) ratingInput.getRating();
            ratingTask = new SubmitRatingTask(this, MBEntity.RELEASE_GROUP, data.getReleaseGroupMbid());
            ratingTask.execute(rating);
        }
    }

    @Override
    public void onStartRating() {
        doingRate = true;
        updateProgressStatus();
        rateBtn.setEnabled(false);
    }

    @Override
    public void onDoneRating() {
        data.setReleaseGroupRating(ratingTask.getUpdatedRating());
        rating.setRating(data.getReleaseGroupRating());
        doingRate = false;
        updateProgressStatus();
        rateBtn.setEnabled(true);
    }

    @Override
    public void onStartTagging() {
        doingTag = true;
        updateProgressStatus();
        tagBtn.setEnabled(false);
    }

    @Override
    public void onDoneTagging() {
        data.setReleaseGroupTags(tagTask.getUpdatedTags());
        tags.setText(StringFormat.commaSeparateTags(data.getReleaseGroupTags()));
        doingTag = false;
        updateProgressStatus();
        tagBtn.setEnabled(true);
        
    }
    
    @Override 
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_RELEASE_SELECTION:
            return new ReleaseSelectionDialog(ReleaseActivity.this, stubs);
        case DIALOG_BARCODE:
            BarcodeResultDialog barcodeDialog = new BarcodeResultDialog(ReleaseActivity.this, isUserLoggedIn(), barcode);
            barcodeDialog.setCancelable(true);
            return barcodeDialog;
        case DIALOG_CONNECTION_FAILURE:
            return createConnectionErrorDialog();
        }
        return null;
    }

    @Override
    protected Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newTask();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ReleaseActivity.this.finish();
            }
        });
        return builder.create();
    }

    @Override
    public void onTaskFinished() {
        if (lookupTask.failed()) {
            showDialog(DIALOG_CONNECTION_FAILURE);
            return;
        }
        if (lookupTask instanceof LookupReleaseTask) {
            LookupReleaseTask task = (LookupReleaseTask) lookupTask;
            data = task.getRelease();
            if (task.getUserData() != null) {
                userData = task.getUserData();
            }
            displayReleaseData();
        } else if (lookupTask instanceof LookupRGStubsTask) {
            LookupRGStubsTask task = (LookupRGStubsTask) lookupTask;
            stubs = task.getStubs();
            if (stubs.size() == 1) {
                ReleaseStub singleRelease = stubs.getFirst();
                lookupTask = new LookupReleaseTask(this);
                lookupTask.execute(singleRelease.getReleaseMbid());
            } else {
                showDialog(DIALOG_RELEASE_SELECTION);
            }
        } else if (lookupTask instanceof LookupBarcodeTask) {
            LookupBarcodeTask task = (LookupBarcodeTask) lookupTask;
            if (task.doesBarcodeExist()) {
                data = task.getRelease();
                if (task.getUserData() != null) {
                    userData = task.getUserData();
                }
                displayReleaseData();
            } else {
                showDialog(DIALOG_BARCODE);
            }
        }
    }
    
    private void displayReleaseData() {
        populate();
        if (isUserLoggedIn()) {
            tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
            ratingInput.setRating(userData.getRating());
        }
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        disconnectTasks();
        return new TaskHolder(lookupTask, tagTask, ratingTask);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectTasks();
    }
    
    private void disconnectTasks() {
        if (lookupTask != null) lookupTask.disconnect();
        if (tagTask != null) tagTask.disconnect();
        if (ratingTask != null) ratingTask.disconnect();
    }
    
    private static class TaskHolder {
        
        public MusicBrainzTask lookupTask;
        public SubmitTagsTask tagTask;
        public SubmitRatingTask ratingTask;
        
        public TaskHolder(MusicBrainzTask lookupTask, SubmitTagsTask tagTask, SubmitRatingTask ratingTask) {
            this.lookupTask = lookupTask;
            this.tagTask = tagTask;
            this.ratingTask = ratingTask;
        }
    }

}

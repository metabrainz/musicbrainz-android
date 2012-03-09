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
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.BarcodeNotFoundException;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseTrackAdapter;
import org.musicbrainz.mobile.adapter.pager.ReleasePagerAdapter;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.dialog.BarcodeResultDialog;
import org.musicbrainz.mobile.dialog.ReleaseSelectionDialog;
import org.musicbrainz.mobile.loader.BarcodeReleaseLoader;
import org.musicbrainz.mobile.loader.ReleaseGroupStubsLoader;
import org.musicbrainz.mobile.loader.ReleaseLoader;
import org.musicbrainz.mobile.loader.SubmitRatingLoader;
import org.musicbrainz.mobile.loader.SubmitTagsLoader;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.util.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Activity which retrieves and displays information about a release.
 * 
 * This Activity initiates lookups given three sources that generally result in
 * display of release information. An intent will contain either a barcode, a
 * release MBID or a release group MBID.
 */
public class ReleaseActivity extends MusicBrainzActivity implements View.OnClickListener {

    private static final int RELEASE_LOADER = 0;
    private static final int RELEASE_GROUP_STUBS_LOADER = 1;
    private static final int BARCODE_RELEASE_LOADER = 2;
    private static final int RATING_LOADER = 3;
    private static final int TAG_LOADER = 4;

    private static final int DIALOG_RELEASE_SELECTION = 0;
    private static final int DIALOG_BARCODE = 1;
    private static final int DIALOG_CONNECTION_FAILURE = 3;

    private Release release;
    private LinkedList<ReleaseStub> stubs;
    private UserData userData;

    private String releaseMbid;
    private String releaseGroupMbid;
    private String barcode;

    private TextView tags;
    private RatingBar rating;
    private EditText tagInput;
    private RatingBar ratingInput;
    private Button tagBtn;
    private Button rateBtn;

    private boolean doingTag, doingRate = false;
    private boolean provideArtistAction = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        releaseMbid = getIntent().getStringExtra(Extra.RELEASE_MBID);
        releaseGroupMbid = getIntent().getStringExtra(Extra.RG_MBID);
        barcode = getIntent().getStringExtra(Extra.BARCODE);

        setContentView(R.layout.layout_loading);
        setProgressBarIndeterminateVisibility(Boolean.FALSE);
        configureLoader();
    }

    private void configureLoader() {
        if (releaseMbid != null) {
            getSupportLoaderManager().initLoader(RELEASE_LOADER, null, releaseLoaderCallbacks);
        } else if (releaseGroupMbid != null) {
            getSupportLoaderManager().initLoader(RELEASE_GROUP_STUBS_LOADER, null, releaseStubLoaderCallbacks);
        } else if (barcode != null) {
            getSupportLoaderManager().initLoader(BARCODE_RELEASE_LOADER, null, releaseLoaderCallbacks);
        } else {
            this.finish();
        }
    }

    private void populateLayout() {
        setContentView(R.layout.activity_release);
        configurePager();
        findViews();

        TextView artist = (TextView) findViewById(R.id.release_artist);
        TextView title = (TextView) findViewById(R.id.release_release);
        TextView labels = (TextView) findViewById(R.id.release_label);
        TextView releaseDate = (TextView) findViewById(R.id.release_date);
        ListView trackList = (ListView) findViewById(R.id.release_tracks);

        if (isArtistUpAvailable()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        artist.setText(StringFormat.commaSeparateArtists(release.getArtists()));
        title.setText(release.getTitle());
        trackList.setAdapter(new ReleaseTrackAdapter(this, release.getTrackList()));
        trackList.setDrawSelectorOnTop(false);
        labels.setText(StringFormat.commaSeparate(release.getLabels()));
        releaseDate.setText(release.getDate());
        tags.setText(StringFormat.commaSeparateTags(release.getReleaseGroupTags(), this));
        rating.setRating(release.getReleaseGroupRating());

        artist.setSelected(true);
        title.setSelected(true);
        labels.setSelected(true);
        tags.setSelected(true);

        if (isUserLoggedIn()) {
            tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
            ratingInput.setRating(userData.getRating());
        } else {
            disableEditFields();
            findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
        }
    }

    private void configurePager() {
        ReleasePagerAdapter adapter = new ReleasePagerAdapter(this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        adapter.instantiateItem(pager, 1);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        pager.setCurrentItem(0);
    }

    private void findViews() {
        rating = (RatingBar) findViewById(R.id.release_rating);
        tags = (TextView) findViewById(R.id.release_tags);
        tagInput = (EditText) findViewById(R.id.tag_input);
        ratingInput = (RatingBar) findViewById(R.id.rating_input);
        tagBtn = (Button) findViewById(R.id.tag_btn);
        rateBtn = (Button) findViewById(R.id.rate_btn);

        tagBtn.setOnClickListener(this);
        rateBtn.setOnClickListener(this);
    }

    private boolean isArtistUpAvailable() {
        ArrayList<ArtistNameMbid> releaseArtists = release.getArtists();
        if (releaseArtists.size() != 1) {
            provideArtistAction = false;
        } else {
            ArtistNameMbid singleArtist = releaseArtists.get(0);
            for (String id : Artist.SPECIAL_PURPOSE) {
                if (singleArtist.getMbid().equals(id)) {
                    provideArtistAction = false;
                }
            }
        }
        return provideArtistAction;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.release, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case R.id.action_share:
            startActivity(Utils.shareIntent(getApplicationContext(), Configuration.RELEASE_SHARE + releaseMbid));
            return true;
        case android.R.id.home:
            if (provideArtistAction) {
                startActivity(createArtistIntent());
                return true;
            }
        }
        return false;
    }

    private Intent createArtistIntent() {
        final Intent releaseIntent = new Intent(this, ArtistActivity.class);
        releaseIntent.putExtra(Extra.ARTIST_MBID, release.getArtists().get(0).getMbid());
        return releaseIntent;
    }

    private void updateProgressStatus() {
        if (doingTag || doingRate) {
            setProgressBarIndeterminateVisibility(Boolean.TRUE);
        } else {
            setProgressBarIndeterminateVisibility(Boolean.FALSE);
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {
        case R.id.tag_btn:
            String tagString = tagInput.getText().toString();
            if (tagString.length() == 0) {
                Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
            } else {
                doingTag = true;
                updateProgressStatus();
                tagBtn.setEnabled(false);
                getSupportLoaderManager().initLoader(TAG_LOADER, null, tagSubmissionCallbacks);
            }
            break;
        case R.id.rate_btn:
            doingRate = true;
            updateProgressStatus();
            rateBtn.setEnabled(false);
            getSupportLoaderManager().initLoader(RATING_LOADER, null, ratingSubmissionCallbacks);
        }
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

    private Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartLoader();
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

    private void restartLoader() {
        if (releaseMbid != null) {
            getSupportLoaderManager().restartLoader(RELEASE_LOADER, null, releaseLoaderCallbacks);
        } else if (releaseGroupMbid != null) {
            getSupportLoaderManager().restartLoader(RELEASE_GROUP_STUBS_LOADER, null, releaseStubLoaderCallbacks);
        } else if (barcode != null) {
            getSupportLoaderManager().restartLoader(BARCODE_RELEASE_LOADER, null, releaseLoaderCallbacks);
        }
    }

    private LoaderCallbacks<AsyncEntityResult<Release>> releaseLoaderCallbacks = new LoaderCallbacks<AsyncEntityResult<Release>>() {

        @Override
        public Loader<AsyncEntityResult<Release>> onCreateLoader(int id, Bundle args) {
            switch (id) {
            case RELEASE_LOADER:
                return new ReleaseLoader(getApplicationContext(), releaseMbid);
            case BARCODE_RELEASE_LOADER:
                return new BarcodeReleaseLoader(getApplicationContext(), barcode);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<AsyncEntityResult<Release>> loader, AsyncEntityResult<Release> container) {
            switch (container.getStatus()) {
            case EXCEPTION:
                if (container.getException() instanceof BarcodeNotFoundException) {
                    showDialog(DIALOG_BARCODE);
                } else {
                    showDialog(DIALOG_CONNECTION_FAILURE);
                }
                break;
            case SUCCESS:
                release = container.getData();
                if (container.hasUserData()) {
                    userData = container.getUserData();
                }
                populateLayout();
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncEntityResult<Release>> loader) {
            loader.reset();
        }
    };

    private LoaderCallbacks<AsyncResult<LinkedList<ReleaseStub>>> releaseStubLoaderCallbacks = new LoaderCallbacks<AsyncResult<LinkedList<ReleaseStub>>>() {

        @Override
        public Loader<AsyncResult<LinkedList<ReleaseStub>>> onCreateLoader(int id, Bundle args) {
            return new ReleaseGroupStubsLoader(getApplicationContext(), releaseGroupMbid);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<LinkedList<ReleaseStub>>> loader,
                AsyncResult<LinkedList<ReleaseStub>> container) {
            switch (container.getStatus()) {
            case EXCEPTION:
                showDialog(DIALOG_CONNECTION_FAILURE);
                break;
            case SUCCESS:
                stubs = container.getData();
                if (stubs.size() == 1) {
                    ReleaseStub singleRelease = stubs.getFirst();
                    releaseMbid = singleRelease.getReleaseMbid();
                    getSupportLoaderManager().initLoader(RELEASE_LOADER, null, releaseLoaderCallbacks);
                } else {
                    showDialog(DIALOG_RELEASE_SELECTION);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<LinkedList<ReleaseStub>>> loader) {
            loader.reset();
        }
    };

    private LoaderCallbacks<AsyncResult<Float>> ratingSubmissionCallbacks = new LoaderCallbacks<AsyncResult<Float>>() {

        @Override
        public Loader<AsyncResult<Float>> onCreateLoader(int id, Bundle args) {
            int rating = (int) ratingInput.getRating();
            return new SubmitRatingLoader(getApplicationContext(), Entity.RELEASE_GROUP, release.getReleaseGroupMbid(),
                    rating);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Float>> loader, AsyncResult<Float> data) {
            getSupportLoaderManager().destroyLoader(RATING_LOADER);
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(ReleaseActivity.this, R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(ReleaseActivity.this, R.string.toast_rate, Toast.LENGTH_SHORT).show();
                updateRating(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Float>> loader) {
            loader.reset();
        }
    };

    private void updateRating(Float newRating) {
        release.setReleaseGroupRating(newRating);
        rating.setRating(newRating);
        doingRate = false;
        updateProgressStatus();
        rateBtn.setEnabled(true);
    }

    private LoaderCallbacks<AsyncResult<LinkedList<Tag>>> tagSubmissionCallbacks = new LoaderCallbacks<AsyncResult<LinkedList<Tag>>>() {

        @Override
        public Loader<AsyncResult<LinkedList<Tag>>> onCreateLoader(int id, Bundle args) {
            String tags = tagInput.getText().toString();
            return new SubmitTagsLoader(getApplicationContext(), Entity.RELEASE_GROUP, release.getReleaseGroupMbid(),
                    tags);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<LinkedList<Tag>>> loader, AsyncResult<LinkedList<Tag>> data) {
            getSupportLoaderManager().destroyLoader(TAG_LOADER);
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(ReleaseActivity.this, R.string.toast_tag_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(ReleaseActivity.this, R.string.toast_tag, Toast.LENGTH_SHORT).show();
                updateTags(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<LinkedList<Tag>>> loader) {
            loader.reset();
        }
    };

    private void updateTags(LinkedList<Tag> newTags) {
        release.setReleaseGroupTags(newTags);
        tags.setText(StringFormat.commaSeparateTags(newTags, this));
        doingTag = false;
        updateProgressStatus();
        tagBtn.setEnabled(true);
    }

}

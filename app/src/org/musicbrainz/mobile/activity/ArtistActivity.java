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

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ArtistReleaseGroupAdapter;
import org.musicbrainz.mobile.adapter.list.LinkAdapter;
import org.musicbrainz.mobile.adapter.pager.ArtistPagerAdapter;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.loader.ArtistLoader;
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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.view.Window;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends MusicBrainzActivity implements LoaderCallbacks<AsyncEntityResult<Artist>>,
        View.OnClickListener, ListView.OnItemClickListener {

    private static final int ARTIST_LOADER = 0;
    private static final int RATING_LOADER = 1;
    private static final int TAG_LOADER = 2;

    private static final int DIALOG_CONNECTION_FAILURE = 0;

    private String mbid;
    private Artist artist;
    private UserData userData;

    private RatingBar rating;
    private TextView tags;
    private EditText tagInput;
    private RatingBar ratingInput;
    private Button tagBtn;
    private Button rateBtn;

    private boolean doingTag, doingRate = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        mbid = getIntent().getStringExtra(Extra.ARTIST_MBID);
        setContentView(R.layout.layout_loading);
        setProgressBarIndeterminateVisibility(Boolean.FALSE);
        getSupportLoaderManager().initLoader(ARTIST_LOADER, savedInstanceState, this);
    }

    protected void populateLayout() {
        setContentView(R.layout.activity_artist);
        configurePager();
        findViews();

        TextView artistText = (TextView) findViewById(R.id.artist_artist);
        ListView releaseList = (ListView) findViewById(R.id.artist_releases);
        ListView linksList = (ListView) findViewById(R.id.artist_links);

        artistText.setText(artist.getName());
        releaseList.setAdapter(new ArtistReleaseGroupAdapter(this, artist.getReleaseGroups()));
        releaseList.setOnItemClickListener(this);
        linksList.setAdapter(new LinkAdapter(this, artist.getLinks()));
        linksList.setOnItemClickListener(this);
        rating.setRating(artist.getRating());
        tags.setText(StringFormat.commaSeparateTags(artist.getTags(), this));

        artistText.setSelected(true);
        tags.setSelected(true);

        displayMessagesForEmptyData();

        if (isUserLoggedIn()) {
            tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
            ratingInput.setRating(userData.getRating());
        } else {
            disableEditViews();
            findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
        }
    }

    private void configurePager() {
        ArtistPagerAdapter adapter = new ArtistPagerAdapter(this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        adapter.instantiateItem(pager, 2);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        pager.setCurrentItem(1);
    }

    private void findViews() {
        rating = (RatingBar) findViewById(R.id.artist_rating);
        tags = (TextView) findViewById(R.id.artist_tags);
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
        if (artist.getReleases().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.noreleases);
            noRes.setVisibility(View.VISIBLE);
        }
        if (artist.getLinks().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.nolinks);
            noRes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
        case R.id.action_share:
            startActivity(Utils.shareIntent(getApplicationContext(), Configuration.ARTIST_SHARE + mbid));
            return true;
        }
        return false;
    }

    private void updateProgress() {
        if (doingTag || doingRate) {
            setProgressBarIndeterminateVisibility(Boolean.TRUE);
        } else {
            setProgressBarIndeterminateVisibility(Boolean.FALSE);
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
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
            public void onClick(DialogInterface dialog, int id) {
                getSupportLoaderManager().restartLoader(ARTIST_LOADER, null, ArtistActivity.this);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ArtistActivity.this.finish();
            }
        });
        return builder.create();
    }

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.tag_btn:
            String tagString = tagInput.getText().toString();
            if (tagString.length() == 0) {
                Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
            } else {
                doingTag = true;
                updateProgress();
                tagBtn.setEnabled(false);
                getSupportLoaderManager().initLoader(TAG_LOADER, null, tagSubmissionCallbacks);
            }
            break;
        case R.id.rate_btn:
            doingRate = true;
            updateProgress();
            rateBtn.setEnabled(false);
            getSupportLoaderManager().initLoader(RATING_LOADER, null, ratingSubmissionCallbacks);
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
        ReleaseGroupStub rg = artist.getReleases().get(position);
        Intent releaseIntent = new Intent(ArtistActivity.this, ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RG_MBID, rg.getMbid());
        startActivity(releaseIntent);
    }

    private void openLink(int position) {
        String link = artist.getLinks().get(position).getUrl();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(urlIntent);
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
        switch (result.getStatus()) {
        case SUCCESS:
            artist = result.getData();
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

    private LoaderCallbacks<AsyncResult<Float>> ratingSubmissionCallbacks = new LoaderCallbacks<AsyncResult<Float>>() {

        @Override
        public Loader<AsyncResult<Float>> onCreateLoader(int id, Bundle args) {
            int rating = (int) ratingInput.getRating();
            return new SubmitRatingLoader(ArtistActivity.this, getCredentials(), Entity.ARTIST, artist.getMbid(),
                    rating);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Float>> loader, AsyncResult<Float> data) {
            getSupportLoaderManager().destroyLoader(RATING_LOADER);
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(ArtistActivity.this, R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(ArtistActivity.this, R.string.toast_rate, Toast.LENGTH_SHORT).show();
                updateRating(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Float>> loader) {
            loader.reset();
        }
    };

    private void updateRating(Float newRating) {
        artist.setRating(newRating);
        rating.setRating(newRating);
        doingRate = false;
        updateProgress();
        rateBtn.setEnabled(true);
    }

    private LoaderCallbacks<AsyncResult<LinkedList<Tag>>> tagSubmissionCallbacks = new LoaderCallbacks<AsyncResult<LinkedList<Tag>>>() {

        @Override
        public Loader<AsyncResult<LinkedList<Tag>>> onCreateLoader(int id, Bundle args) {
            String tags = tagInput.getText().toString();
            return new SubmitTagsLoader(ArtistActivity.this, getCredentials(), Entity.ARTIST, artist.getMbid(), tags);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<LinkedList<Tag>>> loader, AsyncResult<LinkedList<Tag>> data) {
            getSupportLoaderManager().destroyLoader(TAG_LOADER);
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(ArtistActivity.this, R.string.toast_tag_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(ArtistActivity.this, R.string.toast_tag, Toast.LENGTH_SHORT).show();
                updateTags(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<LinkedList<Tag>>> loader) {
            loader.reset();
        }
    };

    private void updateTags(LinkedList<Tag> newTags) {
        artist.setTags(newTags);
        tags.setText(StringFormat.commaSeparateTags(newTags, this));
        doingTag = false;
        updateProgress();
        tagBtn.setEnabled(true);
    }

}

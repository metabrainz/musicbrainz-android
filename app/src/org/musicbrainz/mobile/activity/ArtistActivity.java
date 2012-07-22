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
import org.musicbrainz.mobile.adapter.list.ArtistRGAdapter;
import org.musicbrainz.mobile.adapter.list.WeblinkAdapter;
import org.musicbrainz.mobile.adapter.pager.ArtistPagerAdapter;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;
import org.musicbrainz.mobile.loader.ArtistLoader;
import org.musicbrainz.mobile.loader.SubmitRatingLoader;
import org.musicbrainz.mobile.loader.SubmitTagsLoader;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.util.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
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

    private String mbid;
    private Artist artist;
    private UserData userData;

    private View loading;
    private View error;
    
    private RatingBar rating;
    private TextView tags;
    private RatingBar ratingInput;
    private EditText tagInput;
    private ImageButton tagBtn;

    private boolean doingTag, doingRate;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        
        mbid = getIntent().getStringExtra(Extra.ARTIST_MBID);
        setContentView(R.layout.activity_artist);
        configurePager();
        findViews();
        setSupportProgressBarIndeterminateVisibility(false);
        getSupportLoaderManager().initLoader(ARTIST_LOADER, savedInstanceState, this);
    }

    protected void populateLayout() {
        TextView artistText = (TextView) findViewById(R.id.artist_artist);
        ListView releaseList = (ListView) findViewById(R.id.artist_releases);
        ListView linksList = (ListView) findViewById(R.id.artist_links);

        artistText.setText(artist.getName());
        releaseList.setAdapter(new ArtistRGAdapter(this, artist.getReleaseGroups()));
        releaseList.setOnItemClickListener(this);
        linksList.setAdapter(new WeblinkAdapter(this, artist.getLinks()));
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
        loading.setVisibility(View.GONE);
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
        loading = findViewById(R.id.loading);
        error = findViewById(R.id.error);
        rating = (RatingBar) findViewById(R.id.rating);
        tags = (TextView) findViewById(R.id.tags);
        tagInput = (EditText) findViewById(R.id.tag_input);
        ratingInput = (RatingBar) findViewById(R.id.rating_input);
        ratingInput.setOnRatingBarChangeListener(ratingListener);
        tagBtn = (ImageButton) findViewById(R.id.tag_btn);
        tagBtn.setOnClickListener(this);
    }

    private void disableEditViews() {
        ratingInput.setEnabled(false);
        ratingInput.setFocusable(false);
        tagInput.setEnabled(false);
        tagInput.setFocusable(false);
        tagBtn.setEnabled(false);
        tagBtn.setFocusable(false);
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
        getSupportMenuInflater().inflate(R.menu.artist, menu);
        ShareActionProvider actionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        actionProvider.setShareIntent(Utils.shareIntent(Configuration.ARTIST_SHARE + mbid));
        return true;
    }

    private void updateProgress() {
        if (doingTag || doingRate) {
            setSupportProgressBarIndeterminateVisibility(true);
        } else {
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tag_btn) {
            String tagString = tagInput.getText().toString();
            if (tagString.length() == 0) {
                Toast.makeText(this, R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
            } else {
                doingTag = true;
                updateProgress();
                tagBtn.setEnabled(false);
                getSupportLoaderManager().initLoader(TAG_LOADER, null, tagSubmissionCallbacks);
            }
        }
    }
    
    private OnRatingBarChangeListener ratingListener = new OnRatingBarChangeListener() {
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (fromUser) {
                doingRate = true;
                updateProgress();
                ratingInput.setEnabled(false);
                getSupportLoaderManager().initLoader(RATING_LOADER, null, ratingSubmissionCallbacks);
            }
        }
    };
    
    @Override
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
        return new ArtistLoader(getApplicationContext(), mbid);
    }

    @Override
    public void onLoadFinished(Loader<AsyncEntityResult<Artist>> loader, AsyncEntityResult<Artist> container) {
        handleLoadResult(container);
    }

    private void handleLoadResult(AsyncEntityResult<Artist> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            artist = result.getData();
            userData = result.getUserData();
            populateLayout();
            break;
        case EXCEPTION:
            showConnectionErrorWarning();
        }
    }

    private void showConnectionErrorWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = (Button) error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getSupportLoaderManager().restartLoader(ARTIST_LOADER, null, ArtistActivity.this);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<AsyncEntityResult<Artist>> loader) {
        loader.reset();
    }

    private LoaderCallbacks<AsyncResult<Float>> ratingSubmissionCallbacks = new LoaderCallbacks<AsyncResult<Float>>() {

        @Override
        public Loader<AsyncResult<Float>> onCreateLoader(int id, Bundle args) {
            int rating = (int) ratingInput.getRating();
            return new SubmitRatingLoader(getApplicationContext(), Entity.ARTIST, artist.getMbid(), rating);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Float>> loader, AsyncResult<Float> data) {
            getSupportLoaderManager().destroyLoader(RATING_LOADER);
            onFinishedRating();
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

    private void onFinishedRating() {
        doingRate = false;
        updateProgress();
        ratingInput.setEnabled(true);
    }

    private void updateRating(Float newRating) {
        artist.setRating(newRating);
        rating.setRating(newRating);
    }

    private LoaderCallbacks<AsyncResult<LinkedList<Tag>>> tagSubmissionCallbacks = new LoaderCallbacks<AsyncResult<LinkedList<Tag>>>() {

        @Override
        public Loader<AsyncResult<LinkedList<Tag>>> onCreateLoader(int id, Bundle args) {
            String tags = tagInput.getText().toString();
            return new SubmitTagsLoader(getApplicationContext(), Entity.ARTIST, artist.getMbid(), tags);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<LinkedList<Tag>>> loader, AsyncResult<LinkedList<Tag>> data) {
            getSupportLoaderManager().destroyLoader(TAG_LOADER);
            onFinishedTagging();
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

    private void onFinishedTagging() {
        doingTag = false;
        updateProgress();
        tagBtn.setEnabled(true);
    }

    private void updateTags(LinkedList<Tag> newTags) {
        artist.setTags(newTags);
        tags.setText(StringFormat.commaSeparateTags(newTags, this));
    }

}

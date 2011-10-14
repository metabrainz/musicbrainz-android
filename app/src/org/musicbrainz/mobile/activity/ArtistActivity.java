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

import java.io.IOException;
import java.util.Collection;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.MBEntity;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.android.api.webservice.WebServiceUtils;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.ArtistReleaseGroupAdapter;
import org.musicbrainz.mobile.adapter.LinkAdapter;
import org.musicbrainz.mobile.strings.StringFormat;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.widget.FocusTextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends SuperActivity implements View.OnClickListener, ListView.OnItemClickListener {

    public static final String INTENT_MBID = "mbid";

    private Artist data;
    private String mbid;

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

    private UserData userData;

    // status
    private boolean doingTag = false;
    private boolean doingRate = false;

    private WebClient webService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebClient(Config.USER_AGENT);

        mbid = getIntent().getStringExtra(INTENT_MBID);
        new LookupTask().execute();
        setContentView(R.layout.loading);
        setupActionBarWithHome();
    }

    protected void populate() {

        setContentView(R.layout.activity_artist);
        actionBar = setupActionBarWithHome();
        addActionBarShare();

        // info header
        FocusTextView artist = (FocusTextView) findViewById(R.id.artist_artist);
        rating = (RatingBar) findViewById(R.id.artist_rating);
        tags = (FocusTextView) findViewById(R.id.artist_tags);

        artist.setText(data.getName());
        rating.setRating(data.getRating());
        tags.setText(StringFormat.commaSeparate(data.getTags()));

        ListView releaseList = (ListView) findViewById(R.id.artist_releases);
        releaseList.setOnItemClickListener(this);
        releaseList.setAdapter(new ArtistReleaseGroupAdapter(this, data.getReleaseGroups()));

        ListView linksList = (ListView) findViewById(R.id.artist_links);
        linksList.setOnItemClickListener(this);
        linksList.setAdapter(new LinkAdapter(this, data.getLinks()));

        // setup tabs
        setupTabs();

        tagInput = (EditText) findViewById(R.id.tag_input);

        tagBtn = (Button) findViewById(R.id.tag_btn);
        tagBtn.setOnClickListener(this);

        ratingInput = (RatingBar) findViewById(R.id.rating_input);

        rateBtn = (Button) findViewById(R.id.rate_btn);
        rateBtn.setOnClickListener(this);

        // display messages for no tags, no releases or no links
        if (data.getTags().isEmpty())
            tags.setText(getText(R.string.no_tags));

        if (data.getReleases().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.noreleases);
            noRes.setVisibility(View.VISIBLE);
        }

        if (data.getLinks().isEmpty()) {
            TextView noRes = (TextView) findViewById(R.id.nolinks);
            noRes.setVisibility(View.VISIBLE);
        }

        if (!loggedIn) {
            disableEditViews();
            findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
        }
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

    private void addActionBarShare() {
        Action share = actionBar.newAction();
        share.setIcon(R.drawable.ic_actionbar_share);
        share.setIntent(createShareIntent());
        actionBar.addAction(share);
    }

    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.musicbrainz.org/artist/" + mbid);
        return Intent.createChooser(intent, getString(R.string.share));
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

        protected Boolean doInBackground(Void... params) {

            try {
                data = webService.lookupArtist(mbid);
                if (loggedIn) {
                    webService.setCredentials(getUsername(), getPassword());
                    webService.setAppVersion(getVersion());
                    userData = webService.getUserData(MBEntity.ARTIST, data.getMbid());
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {

            if (success) {
                populate();
                if (loggedIn) {
                    tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
                    ratingInput.setRating(userData.getRating());
                }
            } else {
                // error or connection timed out - retry dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ArtistActivity.this);
                builder.setMessage(getString(R.string.err_text)).setCancelable(false)
                        .setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // restart search thread
                                new LookupTask().execute();
                                dialog.cancel();
                            }
                        }).setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish activity
                                ArtistActivity.this.finish();
                            }
                        });
                try {
                    Dialog conError = builder.create();
                    conError.show();
                } catch (Exception e) {
                    Log.e("Connection timed out but Activity has closed anyway");
                }
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

            Collection<String> processedTags = WebServiceUtils.sanitiseCommaSeparatedTags(tags[0]);

            try {
                webService.setCredentials(getUsername(), getPassword());
                webService.setAppVersion(getVersion());
                webService.submitTags(MBEntity.ARTIST, data.getMbid(), processedTags);
                data.setTags(webService.lookupTags(MBEntity.ARTIST, data.getMbid()));
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {

            tags.setText(StringFormat.commaSeparate(data.getTags()));

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

            try {
                webService.setCredentials(getUsername(), getPassword());
                webService.setAppVersion(getVersion());
                webService.submitRating(MBEntity.ARTIST, data.getMbid(), rating[0]);
                float newRating = webService.lookupRating(MBEntity.ARTIST, data.getMbid());
                data.setRating(newRating);
            } catch (IOException e) {
                return false;
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

        switch (v.getId()) {

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
            ReleaseGroupStub rg = data.getReleases().get(position);

            Intent releaseIntent = new Intent(ArtistActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra("rg_id", rg.getMbid());
            startActivity(releaseIntent);
        } else {
            String link = data.getLinks().get(position).getUrl();
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(urlIntent);
        }
    }

}

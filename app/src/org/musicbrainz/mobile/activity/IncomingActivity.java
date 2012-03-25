/*
 * Copyright (C) 2011 Jamie McDonald
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

import java.util.List;

import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * This Activity parses incoming URI intents from external applications and
 * starts the appropriate Activity before finishing.
 */
public class IncomingActivity extends Activity {

    private static final String URI_SEARCH = "search";
    private static final String URI_RELEASE = "release";
    private static final String URI_ARTIST = "artist";
    private static final int MBID_LENGTH = 36;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri incoming = getIntent().getData();
        List<String> segments = incoming.getPathSegments();
        String type = segments.get(0);

        if (type.equals(URI_ARTIST)) {
            startArtistActivity(segments.get(1));
        } else if (type.equals(URI_RELEASE)) {
            startReleaseActivity(segments.get(1));
        } else if (type.equals(URI_SEARCH)) {
            doSearch(segments);
        } else {
            displayErrorLayout("Unrecognised URI segment: action");
        }
        this.finish();
    }

    private void doSearch(List<String> segments) {
        if (segments.size() == 2) {
            allSearch(segments);
        } else if (segments.size() == 3) {
            entitySearch(segments);
        } else {
            displayErrorLayout("Too many URI segments");
        }
    }

    private void allSearch(List<String> segments) {
        String query = segments.get(1);
        startSearchResultsActivity(Extra.ALL, query);
    }

    private void entitySearch(List<String> segments) {
        String entity = segments.get(1);
        String query = segments.get(2);
        if (entity.equals(URI_ARTIST)) {
            startSearchResultsActivity(Extra.ARTIST, query);
        } else if (entity.equals(URI_RELEASE)) {
            startSearchResultsActivity(Extra.RELEASE_GROUP, query);
        } else {
            displayErrorLayout("Unrecognised URI segment: search type");
        }
    }

    private void startArtistActivity(String mbid) {
        if (isValidMbid(mbid)) {
            Intent artistIntent = new Intent(IncomingActivity.this, ArtistActivity.class);
            artistIntent.putExtra(Extra.ARTIST_MBID, mbid);
            startActivity(artistIntent);
        } else {
            displayErrorLayout("Invalid MBID");
        }
    }

    private void startReleaseActivity(String mbid) {
        if (isValidMbid(mbid)) {
            Intent releaseIntent = new Intent(IncomingActivity.this, ReleaseActivity.class);
            releaseIntent.putExtra(Extra.RELEASE_MBID, mbid);
            startActivity(releaseIntent);
        } else {
            displayErrorLayout("Invalid MBID");
        }
    }

    private void startSearchResultsActivity(String type, String query) {
        Intent searchIntent = new Intent(IncomingActivity.this, SearchActivity.class);
        searchIntent.putExtra(Extra.TYPE, type);
        searchIntent.putExtra(Extra.QUERY, query);
        startActivity(searchIntent);
    }

    private boolean isValidMbid(String mbid) {
        return (mbid.length() == MBID_LENGTH);
    }

    private void displayErrorLayout(String message) {
        Intent errorIntent = new Intent(IncomingActivity.this, IntentErrorActivity.class);
        errorIntent.putExtra(Extra.MESSAGE, message);
        startActivity(errorIntent);
    }

}

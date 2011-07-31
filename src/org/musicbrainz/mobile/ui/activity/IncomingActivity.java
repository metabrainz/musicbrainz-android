/*
 * Copyright (C) 2011 Jamie McDonald
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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class IncomingActivity extends Activity {
	
	private static final int MBID_LENGTH = 36;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Uri incoming = getIntent().getData();
        List<String> segs = incoming.getPathSegments();
        
        String type = segs.get(0);
        String mbid = segs.get(1);
        
        if (type.equals("artist") && isValidMbid(mbid)) {
            displayArtist(mbid);
        } else if (type.equals("release") && isValidMbid(mbid)) {
            displayRelease(mbid);
        }
        this.finish();
    }
	
	private void displayArtist(String mbid) {
		Intent artistIntent = new Intent (IncomingActivity.this, ArtistActivity.class);
		artistIntent.putExtra(ArtistActivity.INTENT_MBID, mbid);
		startActivity(artistIntent);
	}

	private void displayRelease(String mbid) {
		Intent releaseIntent = new Intent (IncomingActivity.this, ReleaseActivity.class);
		releaseIntent.putExtra(ReleaseActivity.INTENT_RELEASE_MBID, mbid);
		startActivity(releaseIntent);
	}
	
	private boolean isValidMbid(String mbid) {
		return (mbid.length() == MBID_LENGTH);
	}
	
}

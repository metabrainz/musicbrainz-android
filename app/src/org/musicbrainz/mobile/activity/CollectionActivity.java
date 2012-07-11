/*
 * Copyright (C) 2012 Jamie McDonald
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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.CollectionFragment.FragmentLoadingCallbacks;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class CollectionActivity extends MusicBrainzActivity implements FragmentLoadingCallbacks {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setSupportProgressBarIndeterminateVisibility(false);
        setTitleFromIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitleFromIntent() {
        String collectionName = getIntent().getStringExtra(Extra.TITLE);
        getSupportActionBar().setTitle(collectionName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(IntentFactory.getCollectionList(getApplicationContext()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadStart() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadFinish() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

}

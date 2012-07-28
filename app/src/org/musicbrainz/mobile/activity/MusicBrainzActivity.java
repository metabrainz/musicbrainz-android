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

import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.util.Utils;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public abstract class MusicBrainzActivity extends SherlockFragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            startActivity(IntentFactory.getDashboard(getApplicationContext()));
            return true;
        case R.id.menu_preferences:
            startActivity(IntentFactory.getSettings(getApplicationContext()));
            return true;
        case R.id.menu_feedback:
            sendFeedback();
            return true;
        case R.id.legacy_search:
            onSearchRequested();
            return true;
        case R.id.menu_login:
            startActivity(IntentFactory.getLogin(getApplicationContext()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendFeedback() {
        try {
            startActivity(Utils.emailIntent(Configuration.FEEDBACK_EMAIL, Configuration.FEEDBACK_SUBJECT));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.toast_feedback_fail, Toast.LENGTH_LONG).show();
        }
    }
    
    protected boolean isUserLoggedIn() {
        return MusicBrainzApp.getApp(this).isUserLoggedIn();
    }
    
}

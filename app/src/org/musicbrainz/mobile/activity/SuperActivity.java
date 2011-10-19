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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Secrets;
import org.musicbrainz.mobile.util.SimpleEncrypt;
import org.musicbrainz.mobile.util.Utils;

import com.markupartist.android.widget.ActionBar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Provides methods used across many Activity classes.
 */
public abstract class SuperActivity extends Activity {

    protected boolean loggedIn = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getUsername() != null) {
            loggedIn = true;
        }
    }

    protected boolean shouldProvideSearchSuggestions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("search_suggestions", true);
    }

    protected String getUsername() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        return prefs.getString("username", null);
    }

    protected String getPassword() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String obscuredPassword = prefs.getString("password", null);
        return SimpleEncrypt.decrypt(new Secrets().getKey(), obscuredPassword);
    }

    public String getVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "unknown";
        }
    }
    
    public String generateClientId() {
        return Config.CLIENT_NAME + "-" + getVersion();
    }
    
    public String generateUserAgent() {
        return Config.USER_AGENT + "/" + getVersion();
    }

    protected ActionBar setupActionBarWithHome() {
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        getMenuInflater().inflate(R.menu.actionbar, actionBar.asMenu());
        actionBar.findAction(R.id.actionbar_item_home).setIntent(HomeActivity.createIntent(this));
        actionBar.setDisplayShowHomeEnabled(true);
        return actionBar;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
        case R.id.menu_about:
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        case R.id.menu_donate:
            Intent donateIntent = new Intent(this, DonateActivity.class);
            startActivity(donateIntent);
            return true;
        case R.id.menu_feedback:
            sendFeedback();
            return true;
        case R.id.menu_preferences:
            Intent prefsIntent = new Intent(this, PreferencesActivity.class);
            startActivity(prefsIntent);
            return true;
        }
        return false;
    }

    private void sendFeedback() {
        try {
            startActivity(Utils.emailIntent(Config.FEEDBACK_EMAIL, Config.FEEDBACK_SUBJECT));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.toast_feedback_fail, Toast.LENGTH_LONG).show();
        }
    }

}

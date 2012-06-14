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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.util.PreferenceUtils.Pref;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.widget.Toast;

public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preference clear = (Preference) findPreference(Pref.PREF_CLEAR_SUGGESTIONS);
        clear.setOnPreferenceClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(prefChanges);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(prefChanges);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(Pref.PREF_CLEAR_SUGGESTIONS)) {
            clearSuggestionHistory();
            return true;
        }
        return false;
    }

    private void clearSuggestionHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE);
        suggestions.clearHistory();
        Toast.makeText(this, R.string.toast_search_cleared, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
        case android.R.id.home:
            startActivity(IntentFactory.getDashboard(getApplicationContext()));
            return true;
        }
        return false;
    }
    
    OnSharedPreferenceChangeListener prefChanges = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            requestBackup();
        }
    };

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void requestBackup() {
        try {
            Class managerClass = Class.forName("android.app.backup.BackupManager");
            Constructor managerConstructor = managerClass.getConstructor(Context.class);
            Object manager = managerConstructor.newInstance(getApplicationContext());
            Method m = managerClass.getMethod("dataChanged");
            m.invoke(manager);
        } catch (ClassNotFoundException e) {
            Log.d("Backup manager not found");
        } catch (Throwable t) {
            Log.e(t.getMessage());
        }
    }

}

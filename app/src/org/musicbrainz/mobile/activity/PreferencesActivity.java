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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.suggestion.SuggestionProvider;

import com.markupartist.android.widget.ActionBar;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.SearchRecentSuggestions;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity implements OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupActionbarWithHome();
        addPreferencesFromResource(R.xml.preferences);

        Preference clear = (Preference) findPreference("clear_suggestions");
        clear.setOnPreferenceClickListener(this);
    }

    private void setupActionbarWithHome() {
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        getMenuInflater().inflate(R.menu.actionbar, actionBar.asMenu());
        actionBar.findAction(R.id.actionbar_item_home).setIntent(HomeActivity.createIntent(this));
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference.getKey().equals("clear_suggestions")) {
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

}

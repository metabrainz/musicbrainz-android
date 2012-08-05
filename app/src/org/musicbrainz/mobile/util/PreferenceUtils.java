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

package org.musicbrainz.mobile.util;

import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.config.Secrets;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    
    public interface Pref {
        public static final String PREFS_USER = "user";
        public static final String PREF_USERNAME = "username";
        public static final String PREF_PASSWORD = "password";
        public static final String PREF_SUGGESTIONS = "search_suggestions";
        public static final String PREF_CLEAR_SUGGESTIONS = "clear_suggestions";
        public static final String PREF_BUGSENSE = "send_crashlogs";
    }

    public static String getUsername() {
        return getUserPreferences().getString(Pref.PREF_USERNAME, null);
    }

    public static String getPassword() {
        String obscuredPassword = getUserPreferences().getString(Pref.PREF_PASSWORD, null);
        return SimpleEncrypt.decrypt(new Secrets().getKey(), obscuredPassword);
    }
    
    public static void clearUser() {
        SharedPreferences prefs = getUserPreferences();
        Editor spe = prefs.edit();
        spe.clear();
        spe.commit();
    }
    
    private static SharedPreferences getUserPreferences() {
        return MusicBrainzApp.getContext().getSharedPreferences(Pref.PREFS_USER, Context.MODE_PRIVATE);
    }

    public static boolean shouldProvideSearchSuggestions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Pref.PREF_SUGGESTIONS, true);
    }

}

package org.musicbrainz.mobile.util;

import org.musicbrainz.mobile.App;
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
        return App.getContext().getSharedPreferences(Pref.PREFS_USER, Context.MODE_PRIVATE);
    }

    public static boolean shouldProvideSearchSuggestions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Pref.PREF_SUGGESTIONS, true);
    }

}

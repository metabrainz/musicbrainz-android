package org.musicbrainz.mobile.user;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.util.SimpleEncrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferences implements AppUser {
    
    private static final String USER_PREFERENCE_FILE = "user";
    
    private interface PreferenceName {
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
        static final String SUGGESTIONS = "search_suggestions";
        static final String REPORT_CRASHES = "send_crashlogs";
    }

    @Override
    public String getUsername() {
        return getUserPreferences().getString(PreferenceName.USERNAME, null);
    }

    @Override
    public void setUsername(String username) {
        SharedPreferences prefs = getUserPreferences();
        prefs.edit().putString(PreferenceName.USERNAME, username).commit();
    }
    
    @Override
    public String getPassword() {
        String obscuredPassword = getUserPreferences().getString(PreferenceName.PASSWORD, null);
        return SimpleEncrypt.decrypt(new Secrets().getKey(), obscuredPassword);
    }
    
    @Override
    public void setPassword(String password) {
        SharedPreferences prefs = getUserPreferences();
        String obscuredPassword = SimpleEncrypt.encrypt(new Secrets().getKey(), password);
        prefs.edit().putString(PreferenceName.PASSWORD, obscuredPassword).commit();
    }
    
    @Override
    public boolean isLoggedIn() {
        return getUsername() != null;
    }

    @Override
    public void clearData() {
        SharedPreferences prefs = getUserPreferences();
        prefs.edit().clear().commit();
    }

    public void setSearchSuggestionsEnabled(boolean enabled) {
        SharedPreferences prefs = getDefaultPreferences();
        prefs.edit().putBoolean(PreferenceName.SUGGESTIONS, enabled).commit();
    }

    public boolean isSearchSuggestionsEnabled() {
        return getDefaultPreferences().getBoolean(PreferenceName.SUGGESTIONS, true);
    }
    
    public void setCrashReportsEnabled(boolean enabled) {
        SharedPreferences prefs = getDefaultPreferences();
        prefs.edit().putBoolean(PreferenceName.REPORT_CRASHES, enabled).commit();
    }

    public boolean isCrashReportingEnabled() {
        return getDefaultPreferences().getBoolean(PreferenceName.REPORT_CRASHES, true);
    }
    
    private SharedPreferences getDefaultPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }
    
    private SharedPreferences getUserPreferences() {
        return App.getContext().getSharedPreferences(USER_PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

}

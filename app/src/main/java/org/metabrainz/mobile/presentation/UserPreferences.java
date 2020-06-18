package org.metabrainz.mobile.presentation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metabrainz.mobile.App;

import static org.metabrainz.mobile.App.TAGGER_ROOT_DIRECTORY;

public class UserPreferences {

    public static final String PREFERENCE_CLEAR_SUGGESTIONS = "clear_suggestions";
    public static final String PREFERENCE_TAGGER_DIRECTORY = "tagger_directory";
    public static final String PREFERENCE_LISTENBRAINZ_TOKEN = "listenbrainz_user_token";
    public static final String PREFERENCE_LISTENING_ENABLED = "listening_enabled";
    private static final String PREFERENCE_LISTENING_SPOTIFY = "listening_spotify_enabled";
    private static final String PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections";
    private static final String PREFERENCE_RATINGS_TAGS = "ratings_tags";
    private static final String PREFERENCE_SYSTEM_LANGUAGE = "use_english";

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static boolean getPrivateCollectionsPreference() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_GET_PRIVATE_COLLECTIONS, false);
    }

    public static boolean getRatingsTagsPreference() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_RATINGS_TAGS, false);
    }

    public static String getTaggerDirectoryPreference() {
        return UserPreferences.getPreferences().getString(PREFERENCE_TAGGER_DIRECTORY, TAGGER_ROOT_DIRECTORY);
    }

    public static boolean getSystemLanguagePreference() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_SYSTEM_LANGUAGE, false);
    }

    public static void setPreferenceTaggerDirectory(String path) {
        SharedPreferences.Editor editor = UserPreferences.getPreferences().edit();
        editor.putString(PREFERENCE_TAGGER_DIRECTORY, path);
        editor.apply();
    }

    public static String getPreferenceListenBrainzToken() {
        return UserPreferences.getPreferences().getString(PREFERENCE_LISTENBRAINZ_TOKEN, null);
    }

    public static void setPreferenceListeningEnabled(boolean value) {
        SharedPreferences.Editor editor = UserPreferences.getPreferences().edit();
        editor.putBoolean(PREFERENCE_LISTENING_ENABLED, value);
        editor.apply();
    }

    public static boolean getPreferenceListeningEnabled() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_LISTENING_ENABLED, false);
    }

    public static boolean getPreferenceListeningSpotifyEnabled() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_LISTENING_SPOTIFY, false);
    }

}

package org.metabrainz.mobile.presentation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metabrainz.mobile.App;

import static org.metabrainz.mobile.App.TAGGER_ROOT_DIRECTORY;

public class UserPreferences {

    public static final String PREFERENCE_CLEAR_SUGGESTIONS = "clear_suggestions";
    public static final String PREFERENCE_TAGGER_DIRECTORY = "tagger_directory";
    public static final String PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections";
    public static final String PREFERENCE_RATINGS_TAGS = "ratings_tags";

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

    public static void setPreferenceTaggerDirectory(String path) {
        SharedPreferences.Editor editor = UserPreferences.getPreferences().edit();
        editor.putString(PREFERENCE_TAGGER_DIRECTORY, path);
        editor.apply();
    }

}

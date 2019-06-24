package org.metabrainz.mobile.presentation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metabrainz.mobile.App;

public class UserPreferences {

    private static final String PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections";
    private static final String PREFERENCE_RATINGS_TAGS = "ratings_tags";

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static void setPreferenceGetPrivateCollections(boolean choice) {
        SharedPreferences.Editor editor = UserPreferences.getPreferences().edit();
        editor.putBoolean(PREFERENCE_GET_PRIVATE_COLLECTIONS, choice);
        editor.apply();
    }

    public static boolean getPrivateCollectionsPreference() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_GET_PRIVATE_COLLECTIONS, false);
    }

    public static boolean getRatingsTagsPreference() {
        return UserPreferences.getPreferences().getBoolean(PREFERENCE_RATINGS_TAGS, false);
    }

    public static void setPreferenceRatingsTags(boolean choice) {
        SharedPreferences.Editor editor = UserPreferences.getPreferences().edit();
        editor.putBoolean(PREFERENCE_RATINGS_TAGS, choice);
        editor.apply();
    }

}

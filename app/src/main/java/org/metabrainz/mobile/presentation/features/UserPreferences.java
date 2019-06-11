package org.metabrainz.mobile.presentation.features;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metabrainz.mobile.App;

public class UserPreferences {

    public static final String PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections";

    public static SharedPreferences getPreferences() {
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

}

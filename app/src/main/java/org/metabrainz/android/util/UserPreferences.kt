package org.metabrainz.android.util

import androidx.preference.PreferenceManager
import org.metabrainz.android.application.App

object UserPreferences {

    const val PREFERENCE_CLEAR_SUGGESTIONS = "clear_suggestions"
    const val PREFERENCE_PICARD_PORT = "picard_port"
    const val PREFERENCE_IP_ADDRESS = "ip_address"
    private const val PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections"
    private const val PREFERENCE_RATINGS_TAGS = "ratings_tags"
    private const val PREFERENCE_SYSTEM_LANGUAGE = "use_english"
    const val PREFERENCE_SYSTEM_THEME = "app_theme"
    private const val PREFERENCE_ONBOARDING = "onboarding"
    private val preferences = PreferenceManager.getDefaultSharedPreferences(App.context!!)

    fun setOnBoardingCompleted() {
        val editor = preferences.edit()
        editor.putBoolean(PREFERENCE_ONBOARDING, true)
        editor.apply()
    }

    val privateCollectionsPreference = preferences.getBoolean(PREFERENCE_GET_PRIVATE_COLLECTIONS, false)
    val ratingsTagsPreference = preferences.getBoolean(PREFERENCE_RATINGS_TAGS, false)
    val systemLanguagePreference = preferences.getBoolean(PREFERENCE_SYSTEM_LANGUAGE, false)
    val themePreference = preferences.getString(PREFERENCE_SYSTEM_THEME, "Use device theme")
    
    val preferencePicardPort: String
        get() = preferences.getString(PREFERENCE_PICARD_PORT, "8000")!!
    val preferenceIpAddress: String?
        get() = preferences.getString(PREFERENCE_IP_ADDRESS, null)
}
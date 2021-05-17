package org.metabrainz.mobile.presentation

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.metabrainz.mobile.App
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences

object UserPreferences {
    const val PREFERENCE_CLEAR_SUGGESTIONS = "clear_suggestions"
    const val PREFERENCE_TAGGER_DIRECTORY = "tagger_directory"
    const val PREFERENCE_LISTENBRAINZ_TOKEN = "listenbrainz_user_token"
    const val PREFERENCE_LISTENING_ENABLED = "listening_enabled"
    private const val PREFERENCE_LISTENING_SPOTIFY = "listening_spotify_enabled"
    private const val PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections"
    private const val PREFERENCE_RATINGS_TAGS = "ratings_tags"
    private const val PREFERENCE_SYSTEM_LANGUAGE = "use_english"
    private const val PREFERENCE_ONBOARDING = "show_onboarding"
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(App.context)

    @JvmStatic
    fun setOnBoardingCompleted() {
        val editor = LoginSharedPreferences.preferences.edit()
        editor.putBoolean(PREFERENCE_ONBOARDING, true)
        editor.apply()
    }

    val onBoardingStatus: Boolean
        get() = preferences.getBoolean(PREFERENCE_ONBOARDING, false)
    @JvmStatic
    val privateCollectionsPreference: Boolean
        get() = preferences.getBoolean(PREFERENCE_GET_PRIVATE_COLLECTIONS, false)
    val ratingsTagsPreference: Boolean
        get() = preferences.getBoolean(PREFERENCE_RATINGS_TAGS, false)
    val taggerDirectoryPreference: String?
        get() = preferences.getString(PREFERENCE_TAGGER_DIRECTORY, App.TAGGER_ROOT_DIRECTORY)
    @JvmStatic
    val systemLanguagePreference: Boolean
        get() = preferences.getBoolean(PREFERENCE_SYSTEM_LANGUAGE, false)
    @JvmStatic
    val preferenceListenBrainzToken: String?
        get() = preferences.getString(PREFERENCE_LISTENBRAINZ_TOKEN, null)
    @JvmStatic
    var preferenceListeningEnabled: Boolean
        get() = preferences.getBoolean(PREFERENCE_LISTENING_ENABLED, false)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(PREFERENCE_LISTENING_ENABLED, value)
            editor.apply()
        }
    @JvmStatic
    val preferenceListeningSpotifyEnabled: Boolean
        get() = preferences.getBoolean(PREFERENCE_LISTENING_SPOTIFY, false)
}
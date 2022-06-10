package org.metabrainz.android.presentation

import androidx.preference.PreferenceManager
import org.metabrainz.android.App
import org.metabrainz.android.presentation.features.login.LoginSharedPreferences

object UserPreferences {

    const val PREFERENCE_CLEAR_SUGGESTIONS = "clear_suggestions"
    const val PREFERENCE_LISTENBRAINZ_TOKEN = "listenbrainz_user_token"
    const val PREFERENCE_PICARD_PORT = "picard_port"
    const val PREFERENCE_IP_ADDRESS = "ip_address"
    const val PREFERENCE_LISTENING_ENABLED = "listening_enabled"
    private const val PREFERENCE_LISTENING_SPOTIFY = "listening_spotify_enabled"
    private const val PREFERENCE_GET_PRIVATE_COLLECTIONS = "private_collections"
    private const val PREFERENCE_RATINGS_TAGS = "ratings_tags"
    private const val PREFERENCE_SYSTEM_LANGUAGE = "use_english"
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

    val preferencePicardPort: String
        get() = preferences.getString(PREFERENCE_PICARD_PORT, "8000")!!
    val preferenceIpAddress: String?
        get() = preferences.getString(PREFERENCE_IP_ADDRESS, null)

    val preferenceListenBrainzToken = preferences.getString(PREFERENCE_LISTENBRAINZ_TOKEN, null)
    var preferenceListeningEnabled: Boolean
        get() = preferences.getBoolean(PREFERENCE_LISTENING_ENABLED, false)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(PREFERENCE_LISTENING_ENABLED, value)
            editor.apply()
        }
    val preferenceListeningSpotifyEnabled = preferences.getBoolean(PREFERENCE_LISTENING_SPOTIFY, false)
}
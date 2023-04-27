package org.metabrainz.android.ui.screens.login

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.metabrainz.android.application.App
import org.metabrainz.android.model.entities.AccessToken
import org.metabrainz.android.model.userdata.UserInfo

object LoginSharedPreferences {
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    private const val USERNAME = "username"
    const val STATUS_LOGGED_IN = 1
    const val STATUS_LOGGED_OUT = 0
    val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(App.context!!)

    fun saveOAuthToken(token: org.metabrainz.android.model.entities.AccessToken) {
        val editor = preferences.edit()
        editor.putString(ACCESS_TOKEN, token.accessToken)
        editor.putString(REFRESH_TOKEN, token.refreshToken)
        editor.apply()
    }

    fun saveUserInfo(userInfo: UserInfo) {
        val editor = preferences.edit()
        editor.putString(USERNAME, userInfo.username)
        editor.apply()
    }

    fun logoutUser() {
        val editor = preferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.remove(USERNAME)
        editor.apply()
    }

    @JvmStatic
    val loginStatus: Int
        get() {
            val accessToken = accessToken
            val username = username
            return if (accessToken!!.isNotEmpty() && username!!.isNotEmpty()) STATUS_LOGGED_IN else STATUS_LOGGED_OUT
        }
    val accessToken: String?
        get() = preferences.getString(ACCESS_TOKEN, "")
    @JvmStatic
    val username: String?
        get() = preferences.getString(USERNAME, "")
    val refreshToken: String?
        get() = preferences.getString(REFRESH_TOKEN, "")
}
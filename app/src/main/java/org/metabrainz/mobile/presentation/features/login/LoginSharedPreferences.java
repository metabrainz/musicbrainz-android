package org.metabrainz.mobile.presentation.features.login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserInfo;

public class LoginSharedPreferences {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    private static final String USERNAME = "username";
    public static final int STATUS_LOGGED_IN = 1;
    public static final int STATUS_LOGGED_OUT = 0;

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static void saveOAuthToken(AccessToken token) {
        SharedPreferences.Editor editor = LoginSharedPreferences.getPreferences().edit();
        editor.putString(ACCESS_TOKEN, token.getAccessToken());
        editor.putString(REFRESH_TOKEN, token.getRefreshToken());
        editor.apply();
    }

    public static void saveUserInfo(UserInfo userInfo) {
        SharedPreferences.Editor editor = LoginSharedPreferences.getPreferences().edit();
        editor.putString(USERNAME, userInfo.getUsername());
        editor.apply();
    }

    public static void logoutUser() {
        SharedPreferences.Editor editor = LoginSharedPreferences.getPreferences().edit();
        editor.remove(ACCESS_TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.remove(USERNAME);
        editor.apply();
    }

    public static int getLoginStatus() {
        String accessToken = LoginSharedPreferences.getAccessToken();
        String username = LoginSharedPreferences.getUsername();
        if (!accessToken.isEmpty() && !username.isEmpty())
            return STATUS_LOGGED_IN;
        else
            return STATUS_LOGGED_OUT;
    }

    public static String getAccessToken() {
        return LoginSharedPreferences.getPreferences().getString(ACCESS_TOKEN, "");
    }

    public static String getUsername() {
        return LoginSharedPreferences.getPreferences().getString(USERNAME, "");
    }

    public static String getRefreshToken() {
        return LoginSharedPreferences.getPreferences().getString(REFRESH_TOKEN, "");
    }
}

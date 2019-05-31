package org.metabrainz.mobile.data.sources.api;

import android.content.SharedPreferences;

import org.metabrainz.mobile.presentation.LoginSharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private SharedPreferences preferences;

    public HeaderInterceptor(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request;

        if (original.url().encodedPath().contains("oauth"))
            return chain.proceed(original);
        else {
            if (checkLoginStatus())
                request = original.newBuilder()
                        .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                        .addHeader("Accept", "application/json")
                        .header("Authorization", " Bearer " + getAccessToken()).build();
            else
                request = original.newBuilder()
                        .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                        .addHeader("Accept", "application/json").build();

            return chain.proceed(request);
        }
    }

    private boolean checkLoginStatus() {
        return LoginSharedPreferences.getLoginStatus(preferences)
                == LoginSharedPreferences.STATUS_LOGGED_IN;
    }

    private String getAccessToken() {
        return LoginSharedPreferences.getAccessToken(preferences);
    }

}

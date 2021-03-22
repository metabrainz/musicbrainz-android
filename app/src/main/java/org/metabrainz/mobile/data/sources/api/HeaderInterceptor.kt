package org.metabrainz.mobile.data.sources.api;

import androidx.annotation.NonNull;

import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request;

        // Do not add any headers if fetching data from wiki
        if (original.url().host().contains("wiki"))
            return chain.proceed(original);

        // Do not add Authorization Header if request is sent to OAuth endpoint except to fetch userinfo
        if (original.url().encodedPath().contains("oauth2")) {
            if (original.url().encodedPath().contains("userinfo"))
                request = original.newBuilder()
                        .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                        .addHeader("Accept", "application/json")
                        .header("Authorization", " Bearer " + getAccessToken()).build();
            else return chain.proceed(original);
        }
        // Check if user is logged in and add authorization header accordingly
        else {
            if (checkLoginStatus()) {
                request = original.newBuilder()
                        .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                        .addHeader("Accept", "application/json")
                        .header("Authorization", " Bearer " + getAccessToken()).build();
            } else {
                request = original.newBuilder()
                        .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                        .addHeader("Accept", "application/json").build();
            }
        }

        return chain.proceed(request);
    }

    private boolean checkLoginStatus() {
        return LoginSharedPreferences.getLoginStatus()
                == LoginSharedPreferences.STATUS_LOGGED_IN;
    }

    private String getAccessToken() {
        return LoginSharedPreferences.getAccessToken();
    }

}

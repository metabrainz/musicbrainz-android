package org.metabrainz.mobile.data.sources.api;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.presentation.LoginSharedPreferences;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class OAuthAuthenticator implements Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {

        LoginService service = MusicBrainzServiceGenerator.createService(LoginService.class,
                false);

        String refreshToken = LoginSharedPreferences.getRefreshToken();

        Call<AccessToken> call = service.refreshAccessToken(refreshToken,
                "refresh_token",
                MusicBrainzServiceGenerator.CLIENT_ID,
                MusicBrainzServiceGenerator.CLIENT_SECRET);

        retrofit2.Response<AccessToken> newResponse = call.execute();

        AccessToken token = newResponse.body();

        SharedPreferences.Editor editor = LoginSharedPreferences.getPreferences().edit();
        editor.putString(LoginSharedPreferences.REFRESH_TOKEN, token.getRefreshToken());
        editor.putString(LoginSharedPreferences.ACCESS_TOKEN, token.getAccessToken());
        editor.apply();

        return response.request()
                .newBuilder()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build();
    }
}

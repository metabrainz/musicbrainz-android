package org.metabrainz.mobile.data.sources.api;

import android.content.SharedPreferences;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicBrainzServiceGenerator {
    public static final String API_BASE_URL = "https://musicbrainz.org/ws/2/";
    public static final String AUTH_URL = "https://test.musicbrainz.org/oauth2/authorize";
    public static final String CLIENT_ID = "X2rIt5DgKkd0W6XsSjBqWg";
    public static final String CLIENT_SECRET = "FA5Fa8zEJQs43Jpig_rtgw";
    public static final String OAUTH_REDIRECT_URI = "org.metabrainz.mobile://oauth";

    private static final int TIMEOUT = 20000;
    private static OAuthAuthenticator authenticator;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static HeaderInterceptor headerInterceptor;

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> service, SharedPreferences preferences,
                                      boolean requiresAuthenticator) {
        headerInterceptor = new HeaderInterceptor(preferences);
        addInterceptors(headerInterceptor);
        if (requiresAuthenticator) addAuthenticator(preferences);
        addInterceptors(loggingInterceptor);
        return retrofit.create(service);
    }

    private static void addAuthenticator(SharedPreferences preferences) {
        authenticator = new OAuthAuthenticator(preferences);
        httpClientBuilder.authenticator(authenticator);
        builder.client(httpClientBuilder.build());
        retrofit = builder.build();
    }

    private static void addInterceptors(Interceptor interceptor) {
        if (!httpClientBuilder.interceptors().contains(interceptor)) {
            httpClientBuilder.addInterceptor(interceptor);
            builder.client(httpClientBuilder.build());
            retrofit = builder.build();
        }
    }
}

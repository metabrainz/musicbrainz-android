package org.metabrainz.mobile.data.sources.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicBrainzServiceGenerator {
    private static final String API_BASE_URL = "https://musicbrainz.org/ws/2/";
    public static final String AUTH_URL = "https://musicbrainz.org/oauth2/authorize";
    public static final String ACOUST_ID_BASE_URL = "https://api.acoustid.org/v2/lookup";
    public static final String CLIENT_ID = "BZn9PT8PXtzoUvR1ZIaXFw";
    public static final String CLIENT_SECRET = "WN6o5cjehjPAP4dib0zOmQ";
    public static final String OAUTH_REDIRECT_URI = "org.metabrainz.mobile://oauth";
    public static final String ACOUST_ID_KEY = "5mgEECwRkp";

    private static final int TIMEOUT = 20000;
    private static OAuthAuthenticator authenticator;

    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static HeaderInterceptor headerInterceptor;

    private static final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> service, boolean requiresAuthenticator) {

        headerInterceptor = new HeaderInterceptor();
        addInterceptors(headerInterceptor);

        // Authenticator should not be added for requests to refresh token and gaining access token
        if (requiresAuthenticator) addAuthenticator();

        addInterceptors(loggingInterceptor);
        return retrofit.create(service);
    }

    private static void addAuthenticator() {
        authenticator = new OAuthAuthenticator();
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

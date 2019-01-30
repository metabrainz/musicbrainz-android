package org.metabrainz.mobile.api.webservice;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicBrainzServiceGenerator {
    public static final String API_BASE_URL = "http://metabrainz.org/ws/2/";
    public static final String AUTH_URL = "https://musicbrainz.org/oauth2/authorize";
    public static final String CLIENT_ID = "7YOlN2TqOe5LaD1zdZpzeQ";
    public static final String  CLIENT_SECRET = "iJ45_lhc5-docYBfLNRaaA";
    public static final String OAUTH_REDIRECT_URI = "http://localhost";

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static MusicBrainzService createService(){
        if (!httpClientBuilder.interceptors().contains(loggingInterceptor)) {
            httpClientBuilder.addInterceptor(loggingInterceptor);
            builder.client(httpClientBuilder.build());
            retrofit = builder.build();
        }
        return retrofit.create(MusicBrainzService.class);
    }
}

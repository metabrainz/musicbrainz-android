package org.metabrainz.mobile.data.sources.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("User-agent", "MusicBrainzAndroid/Test (kartikohri13@gmail.com)")
                .addHeader("Accept", "application/json")
                .build();
        return chain.proceed(request);
    }
}

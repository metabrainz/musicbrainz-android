package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.acoustid.AcoustIDResponse;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaggerService {

    @GET("recording/")
    Call<RecordingSearchResponse> searchRecording(@Query("query") String searchQuery,
                                                  @Query("limit") int limit);

    @GET("release/{MBID}")
    Call<Release> lookupRecording(@Path("MBID") String MBID,
                                  @Query("inc") String params);

    @POST("https://api.acoustid.org/v2/lookup")
    Call<AcoustIDResponse> lookupFingerprint(@Query("client") String client,
                                             @Query("meta") String metadata,
                                             @Query("duration") long duration,
                                             @Query("fingerprint") String fingerprint);
}

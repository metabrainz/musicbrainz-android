package org.metabrainz.mobile.data.sources.api

import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.acoustid.AcoustIDResponse
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaggerService {
    @GET("recording/")
    fun searchRecording(@Query("query") searchQuery: String?,
                        @Query("limit") limit: Int): Call<RecordingSearchResponse?>?

    @GET("release/{MBID}")
    fun lookupRecording(@Path("MBID") MBID: String?,
                        @Query("inc") params: String?): Call<Release?>?

    @POST("https://api.acoustid.org/v2/lookup")
    fun lookupFingerprint(@Query("client") client: String?,
                          @Query("meta") metadata: String?,
                          @Query("duration") duration: Long,
                          @Query("fingerprint") fingerprint: String?): Call<AcoustIDResponse?>?

    @GET("http://coverartarchive.org/release/{MBID}")
    suspend fun getCoverArt(@Path("MBID") MBID: String?): CoverArt
}
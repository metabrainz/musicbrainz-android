package org.metabrainz.android.service

import okhttp3.ResponseBody
import org.metabrainz.android.model.entities.CoverArt
import org.metabrainz.android.model.entities.RecordingItem
import org.metabrainz.android.model.entities.WikiSummary
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.model.response.BarcodeReleaseResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LookupService {
    @GET("{entity}/{MBID}")
    suspend fun lookupEntityData(@Path("entity") entity: String,
                                 @Path("MBID") MBID: String,
                                 @Query("inc") params: String): ResponseBody

    @GET("http://coverartarchive.org/release/{MBID}")
    suspend fun getCoverArt(@Path("MBID") MBID: String?): org.metabrainz.android.model.entities.CoverArt


    @GET("https://labs.api.listenbrainz.org/mbid-mapping/json")
    suspend fun searchRecording(@Query("[artist_credit_name]") artistCreditName: String?,
                        @Query("[recording_name]") recordingName: String?): ArrayList<RecordingItem>

    @GET("release/{MBID}")
    suspend fun lookupRecording(@Path("MBID") MBID: String?,
                        @Query("inc") params: String?): Release
    @GET("https://en.wikipedia.org/api/rest_v1/page/summary/{title}")
    suspend fun getWikipediaSummary(@Path("title") title: String): WikiSummary

    @GET("https://www.wikidata.org/w/api.php" + "?action=wbgetentities&format=xml&props=sitelinks/urls&format=json")
    suspend fun getWikipediaLink(@Query("ids") id: String): ResponseBody

    @GET("release/")
    suspend fun lookupReleaseWithBarcode(@Query("query") barcode: String): BarcodeReleaseResponse
}
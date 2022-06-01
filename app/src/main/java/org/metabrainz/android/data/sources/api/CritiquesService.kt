package org.metabrainz.android.data.sources.api

import org.metabrainz.android.data.sources.api.entities.critiques.ReviewPayload
import retrofit2.http.GET

interface CritiquesService {

    @GET("review/")
    suspend fun getAllReviews() : ReviewPayload

    @GET("review/?entity_type=artist&sort=published_on&sort_order=desc&?limit=30&offset=2")
    suspend fun getArtistReviews() : ReviewPayload

    @GET("review/?entity_type=release_group&sort=published_on&sort_order=desc&?limit=25&offset=10")
    suspend fun getReleaseGroupReviews() : ReviewPayload

    @GET("review/?entity_type=label&sort=published_on&sort_order=desc&?limit=25&offset=20")
    suspend fun getLabelReviews() : ReviewPayload

    @GET("review/?entity_type=recording&sort=published_on&sort_order=desc&?limit=30&offset=8")
    suspend fun  getRecordingReviews() : ReviewPayload

    @GET("review/?entity_type=event&sort=published_on&sort_order=desc&?limit=30&offset=10")
    suspend fun getEventReviews() : ReviewPayload

    @GET("review/?entity_type=place&sort=published_on&sort_order=desc&?limit=20&offset=10")
    suspend fun getPlaceReviews() : ReviewPayload

    @GET("review/?entity_type=work&sort=published_on&sort_order=desc&?limit=25&offset=10")
    suspend fun getWorkReviews() : ReviewPayload
}
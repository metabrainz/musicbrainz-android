package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.api.entities.critiques.Review
import org.metabrainz.android.util.Resource

interface CritiquesRepository {
    suspend fun fetchAllReviews(): Resource<List<Review>>

    suspend fun fetchArtistReviews(): Resource<List<Review>>

    suspend fun fetchReleaseGroupReviews(): Resource<List<Review>>

    suspend fun fetchLabelReviews(): Resource<List<Review>>

    suspend fun fetchRecordingReviews(): Resource<List<Review>>

    suspend fun fetchEventReviews(): Resource<List<Review>>

    suspend fun fetchPlaceReviews(): Resource<List<Review>>

    suspend fun fetchWorkReviews(): Resource<List<Review>>
}
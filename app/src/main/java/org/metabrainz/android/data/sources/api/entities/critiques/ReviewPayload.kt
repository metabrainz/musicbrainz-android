package org.metabrainz.android.data.sources.api.entities.critiques

data class ReviewPayload(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val reviews: List<Review>
)
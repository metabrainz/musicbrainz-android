package org.metabrainz.android.data.sources.api.entities.critiques


data class LastRevision(
    val id: Int,
    val rating: Float,
    val review_id: String,
    val text: String,
    val timestamp: String
)
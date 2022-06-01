package org.metabrainz.android.data.sources.api.entities.critiques


data class Review(
    val created: String,
    val edits: Int,
    val entity_id: String,
    val entity_type: String,
    val full_name: String,
    val id: String,
    val info_url: String,
    val is_draft: Boolean,
    val is_hidden: Boolean,
    val language: String,
    val last_revision: LastRevision,
    val last_updated: String,
    val license_id: String,
    val popularity: Int,
    val published_on: String,
    val rating: Float,
    val source: String,
    val source_url: String,
    val text: String,
    val user: User,
    val votes_negative_count: Int,
    val votes_positive_count: Int
)
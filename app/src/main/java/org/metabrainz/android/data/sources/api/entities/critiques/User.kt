package org.metabrainz.android.data.sources.api.entities.critiques


data class User(
    val created: String,
    val display_name: String,
    val id: String,
    val karma: Int,
    val user_type: String
)
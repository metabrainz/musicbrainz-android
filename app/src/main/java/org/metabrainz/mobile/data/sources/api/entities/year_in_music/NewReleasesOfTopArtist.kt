package org.metabrainz.mobile.data.sources.api.entities.year_in_music

data class NewReleasesOfTopArtist(
    val artist_credit_mbids: List<String>,
    val artist_credit_names: List<String>,
    val first_release_date: String,
    val release_id: String,
    val title: String,
    val type: String
)
package org.metabrainz.mobile.data.sources.api.entities.year_in_music

data class Data(
    val day_of_week: String,
    val most_listened_year: List<Map<Int,Int>>,
    val most_prominent_color: String,
    val new_releases_of_top_artists: List<NewReleasesOfTopArtist>,
    val similar_users: List<Map<String,Double>>
)
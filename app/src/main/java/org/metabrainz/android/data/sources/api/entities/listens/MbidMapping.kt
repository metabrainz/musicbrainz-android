package org.metabrainz.android.data.sources.api.entities.listens

data class MbidMapping(
    val artist_mbids: List<String>,
    val recording_mbid: String,
    val release_mbid: String
)
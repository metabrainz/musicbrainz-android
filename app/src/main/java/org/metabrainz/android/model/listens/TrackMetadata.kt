package org.metabrainz.android.model.listens

data class TrackMetadata(
    val additional_info: org.metabrainz.android.model.listens.AdditionalInfo?,
    val artist_name: String,
    val mbid_mapping: org.metabrainz.android.model.listens.MbidMapping?,
    val release_name: String,
    val track_name: String
)
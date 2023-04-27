package org.metabrainz.android.model.listens

import org.metabrainz.android.model.entities.CoverArt

data class Listen(
    val inserted_at: String,
    val listened_at: Int,
    val recording_msid: String,
    val track_metadata: org.metabrainz.android.model.listens.TrackMetadata,
    val user_name: String,
    var coverArt: org.metabrainz.android.model.entities.CoverArt? = null
)
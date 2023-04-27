package org.metabrainz.android.model.listens

data class Payload(
    val count: Int,
    val latest_listen_ts: Int,
    val listens: List<org.metabrainz.android.model.listens.Listen>,
    val user_id: String
)
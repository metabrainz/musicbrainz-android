package org.metabrainz.android.model.entities

data class CoverArt(
    val images: List<org.metabrainz.android.model.entities.Image>,
    val release: String
)
package org.metabrainz.mobile.data.sources.api.entities

data class CoverArt(
    val images: List<Image>,
    val release: String
)
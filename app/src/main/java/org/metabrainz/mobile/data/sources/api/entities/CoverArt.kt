package org.metabrainz.mobile.data.sources.api.entities

import java.util.*

class CoverArt {
    var images = ArrayList<Image>()
    var release: String? = null
    val allImageLinks: ArrayList<String?>
        get() {
            val urls = ArrayList<String?>()
            for (image in images) if (image.image!!.isNotEmpty()) urls.add(image.image)
            return urls
        }
    val allThumbnailsLinks: ArrayList<String?>
        get() {
            val urls = ArrayList<String?>()
            for (image in images) if (image.thumbnails != null) urls.add(image.thumbnails!!.small)
            return urls
        }
}
package org.metabrainz.mobile.data.sources.api.entities

import com.google.gson.annotations.SerializedName
import org.metabrainz.mobile.data.sources.api.entities.Image.Thumbnail
import java.util.ArrayList

class Image {
    var types = ArrayList<String>()
    var isFront = false
    var isBack = false
    var image: String? = null
    var thumbnails: Thumbnail? = null

    class Thumbnail {
        var small: String? = null
        var large: String? = null
    }
}
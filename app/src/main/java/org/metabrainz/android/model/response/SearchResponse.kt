package org.metabrainz.android.model.response

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.mbentity.MBEntity

open class SearchResponse<T : org.metabrainz.android.model.mbentity.MBEntity?> {
    @SerializedName("created")
    var timestamp: String? = null
    var count = 0
    var offset = 0
    var items: List<T>? = null
}
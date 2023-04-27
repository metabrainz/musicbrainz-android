package org.metabrainz.android.model.response

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.mbentity.Collection
import java.util.*

class CollectionListResponse {
    @SerializedName("collection-offset")
    var offset: String? = null

    @SerializedName("collection-limit")
    var limit: String? = null
    var collections: List<org.metabrainz.android.model.mbentity.Collection> = ArrayList()
}
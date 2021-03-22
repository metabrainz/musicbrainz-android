package org.metabrainz.mobile.data.sources.api.entities

import com.google.gson.annotations.SerializedName
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label

class LabelInfo {
    var label: Label? = null

    @SerializedName("catalog-number")
    var catalogNumber: String? = null
}
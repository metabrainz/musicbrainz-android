package org.metabrainz.android.data.sources.api.entities

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.data.sources.api.entities.mbentity.Label

class LabelInfo {
    var label: Label? = null

    @SerializedName("catalog-number")
    var catalogNumber: String? = null
}
package org.metabrainz.android.model.entities

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.mbentity.Label

class LabelInfo {
    var label: org.metabrainz.android.model.mbentity.Label? = null

    @SerializedName("catalog-number")
    var catalogNumber: String? = null
}
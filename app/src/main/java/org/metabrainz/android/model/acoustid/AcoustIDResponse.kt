package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class AcoustIDResponse {
    @SerializedName("results")
    @Expose
    var results: List<org.metabrainz.android.model.acoustid.Result> = ArrayList()

    @SerializedName("status")
    @Expose
    var status: String? = null
}
package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Result {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("recordings")
    @Expose
    var recordings: List<org.metabrainz.android.model.acoustid.Recording> = ArrayList()

    @SerializedName("score")
    @Expose
    var score = 0.0
}
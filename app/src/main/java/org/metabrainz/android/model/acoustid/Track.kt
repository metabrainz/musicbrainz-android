package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Track {
    @SerializedName("artists")
    @Expose
    var artists: List<org.metabrainz.android.model.acoustid.Artist> = ArrayList()

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("position")
    @Expose
    var position = 0
}
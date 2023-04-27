package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReleaseEvent {
    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("date")
    @Expose
    var date: org.metabrainz.android.model.acoustid.Date? = null
}
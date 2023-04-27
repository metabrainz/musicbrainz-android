package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class Recording {
    @SerializedName("artists")
    @Expose
    var artists: List<org.metabrainz.android.model.acoustid.Artist> = ArrayList()

    @SerializedName("duration")
    @Expose
    var duration = 0

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("releasegroups")
    @Expose
    var releaseGroups: List<org.metabrainz.android.model.acoustid.ReleaseGroup> = ArrayList()

    @SerializedName("title")
    @Expose
    var title: String? = null
    var sources = 0
}
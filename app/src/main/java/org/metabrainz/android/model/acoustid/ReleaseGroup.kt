package org.metabrainz.android.model.acoustid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ReleaseGroup {
    @SerializedName("artists")
    @Expose
    var artists: List<org.metabrainz.android.model.acoustid.Artist> = ArrayList()

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("releases")
    @Expose
    var releases: List<org.metabrainz.android.model.acoustid.Release> = ArrayList()

    @SerializedName("secondarytypes")
    @Expose
    var secondarytypes: List<String> = ArrayList()

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null
}
package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.entities.ArtistCredit
import org.metabrainz.android.model.entities.Link
import java.util.*

class Recording : org.metabrainz.android.model.mbentity.MBEntity() {
    var title: String? = null
    var length: Long = 0

    @SerializedName("artist-credit")
    var artistCredits: MutableList<org.metabrainz.android.model.entities.ArtistCredit> = ArrayList()
        set(artistCredits) {
            this.artistCredits.addAll(artistCredits)
        }

    @SerializedName("track-count")
    var trackCount = 0
    var score = 0
    var relations: MutableList<Link> = ArrayList()
        set(relations) {
            this.relations.addAll(relations)
        }
    var releases: MutableList<Release> = ArrayList()
        set(releases) {
            this.releases.addAll(releases)
        }
    val duration: String
        get() {
            val builder = StringBuilder()
            var seconds = length / 1000
            val minutes = seconds / 60
            seconds %= 60
            builder.append(minutes).append(':')
            if (seconds < 10) builder.append('0')
            builder.append(seconds)
            return builder.toString()
        }
}
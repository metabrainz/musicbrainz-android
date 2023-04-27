package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.entities.LifeSpan
import org.metabrainz.android.model.entities.Link
import java.util.*

class Artist : org.metabrainz.android.model.mbentity.MBEntity() {
    var name: String? = null
    var country: String? = null
    var type: String? = null

    @SerializedName("sort-name")
    var sortName: String? = null
    var area: org.metabrainz.android.model.mbentity.Area? = null

    @SerializedName("begin-area")
    var beginArea: org.metabrainz.android.model.mbentity.Area? = null

    @SerializedName("end-area")
    var endArea: org.metabrainz.android.model.mbentity.Area? = null

    @SerializedName("life-span")
    var lifeSpan: LifeSpan? = null
    var gender: String? = null
    var relations: MutableList<Link> = ArrayList()
        set(relations) {
            this.relations.addAll(relations)
        }
    @JvmField
    val releases: MutableList<org.metabrainz.android.model.mbentity.Release> = ArrayList()
    fun getReleases(): List<org.metabrainz.android.model.mbentity.Release> {
        return releases
    }

    fun setReleases(releases: List<org.metabrainz.android.model.mbentity.Release>?) {
        this.releases.addAll(releases!!)
    }

    fun setRelease(release: org.metabrainz.android.model.mbentity.Release, position: Int) {
        releases[position] = release
    }

    override fun toString(): String {
        return "Artist{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}'
    }
}
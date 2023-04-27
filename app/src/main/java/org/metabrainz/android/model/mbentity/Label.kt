package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.entities.LifeSpan
import org.metabrainz.android.model.entities.Link
import java.util.*

class Label : org.metabrainz.android.model.mbentity.MBEntity() {
    var name: String? = null
    var type: String? = null

    @SerializedName("label-code")
    var code: String? = null
        get() = if (field != null) "LC $field" else ""

    @SerializedName("life-span")
    var lifeSpan: LifeSpan? = null
    var country: String? = null
    var area: org.metabrainz.android.model.mbentity.Area? = null
    var relations: MutableList<Link> = ArrayList()
        set(relations) {
            this.relations.addAll(relations)
        }
    var releases: MutableList<org.metabrainz.android.model.mbentity.Release> = ArrayList()
        set(releases) {
            this.releases.addAll(releases)
        }

    override fun toString(): String {
        return "Label{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", area=" + area +
                '}'
    }
}
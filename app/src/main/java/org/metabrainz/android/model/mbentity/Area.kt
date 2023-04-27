package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.entities.LifeSpan
import java.util.*

class Area : org.metabrainz.android.model.mbentity.MBEntity() {
    //TODO: ISO codes field to be added
    var type: String? = null
    var name: String? = null

    @SerializedName("sort-name")
    var sortName: String? = null
    private val aliases: MutableList<org.metabrainz.android.model.entities.Alias> = ArrayList()

    @SerializedName("life-span")
    var lifeSpan: LifeSpan? = null
    override fun toString(): String {
        return "Area{" +
                "id='" + mbid + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", sortName='" + sortName + '\'' +
                ", aliases=" + aliases +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                '}'
    }

    fun getAliases(): List<org.metabrainz.android.model.entities.Alias> {
        return aliases
    }

    fun setAliases(aliases: List<org.metabrainz.android.model.entities.Alias>?) {
        this.aliases.addAll(aliases!!)
    }
}
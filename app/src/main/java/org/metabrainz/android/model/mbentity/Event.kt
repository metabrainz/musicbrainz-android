package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.data.sources.api.entities.LifeSpan

class Event : org.metabrainz.android.model.mbentity.MBEntity() {
    //TODO: Add Relations Field
    var name: String? = null

    @SerializedName("life-span")
    var lifeSpan: LifeSpan? = null
    var type: String? = null
    var time: String? = null
    override fun toString(): String {
        return "Event{" +
                "name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                '}'
    }
}
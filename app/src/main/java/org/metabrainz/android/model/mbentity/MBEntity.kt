package org.metabrainz.android.model.mbentity

import com.google.gson.annotations.SerializedName
import org.metabrainz.android.model.userdata.Rating
import org.metabrainz.android.model.userdata.Tag
import org.metabrainz.android.model.userdata.UserRating
import org.metabrainz.android.model.userdata.UserTag
import java.io.Serializable
import java.util.*

open class MBEntity : Serializable {
    @SerializedName("id")
    var mbid: String? = null
    var disambiguation: String? = null

    @SerializedName("user-rating")
    var userRating: UserRating? = null
    var rating: Rating? = null

    @SerializedName("user-tags")
    var userTags: List<UserTag> = ArrayList()
    var tags: List<Tag> = ArrayList()

    @SerializedName("user-genres")
    var userGenres: List<UserTag> = ArrayList()
    var genres: List<Tag> = ArrayList()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MBEntity) return false
        return mbid == other.mbid
    }

    override fun hashCode(): Int {
        return mbid.hashCode()
    }
}
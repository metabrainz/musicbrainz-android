package org.metabrainz.android.model.entities

import org.metabrainz.android.model.mbentity.Artist

class ArtistCredit {
    var name: String? = null
    var joinphrase: String? = null
    var artist: Artist? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is org.metabrainz.android.model.entities.ArtistCredit) return false
        return name == other.name && joinphrase == other.joinphrase && artist == other.artist
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (joinphrase?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        return result
    }
}
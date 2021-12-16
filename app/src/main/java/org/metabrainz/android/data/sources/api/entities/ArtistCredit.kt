package org.metabrainz.android.data.sources.api.entities

import org.metabrainz.android.data.sources.api.entities.mbentity.Artist

class ArtistCredit {
    var name: String? = null
    var joinphrase: String? = null
    var artist: Artist? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ArtistCredit) return false
        val that = o
        return name == that.name && joinphrase == that.joinphrase && artist == that.artist
    }
}
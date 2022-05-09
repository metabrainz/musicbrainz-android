package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.api.entities.CoverArt
import org.metabrainz.android.data.sources.api.entities.listens.Listen
import org.metabrainz.android.util.Resource

interface ListensRepository {
    suspend fun fetchUserListens(userName: String): Resource<List<Listen>>
    suspend fun fetchCoverArt(MBID: String): Resource<CoverArt>
}
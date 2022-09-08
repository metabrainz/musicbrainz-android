package org.metabrainz.android.data.repository

import kotlinx.coroutines.flow.Flow
import org.metabrainz.android.data.sources.brainzplayer.Song

interface SongRepository {
    fun getSongsStream() : Flow<List<Song>>
    suspend fun addSongs(): Boolean

}
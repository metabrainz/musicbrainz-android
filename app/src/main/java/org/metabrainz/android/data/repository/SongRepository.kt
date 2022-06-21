package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.brainzplayer.Song

interface SongRepository {
    suspend fun fetchSongs() : List<Song>
}
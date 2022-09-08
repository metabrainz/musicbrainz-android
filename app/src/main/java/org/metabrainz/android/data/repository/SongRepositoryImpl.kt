package org.metabrainz.android.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.metabrainz.android.data.dao.SongDao
import org.metabrainz.android.data.di.brainzplayer.Transformer.toSong
import org.metabrainz.android.data.di.brainzplayer.Transformer.toSongEntity
import org.metabrainz.android.data.sources.brainzplayer.Song
import org.metabrainz.android.presentation.features.brainzplayer.musicsource.SongData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepositoryImpl @Inject constructor(
    private val songDao: SongDao)
    : SongRepository {
    override fun getSongsStream(): Flow<List<Song>> =
        songDao.getSongEntities()
            .map { it ->
                it.map {
                    it.toSong()
                }
            }

    override suspend fun addSongs(): Boolean {
         val songs = SongData().fetchSongs().map {
            it.toSongEntity()
        }
        songDao.addSongs(songs)
        return songs.isNotEmpty()
    }
}
package org.metabrainz.android.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.metabrainz.android.data.dao.AlbumDao
import org.metabrainz.android.data.di.brainzplayer.Transformer.toAlbum
import org.metabrainz.android.data.di.brainzplayer.Transformer.toAlbumEntity
import org.metabrainz.android.data.sources.brainzplayer.Album
import org.metabrainz.android.data.sources.brainzplayer.Song
import org.metabrainz.android.presentation.features.brainzplayer.musicsource.AlbumsData
import org.metabrainz.android.presentation.features.brainzplayer.musicsource.SongData
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao
): AlbumRepository {
    override fun getAlbums(): Flow<List<Album>> =
        albumDao.getAlbumEntities()
            .map{ it ->
                it.map {
                    it.toAlbum()
                }
            }

    override fun getAlbum(albumId: Long): Flow<Album> =
        albumDao.getAlbumEntity(albumId)
            .map { it.toAlbum() }


    override suspend fun addAlbums(): Boolean {
        val albums = AlbumsData().fetchAlbums().map {
                it.toAlbumEntity()
        }
        albumDao.addAlbums(albums)
        return albums.isNotEmpty()
    }

    override fun getAllSongsOfAlbum(albumId: Long): Flow<List<Song>> {
        val songs = SongData().fetchSongs().filter { song->
            song.albumID == albumId.toString()
        }
        return flowOf(songs)
    }
}
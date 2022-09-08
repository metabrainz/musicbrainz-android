package org.metabrainz.android.presentation.features.brainzplayer.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.metabrainz.android.data.repository.AlbumRepository
import org.metabrainz.android.data.sources.brainzplayer.Album
import org.metabrainz.android.data.sources.brainzplayer.Song
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
) : ViewModel() {
    val albums = albumRepository.getAlbums()
    init {
        viewModelScope.launch {
            albums.collectLatest {
                if (it.isEmpty()) albumRepository.addAlbums()
            }
        }
    }
    fun getAlbumFromID(albumID: Long): Flow<Album> {
        return albumRepository.getAlbum(albumID)
    }
    fun getAllSongsOfAlbum(albumID: Long): Flow<List<Song>>{
        return albumRepository.getAllSongsOfAlbum(albumID)
    }
}
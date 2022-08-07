package org.metabrainz.android.presentation.features.brainzplayer.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.metabrainz.android.data.sources.brainzplayer.Song
import org.metabrainz.android.presentation.features.brainzplayer.services.BrainzPlayerService
import org.metabrainz.android.presentation.features.brainzplayer.services.BrainzPlayerServiceConnection
import org.metabrainz.android.util.BrainzPlayerExtensions.currentPlaybackPosition
import org.metabrainz.android.util.BrainzPlayerExtensions.isPlayEnabled
import org.metabrainz.android.util.BrainzPlayerExtensions.isPlaying
import org.metabrainz.android.util.BrainzPlayerExtensions.isPrepared
import org.metabrainz.android.util.BrainzPlayerUtils.MEDIA_ROOT_ID
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class BrainzPlayerViewModel @Inject constructor(
    private val brainzPlayerServiceConnection: BrainzPlayerServiceConnection
) : ViewModel() {
    private val _mediaItems = MutableStateFlow<Resource<List<Song>>>(Resource.loading())
    private val _songDuration = MutableStateFlow(0L)
    private val _progress = MutableStateFlow(0F)
    val mediaItems = _mediaItems.asStateFlow()
    val progress = _progress.asStateFlow()

    private val playbackState = brainzPlayerServiceConnection.playbackState
    val isShuffled = brainzPlayerServiceConnection.shuffleState
    val isConnected = brainzPlayerServiceConnection.isConnected
    val currentlyPlayingSong = brainzPlayerServiceConnection.currentPlayingSong
    val isPlaying = brainzPlayerServiceConnection.isPlaying
    val playButton = brainzPlayerServiceConnection.playButtonState

    init {
        updatePlayerPosition()
        _mediaItems.value = Resource.loading()
        brainzPlayerServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map { song ->
                        Song(
                            song.mediaId!!,
                            song.description.title.toString(),
                            song.description.subtitle.toString(),
                            song.description.mediaUri.toString(),
                            song.description.iconUri.toString(),
                        )
                    }
                    _mediaItems.value = Resource(Resource.Status.SUCCESS, items)
                }
            }
        )
    }

    fun skipToNextSong() {
        brainzPlayerServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        brainzPlayerServiceConnection.transportControls.skipToPrevious()
    }

    fun onSeek(seekTo: Float) {
        viewModelScope.launch { _progress.emit(seekTo) }
    }

    fun onSeeked() {
        brainzPlayerServiceConnection.transportControls.seekTo((_songDuration.value * progress.value).toLong())
    }
    fun shuffle(){
        val transportControls = brainzPlayerServiceConnection.transportControls
        transportControls.setShuffleMode(if(isShuffled.value) PlaybackStateCompat.SHUFFLE_MODE_NONE else PlaybackStateCompat.SHUFFLE_MODE_ALL)
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value.isPrepared ?: false
        if (isPrepared && mediaItem.mediaID == currentlyPlayingSong.value.getString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID
            )
        ) {
            playbackState.value.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) brainzPlayerServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> brainzPlayerServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            brainzPlayerServiceConnection.transportControls.playFromMediaId(mediaItem.mediaID, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        brainzPlayerServiceConnection.unsubscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }

    private fun updatePlayerPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val pos = playbackState.value.currentPlaybackPosition.toFloat()
                if (progress.value != pos) {
                    _progress.emit(pos / BrainzPlayerService.currentSongDuration)
                    _songDuration.emit(BrainzPlayerService.currentSongDuration)
                }
                delay(500)
            }
        }
    }
}
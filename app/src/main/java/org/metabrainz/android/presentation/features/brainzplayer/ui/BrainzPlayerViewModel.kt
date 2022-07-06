package org.metabrainz.android.presentation.features.brainzplayer.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.metabrainz.android.data.sources.brainzplayer.Song
import org.metabrainz.android.presentation.features.brainzplayer.services.BrainzPlayerServiceConnection
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
    val mediaItems = _mediaItems.asStateFlow()
    val isConnected = brainzPlayerServiceConnection.isConnected
    val currentlyPlayingSong = brainzPlayerServiceConnection.currentPlayingSong
    private val playbackState = brainzPlayerServiceConnection.playbackState
    var isPlaying = brainzPlayerServiceConnection.isPlaying

    init {
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
                            song.description.mediaUri.toString()
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

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaID == currentlyPlayingSong.value?.getString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID
            )
        ) {
            playbackState.value?.let { playbackState ->
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
}
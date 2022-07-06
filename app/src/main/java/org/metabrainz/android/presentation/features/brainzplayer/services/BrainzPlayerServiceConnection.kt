package org.metabrainz.android.presentation.features.brainzplayer.services

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.metabrainz.android.util.Resource

class BrainzPlayerServiceConnection(
    context: Context
) {
    lateinit var mediaController: MediaControllerCompat

    private val _isConnected = MutableStateFlow<Resource<Boolean>>(Resource.loading())
    val isConnected = _isConnected.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackStateCompat?>(EMPTY_PLAYBACK_STATE)
    val playbackState = _playbackState.asStateFlow()

    private val _currentlyPlayingSong = MutableStateFlow<MediaMetadataCompat?>(NOTHING_PLAYING)
    val currentPlayingSong = _currentlyPlayingSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            BrainzPlayerService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    //To skip, pause, resume etc in player
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    //subscribe and unsubscribe will be called from ViewModel to subscribe and unsubscribe from a mediaID to get access of mediaItems from local
    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            _isConnected.value = Resource(Resource.Status.SUCCESS, true)
        }

        override fun onConnectionSuspended() {
            _isConnected.value = Resource(Resource.Status.FAILED,false)
        }

        override fun onConnectionFailed() {
            _isConnected.value = Resource(Resource.Status.FAILED, false)
        }

    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.value = state ?: EMPTY_PLAYBACK_STATE

        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _currentlyPlayingSong.value =
                if (metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) == null)
                    NOTHING_PLAYING
                else metadata
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()
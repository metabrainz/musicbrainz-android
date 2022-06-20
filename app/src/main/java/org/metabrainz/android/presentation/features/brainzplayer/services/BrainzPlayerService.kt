package org.metabrainz.android.presentation.features.brainzplayer.services

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.metabrainz.android.presentation.features.brainzplayer.musicsource.LocalMusicSource
import org.metabrainz.android.util.BrainzPlayerUtils.MEDIA_ROOT_ID
import javax.inject.Inject

@AndroidEntryPoint
class BrainzPlayerService: MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var localMusicSource: LocalMusicSource

    var isForegroundService = false
    private var isPlayerInitialized = false
    private var currentSong: MediaBrowserServiceCompat? = null

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID,null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultSent = localMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(localMusicSource.asMediaItem())
                        if (!isPlayerInitialized && localMusicSource.songs.isNotEmpty()) {
                            preparePlayer(localMusicSource.songs, localMusicSource.songs[0], false)
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultSent) {
                    result.detach()

                }
            }
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        serviceScope.launch(Dispatchers.Main) {
            val currentSongIndex = if (currentSong == null) 0 else songs.indexOf(itemToPlay)
            exoPlayer.setMediaSource(localMusicSource.asMediaSource(dataSourceFactory))
            exoPlayer.prepare()
            exoPlayer.seekTo(currentSongIndex, 0L)
            exoPlayer.playWhenReady = playNow
        }
    }

}
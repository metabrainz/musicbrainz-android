package org.metabrainz.android.presentation.features.spotify

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.ContentApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.*
import kotlinx.coroutines.launch
import org.metabrainz.android.R
import org.metabrainz.android.databinding.AppRemoteLayoutBinding
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.AuthParams.CLIENT_ID
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.AuthParams.REDIRECT_URI
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.SpotifySampleContexts.ALBUM_URI
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.SpotifySampleContexts.ARTIST_URI
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.SpotifySampleContexts.PLAYLIST_URI
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.SpotifySampleContexts.PODCAST_URI
import org.metabrainz.android.presentation.features.spotify.RemotePlayerActivity.SpotifySampleContexts.TRACK_URI
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RemotePlayerActivity : AppCompatActivity() {

    object AuthParams {
        const val CLIENT_ID = "da5388546fde4aa8a47aad3539e7f87b"
        const val REDIRECT_URI = "org.metabrainz.android://callback"
    }

    object SpotifySampleContexts {
        const val TRACK_URI = "spotify:track:4IWZsfEkaK49itBwCTFDXQ"
        const val ALBUM_URI = "spotify:album:4m2880jivSbbyEGAKfITCa"
        const val ARTIST_URI = "spotify:artist:3WrFJ7ztbogyGnTHbHJFl2"
        const val PLAYLIST_URI = "spotify:playlist:37i9dQZEVXbMDoHDwVN2tF"
        const val PODCAST_URI = "spotify:show:2tgPYIeGErjk6irHRhk9kj"
    }

    companion object {
        const val TAG = "MusicBrainz Player"
        const val STEP_MS = 15000L
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private var playerStateSubscription: Subscription<PlayerState>? = null
    private var playerContextSubscription: Subscription<PlayerContext>? = null
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private lateinit var views: List<View>
    private lateinit var trackProgressBar: TrackProgressBar
    private lateinit var binding: AppRemoteLayoutBinding

    private val errorCallback = { throwable: Throwable -> logError(throwable) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppRemoteLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekTo.apply {
            isEnabled = false
            progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        }

        trackProgressBar = TrackProgressBar(binding.seekTo) { seekToPosition: Long -> seekTo(seekToPosition) }

        views = listOf(
            binding.subscribeToPlayerContextButton,
            binding.subscribeToPlayerStateButton,
            binding.playPauseButton,
            binding.seekForwardButton,
            binding.seekBackButton,
            binding.skipPrevButton,
            binding.skipNextButton,
            binding.seekTo)

        SpotifyAppRemote.setDebugMode(true)

        onDisconnected()
        connect()
    }


    private val playerContextEventCallback = Subscription.EventCallback<PlayerContext> { playerContext ->
        binding.currentContextLabel.apply {
            text = String.format(Locale.US, "%s", playerContext.title)
            tag = playerContext
        }
    }

    private val playerStateEventCallback = Subscription.EventCallback<PlayerState> { playerState ->
        Log.v(TAG, String.format("Player State: %s", gson.toJson(playerState)))

        updateTrackStateButton(playerState)

        updatePlayPauseButton(playerState)

        updateTrackCoverArt(playerState)

        updateSeekbar(playerState)
    }

    private fun updatePlayPauseButton(playerState: PlayerState) {
        // Invalidate play / pause
        when {
            playerState.isPaused -> {
                binding.playPauseButton.setImageResource(R.drawable.btn_play)
            }
            else -> {
                binding.playPauseButton.setImageResource(R.drawable.btn_pause)
            }
        }
    }

    private fun updateTrackStateButton(playerState: PlayerState) {
        binding.currentTrackLabel.apply {
            text = String.format(Locale.US, "%s\n%s", playerState.track.name, playerState.track.artist.name)
            tag = playerState
        }
    }

    private fun AppCompatImageButton.setTint(@ColorInt tint: Int) {
        DrawableCompat.setTint(drawable, Color.WHITE)
    }

    private fun updateSeekbar(playerState: PlayerState) {
        // Update progressbar
        trackProgressBar.apply {
            when {
                playerState.playbackSpeed > 0 -> {
                    unpause()
                }
                else -> {
                    pause()
                }
            }
            // Invalidate seekbar length and position
            binding.seekTo.max = playerState.track.duration.toInt()
            binding.seekTo.isEnabled = true
            setDuration(playerState.track.duration)
            update(playerState.playbackPosition)
        }
    }

    private fun updateTrackCoverArt(playerState: PlayerState) {
        // Get image from track
        assertAppRemoteConnected()
            .imagesApi
            .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
            .setResultCallback { bitmap ->
                binding.image.setImageBitmap(bitmap)
            }
    }

    private fun seekTo(seekToPosition: Long) {
        assertAppRemoteConnected()
            .playerApi
            .seekTo(seekToPosition)
            .setErrorCallback(errorCallback)
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        onDisconnected()
    }

    private fun onConnected() {
        for (input in views) {
            input.isEnabled = true
        }

        onSubscribedToPlayerStateButtonClicked(binding.subscribeToPlayerStateButton)
        onSubscribedToPlayerContextButtonClicked(binding.subscribeToPlayerContextButton)
    }

    private fun onDisconnected() {
        for (view in views) {
            view.isEnabled = false
        }
        binding.image.setImageResource(R.drawable.widget_placeholder)
        binding.subscribeToPlayerContextButton.apply {
            visibility = View.VISIBLE
            setText(R.string.title_player_context)
        }
        binding.subscribeToPlayerStateButton.apply {
            visibility = View.VISIBLE
            setText(R.string.title_current_track)
        }

        binding.currentContextLabel.visibility = View.INVISIBLE
        binding.currentTrackLabel.visibility = View.INVISIBLE
    }

    private fun connect() {

        SpotifyAppRemote.disconnect(spotifyAppRemote)
        lifecycleScope.launch {
            try {
                spotifyAppRemote = connectToAppRemote(true)
                onConnected()
            } catch (error: Throwable) {
                onDisconnected()
                logError(error)
            }
        }
    }

    private suspend fun connectToAppRemote(showAuthView: Boolean): SpotifyAppRemote? =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.connect(
                application,
                ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(showAuthView)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        cont.resume(spotifyAppRemote)
                    }

                    override fun onFailure(error: Throwable) {
                        cont.resumeWithException(error)
                    }
                })
        }

    fun onPlayPodcastButtonClicked(notUsed: View) {
        playUri(PODCAST_URI)
    }

    fun onPlayTrackButtonClicked(notUsed: View) {
        playUri(TRACK_URI)
    }

    fun onPlayAlbumButtonClicked(notUsed: View) {
        playUri(ALBUM_URI)
    }

    fun onPlayArtistButtonClicked(notUsed: View) {
        playUri(ARTIST_URI)
    }

    fun onPlayPlaylistButtonClicked(notUsed: View) {
        playUri(PLAYLIST_URI)
    }

    private fun playUri(uri: String) {
        assertAppRemoteConnected()
            .playerApi
            .play(uri)
            .setResultCallback { logMessage(getString(R.string.command_feedback, "play")) }
            .setErrorCallback(errorCallback)
    }

    fun showCurrentPlayerContext(view: View) {
        view.tag?.let {
            showDialog("PlayerContext", gson.toJson(it))
        }
    }

    fun showCurrentPlayerState(view: View) {
        view.tag?.let {
            showDialog("PlayerState", gson.toJson(it))
        }
    }

    fun onToggleShuffleButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .toggleShuffle()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "toggle shuffle")) }
            .setErrorCallback(errorCallback)
    }

    fun onToggleRepeatButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .toggleRepeat()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "toggle repeat")) }
            .setErrorCallback(errorCallback)
    }

    fun onSkipPreviousButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .skipPrevious()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "skip previous")) }
            .setErrorCallback(errorCallback)
    }

    fun onPlayPauseButtonClicked(notUsed: View) {
        assertAppRemoteConnected().let {
            it.playerApi
                .playerState
                .setResultCallback { playerState ->
                    if (playerState.isPaused) {
                        it.playerApi
                            .resume()
                            .setResultCallback { logMessage(getString(R.string.command_feedback, "play")) }
                            .setErrorCallback(errorCallback)
                    } else {
                        it.playerApi
                            .pause()
                            .setResultCallback { logMessage(getString(R.string.command_feedback, "pause")) }
                            .setErrorCallback(errorCallback)
                    }
                }
        }

    }

    fun onSkipNextButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .skipNext()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "skip next")) }
            .setErrorCallback(errorCallback)
    }

    fun onSeekBack(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .seekToRelativePosition(-STEP_MS)
            .setResultCallback { logMessage(getString(R.string.command_feedback, "seek back")) }
            .setErrorCallback(errorCallback)
    }

    fun onSeekForward(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .seekToRelativePosition(STEP_MS)
            .setResultCallback { logMessage(getString(R.string.command_feedback, "seek fwd")) }
            .setErrorCallback(errorCallback)
    }

    private fun convertToList(inputItems: ListItems?): List<ListItem> {
        return if (inputItems?.items != null) {
            inputItems.items.toList()
        } else {
            emptyList()
        }
    }

    private suspend fun loadRootRecommendations(appRemote: SpotifyAppRemote): ListItems? =
        suspendCoroutine { cont ->
            appRemote.contentApi
                .getRecommendedContentItems(ContentApi.ContentType.FITNESS)
                .setResultCallback { listItems -> cont.resume(listItems) }
                .setErrorCallback { throwable ->
                    errorCallback.invoke(throwable)
                    cont.resumeWithException(throwable)
                }
        }

    private suspend fun loadChildren(appRemote: SpotifyAppRemote, parent: ListItem): ListItems? =
        suspendCoroutine { cont ->
            appRemote.contentApi
                .getChildrenOfItem(parent, 6, 0)
                .setResultCallback { listItems -> cont.resume(listItems) }
                .setErrorCallback { throwable ->
                    errorCallback.invoke(throwable)
                    cont.resumeWithException(throwable)
                }
        }

    fun onSubscribedToPlayerContextButtonClicked(notUsed: View) {
        playerContextSubscription = cancelAndResetSubscription(playerContextSubscription)

        binding.currentContextLabel.visibility = View.VISIBLE
        binding.subscribeToPlayerContextButton.visibility = View.INVISIBLE
        playerContextSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerContext()
            .setEventCallback(playerContextEventCallback)
            .setErrorCallback { throwable ->
                binding.currentContextLabel.visibility = View.INVISIBLE
                binding.subscribeToPlayerContextButton.visibility = View.VISIBLE
                logError(throwable)
            } as Subscription<PlayerContext>
    }

    fun onSubscribedToPlayerStateButtonClicked(notUsed: View) {
        playerStateSubscription = cancelAndResetSubscription(playerStateSubscription)

        binding.currentTrackLabel.visibility = View.VISIBLE
        binding.subscribeToPlayerStateButton.visibility = View.INVISIBLE

        playerStateSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerState()
            .setEventCallback(playerStateEventCallback)
            .setLifecycleCallback(
                object : Subscription.LifecycleCallback {
                    override fun onStart() {
                        logMessage("Event: start")
                    }

                    override fun onStop() {
                        logMessage("Event: end")
                    }
                })
            .setErrorCallback {
                binding.currentTrackLabel.visibility = View.INVISIBLE
                binding.subscribeToPlayerStateButton.visibility = View.VISIBLE
            } as Subscription<PlayerState>
    }

    private fun <T : Any?> cancelAndResetSubscription(subscription: Subscription<T>?): Subscription<T>? {
        return subscription?.let {
            if (!it.isCanceled) {
                it.cancel()
            }
            null
        }
    }

    private fun assertAppRemoteConnected(): SpotifyAppRemote {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                return it
            }
        }
        Log.e(TAG, getString(R.string.err_spotify_disconnected))
        throw SpotifyDisconnectedException()
    }

    private fun logError(throwable: Throwable) {
        Log.e(TAG, "", throwable)
    }

    private fun logMessage(msg: String) {
        Log.d(TAG, msg)
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).create().show()
    }
}

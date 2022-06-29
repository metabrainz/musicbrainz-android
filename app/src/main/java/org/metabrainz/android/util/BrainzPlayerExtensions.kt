package org.metabrainz.android.util

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import org.metabrainz.android.data.sources.brainzplayer.Song

object BrainzPlayerExtensions {

    //MediaMetadataCompat extensions
    inline val MediaMetadataCompat?.toSong
        get() = this?.description?.let {
            Song(
                mediaID = it.mediaId.toString(),
                title = it.title.toString(),
                artist = it.subtitle.toString(),
                uri = it.mediaUri.toString()
            )
        } ?: Song()

    inline val Song?.toMediaMetadataCompat
        get() = this?.let { song ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.mediaID)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.title)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, song.uri.toString())
                .build()
        }

    //PlaybackStateCompat extensions
    inline val PlaybackStateCompat.isPrepared
        get() = state == PlaybackStateCompat.STATE_BUFFERING ||
                state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED

    inline val PlaybackStateCompat.isPlaying
        get() = state == PlaybackStateCompat.STATE_BUFFERING ||
                state == PlaybackStateCompat.STATE_PLAYING

    inline val PlaybackStateCompat.isPlayEnabled
        get() = actions and PlaybackStateCompat.ACTION_PLAY != 0L ||
                (actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L) ||
                ( state == PlaybackStateCompat.STATE_PAUSED)
}
package org.metabrainz.android.presentation.features.brainzplayer.musicsource

import android.support.v4.media.MediaBrowserCompat
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

interface MusicSource<T> {
    var songs: List<T>
    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory) : ConcatenatingMediaSource
    fun asMediaItem(): MutableList<MediaBrowserCompat.MediaItem>
    fun whenReady(action: (Boolean) -> Unit): Boolean
}
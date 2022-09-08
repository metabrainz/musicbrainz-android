package org.metabrainz.android.data.di.brainzplayer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.metabrainz.android.data.repository.PlaylistRepository
import org.metabrainz.android.data.repository.PlaylistRepositoryImpl

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class PlaylistRepositoryModule {
        @Binds
        abstract fun bindsPlaylistRepository(repository: PlaylistRepositoryImpl?): PlaylistRepository?
    }
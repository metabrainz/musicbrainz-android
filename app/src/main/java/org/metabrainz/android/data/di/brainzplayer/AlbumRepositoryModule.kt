package org.metabrainz.android.data.di.brainzplayer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.metabrainz.android.data.repository.AlbumRepository
import org.metabrainz.android.data.repository.AlbumRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumRepositoryModule {
    @Binds
    abstract fun bindsAlbumRepository(repository: AlbumRepositoryImpl?) : AlbumRepository?
}
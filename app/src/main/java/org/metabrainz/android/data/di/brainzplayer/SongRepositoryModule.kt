package org.metabrainz.android.data.di.brainzplayer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.metabrainz.android.data.repository.SongRepository
import org.metabrainz.android.data.repository.SongRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongRepositoryModule {
    @Binds
    abstract fun bindsSongRepository(repository: SongRepositoryImpl?) : SongRepository?
}
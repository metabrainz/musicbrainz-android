package org.metabrainz.android.data.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.android.data.repository.LookupRepositoryImpl
import org.metabrainz.android.data.repository.LookupRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class LookupRepositoryModule {
    @Binds
    abstract fun bindsLookupRepository(repository: LookupRepositoryImpl?): LookupRepository?
}
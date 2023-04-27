package org.metabrainz.android.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.android.repository.LookupRepositoryImpl
import org.metabrainz.android.repository.LookupRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class LookupRepositoryModule {
    @Binds
    abstract fun bindsLookupRepository(repository: LookupRepositoryImpl?): LookupRepository?
}
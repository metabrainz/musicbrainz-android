package org.metabrainz.mobile.data.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.mobile.data.repository.LookupRepositoryImpl
import org.metabrainz.mobile.data.repository.LookupRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class LookupRepositoryModule {
    @Binds
    abstract fun bindsLookupRepository(repository: LookupRepositoryImpl?): LookupRepository?
}
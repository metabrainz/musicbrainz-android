package org.metabrainz.android.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.android.repository.CollectionRepository
import org.metabrainz.android.repository.CollectionRepositoryImpl
import org.metabrainz.android.repository.LookupRepositoryImpl
import org.metabrainz.android.repository.LookupRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CollectionRepositoryModule {
    @Binds
    abstract fun bindsCollectionRepository(repository: CollectionRepositoryImpl?): CollectionRepository?
}
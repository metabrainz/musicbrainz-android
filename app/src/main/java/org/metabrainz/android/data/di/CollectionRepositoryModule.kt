package org.metabrainz.android.data.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.android.data.repository.CollectionRepository
import org.metabrainz.android.data.repository.CollectionRepositoryImpl
import org.metabrainz.android.data.repository.LookupRepositoryImpl
import org.metabrainz.android.data.repository.LookupRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CollectionRepositoryModule {
    @Binds
    abstract fun bindsCollectionRepository(repository: CollectionRepositoryImpl?): CollectionRepository?
}
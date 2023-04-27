package org.metabrainz.android.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.android.repository.BlogRepository
import org.metabrainz.android.repository.BlogRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class BlogRepositoryModule {
    @Binds
    abstract fun bindsBlogRepository(repository: BlogRepositoryImpl?): BlogRepository?
}
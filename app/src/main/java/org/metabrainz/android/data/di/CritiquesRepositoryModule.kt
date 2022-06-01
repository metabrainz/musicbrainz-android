package org.metabrainz.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.metabrainz.android.data.repository.CritiquesRepository
import org.metabrainz.android.data.repository.CritiquesRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CritiquesRepositoryModule {
    @Binds
    abstract fun bindsCritiquesRepository(repository: CritiquesRepositoryImpl?) : CritiquesRepository?
}
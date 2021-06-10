package org.metabrainz.mobile.data.sources.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator.createService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {

    @get:Provides
    @get:Singleton
    val lookupService: LookupService = createService(LookupService::class.java, true)

    @get:Provides
    @get:Singleton
    val collectionService: CollectionService = createService(CollectionService::class.java, true)

    @get:Provides
    @get:Singleton
    val loginService: LoginService = createService(LoginService::class.java, false)

}
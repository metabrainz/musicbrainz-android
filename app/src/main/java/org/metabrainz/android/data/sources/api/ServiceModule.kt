package org.metabrainz.android.data.sources.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.metabrainz.android.data.sources.api.MusicBrainzServiceGenerator.createService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @get:Provides
    @get:Singleton
    val lookupService: LookupService = createService(LookupService::class.java, true)

    @get:Provides
    @get:Singleton
    val blogService: BlogService = Retrofit.Builder()
        .baseUrl("https://public-api.wordpress.com/rest/v1.1/sites/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(BlogService::class.java)

    @get:Provides
    @get:Singleton
    val listensService: ListensService = Retrofit.Builder()
        .baseUrl("https://api.listenbrainz.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ListensService::class.java)

    @get:Provides
    @get:Singleton
    val collectionService: CollectionService = createService(CollectionService::class.java, true)

    @get:Provides
    @get:Singleton
    val loginService: LoginService = createService(LoginService::class.java, false)

    @get:Provides
    @get:Singleton
    val critiquesService: CritiquesService = Retrofit.Builder()
    .baseUrl("https://critiquebrainz.org/ws/1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build().create(CritiquesService::class.java)

}
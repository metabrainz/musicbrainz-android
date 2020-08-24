package org.metabrainz.mobile.data.sources.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class ServiceModule {

    @Singleton
    @Provides
    public LookupService getLookupService() {
        return MusicBrainzServiceGenerator.createService(LookupService.class, true);
    }

    @Singleton
    @Provides
    public CollectionService getCollectionService() {
        return MusicBrainzServiceGenerator.createService(CollectionService.class, true);
    }

    @Singleton
    @Provides
    public LoginService getLoginService() {
        return MusicBrainzServiceGenerator.createService(LoginService.class, false);
    }

    @Singleton
    @Provides
    public TaggerService getTaggerService() {
        return MusicBrainzServiceGenerator.createService(TaggerService.class, false);
    }


}

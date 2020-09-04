package org.metabrainz.mobile.data.di;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.repository.LookupRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class LookupRepositoryModule {
    @Binds
    public abstract LookupRepository bindsLookupRepository(LookupRepositoryImpl repository);
}

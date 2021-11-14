package org.metabrainz.mobile.data.di

import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.Binds
import dagger.Module
import org.metabrainz.mobile.data.repository.*

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PaymentRepositoryModule {
    @Binds
    abstract fun bindsPaymentRepository(repository: PaymentRepositoryImpl?): PaymentRepository?
}
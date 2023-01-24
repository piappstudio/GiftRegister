package com.piappstudio.giftregister.di

import com.piappstudio.giftregister.di.PiGiftRegisterAnalytic
import com.piappstudio.pianalytic.IPiAnalyticProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticModule {

    @Singleton
    @Provides
    fun getAnalyticsProvider(piGiftRegisterAnalytic: PiGiftRegisterAnalytic): IPiAnalyticProvider {
        return piGiftRegisterAnalytic
    }
}
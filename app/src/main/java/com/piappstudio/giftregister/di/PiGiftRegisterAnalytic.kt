package com.piappstudio.giftregister.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.piappstudio.pianalytic.IPiAnalyticProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiGiftRegisterAnalytic @Inject constructor(): IPiAnalyticProvider {
    override fun crashlytics(): FirebaseCrashlytics {
        return Firebase.crashlytics
    }

    override fun analytics(): FirebaseAnalytics {
        return Firebase.analytics
    }
}
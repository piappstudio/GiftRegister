package com.piappstudio.pianalytic

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.piappstudio.pimodel.PiSession
import timber.log.Timber
import javax.inject.Inject


class PiTracker @Inject constructor(private val iPiAnalyticProvider: IPiAnalyticProvider, val piSession: PiSession) {
    fun trackUserProperty(properties: Map<String, Any>) {
        for ((key, value) in properties) {
            iPiAnalyticProvider.analytics().setUserProperty(key, value.toString())
        }
    }

    fun logEvent(eventName: String, screenName: String? = null, params: Map<String, Any>? = null) {
        Timber.d("Log event is fired $eventName")
        val bundle = Bundle()
        params?.let {
            for ((key, value) in it) {
                bundle.putString(key, value.toString())
            }
        }
        screenName?.let {
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, it)
        }

        iPiAnalyticProvider.analytics().logEvent(eventName, bundle)
    }
}
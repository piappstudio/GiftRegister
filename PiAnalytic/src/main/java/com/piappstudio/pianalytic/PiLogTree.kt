package com.piappstudio.pianalytic

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiLogTree @Inject constructor(private val analyticProvider: IPiAnalyticProvider): Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //super.log(priority, tag, message, t)
    }

    override fun e(message: String?, vararg args: Any?) {
        message?.let {
            analyticProvider.crashlytics().recordException(Throwable(it))
        }
    }

    override fun e(t: Throwable?) {
        t?.let {
            analyticProvider.crashlytics().recordException(t)
        }
    }
}
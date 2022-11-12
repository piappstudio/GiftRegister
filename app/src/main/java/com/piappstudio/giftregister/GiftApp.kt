/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.giftregister

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.google.firebase.FirebaseApp
import com.piappstudio.picloud.ui.auth.isUserLoggedIn
import com.piappstudio.picloud.worker.GoogleDriveSyncWorker
import com.piappstudio.picloud.worker.makeStatusNotification
import androidx.work.Configuration
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pimodel.pref.PiPreference
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GiftApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var preference: PiPreference

    @Inject
    lateinit var piSession: PiSession
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        piSession.appName = applicationContext.getString(R.string.app_name)
        piSession.packageName = BuildConfig.APPLICATION_ID
        piSession.appVersion = BuildConfig.VERSION_NAME
        piSession.buildNumber = BuildConfig.VERSION_CODE.toString()

    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

}
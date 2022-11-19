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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.database.PiDataRepository
import com.piappstudio.pimodel.pref.PiPreference
import com.piappstudio.pinetwork.PiRemoteDataRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GiftApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var preference: PiPreference

    @Inject
    lateinit var piLogTree: PiLogTree

    @Inject
    lateinit var piSession: PiSession


    override fun onCreate() {
        super.onCreate()

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics.getInstance(this)

        Timber.plant(piLogTree)
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
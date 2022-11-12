/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.piappstudio.picloud.R
import com.piappstudio.picloud.ui.auth.isUserLoggedIn
import com.piappstudio.pimodel.Constant
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.pref.PiPrefKey
import com.piappstudio.pimodel.pref.PiPreference
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


@HiltWorker
class GoogleDriveSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerProviderEntryPoint {
        fun piDriveManager(): PiDriveManager
        fun piPreference(): PiPreference
    }

    override suspend fun doWork(): Result {
        val entryPoint =
            EntryPointAccessors.fromApplication(context, WorkerProviderEntryPoint::class.java)
        val piDriveManager = entryPoint.piDriveManager()
        val piPreference = entryPoint.piPreference()
        val info = createForegroundInfo(context.getString(R.string.sync_started))
        setForeground(info)
        var status = Resource.Status.NONE
        piDriveManager.doSync().collect {
            it.data?.let { progress ->
                status = it.status
            }
            if (it.status == Resource.Status.SUCCESS) {
                val date = Constant.PiFormat.orderItemDisplay.format(Date())
                piPreference.save(PiPrefKey.LAST_SYNC_TIME, date)
            }
        }

        while (status == Resource.Status.NONE || status == Resource.Status.LOADING) {
            Timber.d("Wait at driver sync")
            delay(1000)
        }
        return if (status == Resource.Status.SUCCESS) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}
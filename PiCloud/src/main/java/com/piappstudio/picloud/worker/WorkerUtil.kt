/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import com.piappstudio.picloud.R
import java.util.*


val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "GiftRegister Notification"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "GiftRegister status notification"
const val CHANNEL_ID = "GIFT_REGISTER_NOTIFICATION"

fun makeStatusNotification(title: String, message: String, context: Context) {
    val notificationId = 124
    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createChannel(context)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(notificationId, builder.build())
}

/***
 * Foreground service notification for Worker to update the notification
 */
@NonNull
fun CoroutineWorker.createForegroundInfo(@NonNull progress: String): ForegroundInfo {
    val notificationId = UUID.randomUUID().hashCode()

    // Build a notification using bytesRead and contentLength
    val context: Context = applicationContext
    val id = context.getString(R.string.notification_channel_id)
    val title = context.getString(R.string.notification_title)
    val cancel = context.getString(R.string.cancel_download)
    // This PendingIntent can be used to cancel the worker
    val intent: PendingIntent = WorkManager.getInstance(context)
        .createCancelPendingIntent(getId())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createChannel(context)
    }
    val notification: Notification = NotificationCompat.Builder(context, id)
        .setContentTitle(title)
        .setTicker(progress)
        .setSmallIcon(R.drawable.ic_stat_name)
        .setOngoing(true) // Add the cancel action to the notification which can
        // be used to cancel the worker
        .addAction(android.R.drawable.ic_delete, cancel, intent)
        .build()
    return ForegroundInfo(notificationId, notification)
}

/***
 * Foreground service notification for Worker to update the notification
 */

@RequiresApi(Build.VERSION_CODES.O)
fun createChannel(context: Context) {
    val id = context.getString(R.string.notification_channel_id)
    val chan = NotificationChannel(
        id,
        "My Foreground Service",
        NotificationManager.IMPORTANCE_HIGH
    )
    chan.lightColor = Color.BLUE
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    manager?.createNotificationChannel(chan)

}
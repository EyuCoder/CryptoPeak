package com.codexo.cryptopeak.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.ui.MainActivity

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT,
    )

    val cryptoPeakImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_favorite,
    )

    val bigPickStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(cryptoPeakImage)
        .bigLargeIcon(null)

//    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
//    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
//        applicationContext,
//        REQUEST_CODE,
//        snoozeIntent,
//        FLAGS)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.cryptopeak_channel_id)
    )
        .setSmallIcon(R.drawable.ic_favorite)
        .setContentTitle(applicationContext.getString(R.string.app_name))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
//        .setStyle(bigPickStyle)
        .setLargeIcon(cryptoPeakImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .addAction(
//            R.drawable.ic_broken_image,
//            applicationContext.getString(R.string.snooze),
//            snoozePendingIntent
//        )
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() = cancelAll()
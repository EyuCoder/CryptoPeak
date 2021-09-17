package com.codexo.cryptopeak.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.ui.MainActivity

// Notification ID.
private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(coin: CoinData, applicationContext: Context) {
    val messageBody = "${coin.name}: ${coin.changePercent24HrFormatted}"

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT,
    )

    val cryptoPeakImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        if (coin.changePercent24Hr!!.toDouble() > 0) R.drawable.ic_high
        else R.drawable.ic_low,
    )

    val notification = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.cryptopeak_channel_id)
    )
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(applicationContext.getString(R.string.app_name))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(cryptoPeakImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    notify(NOTIFICATION_ID, notification)
}

fun NotificationManager.cancelNotifications() = cancelAll()

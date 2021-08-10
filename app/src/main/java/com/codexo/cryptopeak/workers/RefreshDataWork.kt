package com.codexo.cryptopeak.workers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.receivers.MyReceiver
import com.codexo.cryptopeak.repository.Repository
import com.codexo.cryptopeak.ui.MainActivity
import com.codexo.cryptopeak.utils.sendNotification
import retrofit2.HttpException

class RefreshDataWork(
    context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)

        return try {
            repository.refreshCoin()
            initNotification(applicationContext)
            Log.d("CODEXOX", "doWork: Background refresh once a day")
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }

    companion object {
        const val BACKGROUND_WORK_NAME = "RefreshDataWorker"
    }
}

private fun initNotification(context: Context) {

    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    notificationManager.sendNotification(
        context.getText(R.string.app_name).toString(),
        context
    )
}
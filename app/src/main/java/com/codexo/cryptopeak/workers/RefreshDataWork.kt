package com.codexo.cryptopeak.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.receivers.MyReceiver
import com.codexo.cryptopeak.repository.Repository
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
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}

private lateinit var notifyPendingIntent: PendingIntent
private val REQUEST_CODE = 0

private fun initNotification(context: Context) {
    val notifyIntent = Intent(context, MyReceiver::class.java)
    notifyPendingIntent = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}
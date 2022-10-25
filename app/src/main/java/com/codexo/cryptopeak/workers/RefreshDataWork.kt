package com.codexo.cryptopeak.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinData
import com.codexo.cryptopeak.data.database.CoinDatabase.Companion.getDatabase
import com.codexo.cryptopeak.utils.sendNotification
import retrofit2.HttpException

private val TAG: String = RefreshDataWork::class.java.simpleName

class RefreshDataWork(
    context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)

        return try {
            repository.updateCoinData()
            repository.getRandomCoin()?.let {
                initNotification(applicationContext, it)
            }
            Log.d(TAG, "doWork: Background refresh once a day")
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }

    companion object {
        const val BACKGROUND_WORK_NAME = "RefreshDataWorker"
    }
}

private fun initNotification(context: Context, coin: CoinData) {
    val notificationManager =
        ContextCompat.getSystemService(context, NotificationManager::class.java)!!
    Log.d(TAG, "initNotification: sent ${coin.name}")
    notificationManager.sendNotification(coin, context)
}

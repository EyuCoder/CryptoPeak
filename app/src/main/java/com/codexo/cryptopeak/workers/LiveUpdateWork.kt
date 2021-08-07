package com.codexo.cryptopeak.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.repository.Repository
import kotlinx.coroutines.delay
import retrofit2.HttpException

class LiveUpdateWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val Progress = "LiveUpdateWork"
        private const val delayDuration = 1L
    }

    override suspend fun doWork(): Result {
        val database = CoinDatabase.getDatabase(applicationContext)
        val repository = Repository(database)

        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)

        return try {
            repository.refreshCoin()
            setProgress(firstUpdate)
            delay(delayDuration)
            setProgress(lastUpdate)
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }
}
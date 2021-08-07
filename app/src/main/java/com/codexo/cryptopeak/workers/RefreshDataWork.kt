package com.codexo.cryptopeak.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codexo.cryptopeak.database.CoinDatabase.Companion.getDatabase
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
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}